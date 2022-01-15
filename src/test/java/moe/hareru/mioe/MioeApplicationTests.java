package moe.hareru.mioe;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.hareru.mioe.entity.TodoEntity;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.mapper.TodoMapper;
import moe.hareru.mioe.mapper.UserMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class MioeApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TodoMapper todoMapper;

    @Test
    void userMapperTest(){
        System.out.println("-------userMapperTest----------");
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

    @Test
    void todoMapperTest(){
        System.out.println("-------todoMapperTest----------");
        Assertions.assertEquals("user 3 test todo 1", todoMapper.selectById(3).getTitle());
        Assertions.assertEquals("user 1 test todo 2", todoMapper.selectByTidCid(2L,1L).getTitle());
        Assertions.assertNull(todoMapper.selectByTidCid(2L,2L));
        todoMapper.selectByCid(1L,99L,0L).forEach(System.out::println);
        Assertions.assertEquals(2, todoMapper.selectByCid(1L,99L,0L).size());
        TodoEntity e=new TodoEntity(null,1L,"test","test",null,null,null, Timestamp.valueOf("2021-12-31 12:34:56"),null,null,null,null);
        Assertions.assertEquals(1, todoMapper.insert(e));
        System.out.println(e);
        Assertions.assertEquals(1L, todoMapper.findCreatorByTid(e.getTodoId()));
        Assertions.assertNull(todoMapper.findCreatorByTid(e.getTodoId()+1));
        e.setImportance(233L);
        Assertions.assertEquals(1, todoMapper.updateById(e));
        Assertions.assertEquals(233L, todoMapper.selectByTidCid(e.getTodoId(),e.getCreatorId()).getImportance());
    }

    @Test
    void tmp(){
        System.out.println("-------tmp----------");
        TodoEntity e;

        e=todoMapper.selectByTidCid(4L,2L);
        todoMapper.deleteById(4L);
        todoMapper.insert(e);
        e=new TodoEntity(null,1L,"test insert","test",null,null,null, Timestamp.valueOf("2021-12-31 12:34:56"),null,null,null,null);
        Assertions.assertEquals(1, todoMapper.insert(e));
        System.out.println(e);
    }

}
