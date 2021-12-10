package moe.hareru.mioe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.hareru.mioe.entity.TodoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper extends BaseMapper<TodoEntity> {

}
