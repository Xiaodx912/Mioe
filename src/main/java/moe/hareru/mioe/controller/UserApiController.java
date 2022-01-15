package moe.hareru.mioe.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import moe.hareru.mioe.entity.JSONResponseEntity;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.entity.UserMetaEntity;
import moe.hareru.mioe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(tags = "User Api")
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("查询全部资料")
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getAllUserInfo() {
        List<UserEntity> userEntityList = userService.queryUser(null);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntityList);
    }

    @ApiOperation("查询公开资料")
    @GetMapping("{userId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public JSONResponseEntity getUserPublicInfo(@PathVariable("userId") Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return new JSONResponseEntity(HttpStatus.NOT_FOUND.value(), "No such user id", null);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity.publicInfo());
    }

    @ApiOperation("查询详细资料")
    @GetMapping("{userId}/detail")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getUserFullInfo(@PathVariable("userId") Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return new JSONResponseEntity(HttpStatus.NOT_FOUND.value(), "No such user id", null);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity);
    }

    @ApiOperation("更新资料")
    @PostMapping(value = "{userId}/detail", consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity setUserInfo(@PathVariable("userId") Long userId, @RequestBody JSONObject data) {
        if (!Objects.equals(data.getLong("userId"), userId)) {
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Illegal parameter", null);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority adminAuth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN").get(0);
        if (data.containsKey("role") && !authentication.getAuthorities().contains(adminAuth)) {
            return new JSONResponseEntity(HttpStatus.FORBIDDEN.value(), "Authority failure", null);
        }
        data.remove("watchList");//Do not support watch modify by detail post
        String newPassword = data.getString("password");
        if (newPassword != null) {//BCrypt encode if password update
            data.put("password", new BCryptPasswordEncoder().encode(newPassword));
        }
        JSONObject userJson = (JSONObject) JSONObject.toJSON(userService.getUserById(userId));
        for (Map.Entry<String, Object> e : data.entrySet()) {
            if (e.getValue() != null) {
                userJson.put(e.getKey(), e.getValue());
            }
        }
        UserEntity updatedEntity = JSONObject.toJavaObject(userJson, UserEntity.class);
        if (userService.updateUserById(updatedEntity))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", updatedEntity);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update failure", null);
    }

    @ApiOperation("搜索昵称")
    @GetMapping("search")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public JSONResponseEntity searchByNickname(String nickname) {
        if (nickname == null || nickname.length() == 0)
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Illegal parameter", null);
        List<UserEntity> userEntityList = userService.searchNickname(nickname);
        JSONArray jsonArray = new JSONArray();
        if (userEntityList != null) {
            for (UserEntity userEntity : userEntityList) {
                jsonArray.add(userEntity.publicInfo());
            }
        }
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", jsonArray);
    }

    @ApiOperation(value = "获取头像")
    @GetMapping(value = "{userId}/avatar", produces = "image/jpeg")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public byte[] getUserAvatar(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return null;
        return userService.getUserById(userId).getAvatar();
    }

    @ApiOperation("更新头像")
    @PostMapping(value = "{userId}/avatar", consumes = "image/jpeg", produces = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity setUserAvatar(@PathVariable Long userId, @RequestBody byte[] avatar) {
        if (userService.updateAvatarById(userId, avatar))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", null);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update failure", null);
    }

    @ApiOperation("获取关注列表")
    @GetMapping("{userId}/watchlist")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getUserWatchList(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return new JSONResponseEntity(HttpStatus.NO_CONTENT.value(), "No such user id", null);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity.getWatchList());
    }

    @ApiOperation("添加关注")
    @PostMapping(value = "{userId}/watchlist", consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity updateUserWatchList(@PathVariable("userId") Long userId, @RequestBody JSONObject data) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return new JSONResponseEntity(HttpStatus.NO_CONTENT.value(), "No such user id", null);
        if (!data.containsKey("watchList")) {
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Illegal parameter", null);
        }
        userEntity.getWatchList().addAll(new HashSet<>(data.getJSONArray("watchList").toJavaList(Integer.class)));
        if (userService.updateUserById(userEntity))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity.getWatchList());
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update failure", null);
    }

    @ApiOperation("删除关注")
    @DeleteMapping(value = "{userId}/watchlist", consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity deleteUserWatchList(@PathVariable("userId") Long userId, @RequestBody JSONObject data) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return new JSONResponseEntity(HttpStatus.NO_CONTENT.value(), "No such user id", null);
        if (!data.containsKey("watchList")) {
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Illegal parameter", null);
        }
        userEntity.getWatchList().removeAll(new HashSet<>(data.getJSONArray("watchList").toJavaList(Integer.class)));
        if (userService.updateUserById(userEntity))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity.getWatchList());
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update failure", null);
    }

    @ApiOperation("吊销用户已签发token")
    @PostMapping(value = "{userId}/logout")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity revokeUserToken(@PathVariable Long userId){
        if(userService.revokeUserToken(userId))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", null);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Revoke fail", null);
    }

    @ApiOperation("获取Meta信息")
    @GetMapping(value = "{userId}/meta")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getUserMetaInfo(@PathVariable Long userId){
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userService.getUserMeta(userId));
    }

    @ApiOperation("更新Meta信息")
    @PostMapping(value = "{userId}/meta")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity updateUserMetaInfo(@PathVariable Long userId, @RequestBody UserMetaEntity data){
        if(!Objects.equals(userId, data.getUserId()))
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Illegal parameter", null);
        if(userService.updateUserMeta(data))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userService.getUserMeta(userId));
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update fail", null);
    }

}
