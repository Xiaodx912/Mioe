package moe.hareru.mioe.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import moe.hareru.mioe.entity.TodoEntity;
import moe.hareru.mioe.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TodoService {

    private final TodoMapper todoMapper;
    @Autowired
    public TodoService(TodoMapper todoMapper) { this.todoMapper = todoMapper; }

    public TodoEntity getTodoByTidCid(String todoId, String creatorId) {
        long tid,cid;
        try {
            tid = Long.parseLong(todoId);
            cid = Long.parseLong(creatorId);
        } catch (Exception e) {
            return null;
        }
        return getTodoByTidCid(tid, cid);
    }
    public TodoEntity getTodoByTidCid(Long todoId, Long creatorId){
        return todoMapper.selectByTidCid(todoId,creatorId);
    }

    public List<TodoEntity> getTodoListByCid(Long creatorId, Long limit, Long offset){
        return todoMapper.selectByCid(creatorId, limit, offset);
    }

    public Boolean insertTodo(TodoEntity todoEntity){
        return todoMapper.insert(todoEntity)==1;
    }

    public Boolean updateTodo(TodoEntity todoEntity){
        if(todoEntity.getTodoId()==null||todoEntity.getCreatorId()==null)return Boolean.FALSE;
        UpdateWrapper<TodoEntity> updateWrapper = new UpdateWrapper<TodoEntity>()
                .eq("todo_id",todoEntity.getTodoId())
                .eq("creator_id",todoEntity.getCreatorId());
        return todoMapper.update(todoEntity,updateWrapper)==1;
    }

    public Boolean updateTodoByTid(TodoEntity todoEntity){
        return todoMapper.updateById(todoEntity)==1;
    }

    public Boolean deleteTodo(TodoEntity todoEntity){
        if(todoEntity.getTodoId()==null||todoEntity.getCreatorId()==null)return Boolean.FALSE;
        QueryWrapper<TodoEntity> queryWrapper = new QueryWrapper<TodoEntity>()
                .eq("todo_id",todoEntity.getTodoId())
                .eq("creator_id",todoEntity.getCreatorId());
        return todoMapper.delete(queryWrapper)==1;
    }

    public Boolean deleteTodoByTid(TodoEntity todoEntity){
        return todoMapper.deleteById(todoEntity)==1;
    }

    public Boolean overwriteUserTodo(List<TodoEntity> todoEntityList){
        for(TodoEntity todoEntity:todoEntityList) {
            if (todoEntity.getTodoId() != null && todoEntity.getTodoId() <= 0)
                todoEntity.setTodoId(null);
            if (todoEntity.getTodoId() != null && !Objects.equals(todoMapper.findCreatorByTid(todoEntity.getTodoId()), todoEntity.getCreatorId()))
                throw new IllegalArgumentException("Invalid parameter");
        }
        try {
            for (TodoEntity todoEntity : todoEntityList) {
                Boolean res;
                if (todoEntity.getTodoId() == null || todoEntity.getTodoId()<=0) {
                    res = this.insertTodo(todoEntity);
                } else {
                    res = this.updateTodoByTid(todoEntity);
                }
                assert res;
            }
        }catch (AssertionError e){
            throw new RuntimeException("Unknown fail, atomic operation broke");
        }
        return Boolean.TRUE;
    }

//    public Boolean setTodoCompleteness(){
//        return Boolean.TRUE;
//    }
}
