package moe.hareru.mioe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.hareru.mioe.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    UserEntity selectById(Long userId);

    List<UserEntity> selectNickLike(@Param("nickname") String nickname);

    int updateById(UserEntity updatedUser);

    int updateAvatar(Long userId, byte[] newAvatar);

    int insertUser(UserEntity newUser);

}
