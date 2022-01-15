package moe.hareru.mioe.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("USER")
public class UserEntity {
    @JSONField(ordinal = 1)
    private Long userId;
    @JSONField(ordinal = 9)
    private String password;
    @JSONField(ordinal = 2)
    private String nickname;
    private String role;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private byte[] avatar;

    @JSONField(ordinal = 5)
    @TableField("watch")//Mybatis will use get/set method of "watch" instead of "watchList"
    @NonNull
    private Set<Integer> watchList = new HashSet<>();

    @JSONField(serialize = false)
    public void setWatch(Object watch) {
        this.watchList.clear();
        for (Object e : (Object[]) watch) {
            this.watchList.add((Integer) e);
        }
    }

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    public Integer[] getWatch() {
        return watchList.toArray(Integer[]::new);
    }


    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    public String getUserIdStr() {
        return getUserId().toString();
    }

    @JSONField(name = "role", ordinal = 8)
    public Collection<? extends GrantedAuthority> getRoleList() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }

    @JSONField(serialize = false)
    public String getRole() {
        return role;
    }

    public void setRole(Object role) {//Patch for asymmetric getter/setter
        if (role instanceof String)//Mapper source (String)
            this.role = (String) role;
        else if (role instanceof JSONArray) {//JSON.toJavaObject source (JArray)
            List<String> roleList = new ArrayList<>();
            for (Object e : (JSONArray) role) {
                roleList.add(((JSONObject) e).getString("authority"));
            }
            this.role = StringUtils.join(roleList, ',');
        }
    }

    @JSONField(serialize = false)
    public JSONObject publicInfo() {
        JSONObject data = new JSONObject();
        data.put("userId", getUserId());
        data.put("nickname", getNickname());
        byte[] avatar = getAvatar();
        String b64Avatar = avatar == null ? "" : "data:image/jpg;base64," + Base64.getEncoder().encodeToString(avatar);
        data.put("avatar", b64Avatar);
        return data;
    }
}
