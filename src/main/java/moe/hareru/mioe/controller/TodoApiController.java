package moe.hareru.mioe.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import moe.hareru.mioe.entity.JSONResponseEntity;
import moe.hareru.mioe.entity.TodoEntity;
import moe.hareru.mioe.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(tags = "Todo Api")
@RestController
@RequestMapping("/api/todo")
public class TodoApiController {

    private final TodoService todoService;

    @Autowired
    public TodoApiController(TodoService todoService) { this.todoService = todoService; }

    @ApiOperation("获取用户todo列表")
    @GetMapping("{userId}")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getUserTodoList(@PathVariable("userId") Long userId, Long limit, Long max_id){
        List<TodoEntity> todoEntityList = todoService.getTodoListByCid(userId,limit,max_id);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", todoEntityList);
    }


    @ApiOperation("同步用户todo列表")
    @PostMapping(value = "{userId}", consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity syncUserTodoList(@PathVariable("userId") Long userId, @RequestBody List<TodoEntity> data){
        for(TodoEntity e:data) if(!Objects.equals(e.getCreatorId(), userId))
                return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Invalid parameter", null);
        try {
            if(todoService.overwriteUserTodo(data))
                return new JSONResponseEntity(HttpStatus.OK.value(), "OK", null);
            else
                return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Sync fail", null);
        } catch (Exception e){
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @ApiOperation("获取用户特定todo")
    @GetMapping("{userId}/{todoId}")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity getUserSpecificTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId){
        TodoEntity todoEntity = todoService.getTodoByTidCid(todoId, userId);
        return new JSONResponseEntity(HttpStatus.OK.value(), "OK", todoEntity);
    }

    @ApiOperation("更新用户特定todo")
    @PutMapping(value = "{userId}/{todoId}",consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity updateUserSpecificTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId, @RequestBody TodoEntity data){
        if (!Objects.equals(userId, data.getCreatorId()) || !Objects.equals(todoId, data.getTodoId()))
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Invalid parameter", null);
        if(todoService.updateTodoByTid(data))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", todoService.getTodoByTidCid(todoId,userId));
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update fail", null);
    }

    @ApiOperation("删除用户特定todo")
    @DeleteMapping(value = "{userId}/{todoId}",consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity deleteUserSpecificTodo(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId, @RequestBody TodoEntity data){
        if (!Objects.equals(userId, data.getCreatorId()) || !Objects.equals(todoId, data.getTodoId()))
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Invalid parameter", null);
        if(todoService.deleteTodo(data))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", null);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Delete fail", null);
    }



    @ApiOperation("新增todo")
    @PostMapping(value = "{userId}/new",consumes = "application/json")
    @PreAuthorize("(hasAuthority('ROLE_USER') and principal.username==#userId.toString()) or hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity addNewTodo(@PathVariable("userId") Long userId, @RequestBody TodoEntity data){
        if (!Objects.equals(userId, data.getCreatorId()))
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Invalid parameter", null);
        data.setTodoId(null);
        if(todoService.insertTodo(data))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", data);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Update fail", null);
    }

}
