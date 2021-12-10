package moe.hareru.mioe;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;

@SpringBootTest
class MioeApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void mapperTest(){
        List<UserEntity> userList = userMapper.selectNickLike("k");//.selectList(null);
        userList.forEach(System.out::println);
        UserEntity u ;
        u=userMapper.selectById(3L);
        System.out.println(u);
        int r= userMapper.updateById(new UserEntity(2L,"pass","new_nick","ROLE_VOID",null,new HashSet<>(asList(233,666))));
        userList = userMapper.selectNickLike("n");//.selectList(null);
        userList.forEach(System.out::println);
        for(UserEntity e:userList){System.out.println(JSONObject.toJSONString(e));}

    }

}
