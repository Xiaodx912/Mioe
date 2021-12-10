package moe.hareru.mioe.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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
        return userMapper.insertUser(userEntity) == 1;
    }

}
