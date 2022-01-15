package moe.hareru.mioe.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.entity.UserMetaEntity;
import moe.hareru.mioe.mapper.UserMapper;
import moe.hareru.mioe.mapper.UserMetaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMetaMapper userMetaMapper;


    public UserEntity getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    public UserEntity getUserById(String userIdStr) {
        return getUserById(Long.parseLong(userIdStr));
    }

    public List<UserEntity> queryUser(QueryWrapper<UserEntity> query) {
        return userMapper.selectList(query);
    }

    public List<UserEntity> searchNickname(String nickname) {
        if (nickname == null) return null;
        nickname = nickname.replaceAll("[^a-zA-Z0-9]", "");
        if (nickname.length() == 0) return null;
        return userMapper.selectNickLike(nickname);
    }

    public Boolean updateUserById(UserEntity updatedUser) {
        return userMapper.updateById(updatedUser) == 1;
    }

    public Boolean updateAvatarById(Long userId, byte[] newAvatar) {
        return userMapper.updateAvatar(userId, newAvatar) == 1;
    }

    public Boolean addNewUser(UserEntity userEntity) {
        return userMapper.insertUser(userEntity) == 1 &&
               userMetaMapper.insert(new UserMetaEntity(userEntity.getUserId(),null,null,null)) == 1;
    }

    public Boolean updateUserMeta(UserMetaEntity userMetaEntity){
        return userMetaMapper.updateById(userMetaEntity)==1;
    }

    public UserMetaEntity getUserMeta(Long userId){
        return userMetaMapper.selectById(userId);
    }

    public Boolean revokeUserToken(Long userId){
        if (userId==null)return false;
        UserMetaEntity userMetaEntity=new UserMetaEntity();
        userMetaEntity.setUserId(userId);
        userMetaEntity.setRevokeAt(new Timestamp(System.currentTimeMillis()));
        return userMetaMapper.updateById(userMetaEntity)==1;
    }

    public Long getRevokeTimeMs(Long userId){
        if (userId==null)return 0L;
        UserMetaEntity userMetaEntity = userMetaMapper.selectById(userId);
        if (userMetaEntity==null)return 0L;
        try {
            return userMetaEntity.getRevokeAt().getTime();
        }catch (Exception e){
            return 0L;
        }
    }

    public Boolean userTodoSynced(Long userId){
        if (userId==null)return false;
        UserMetaEntity userMetaEntity=new UserMetaEntity();
        userMetaEntity.setUserId(userId);
        userMetaEntity.setSyncAt(new Timestamp(System.currentTimeMillis()));
        return userMetaMapper.updateById(userMetaEntity)==1;
    }

}
