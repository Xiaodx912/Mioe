package moe.hareru.mioe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.hareru.mioe.entity.TodoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper extends BaseMapper<TodoEntity> {

    TodoEntity selectByTidCid(Long todoId, Long creatorId);

    List<TodoEntity> selectByCid(Long creatorId, Long limit, Long offset);

    Long findCreatorByTid(Long todoId);

}
