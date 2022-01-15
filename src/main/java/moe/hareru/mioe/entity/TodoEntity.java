package moe.hareru.mioe.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TODO")
public class TodoEntity {           //  insert                  update

    @TableId(type = IdType.AUTO)
    private Long todoId;            //  no(auto generate)       readonly(as index)

    private Long creatorId;         //  yes(require)            readonly(as index)
    private String title;           //  yes(require)            option
    private String description;     //  no                      option
    private Long importance;        //  no                      option
    private Long parentId;          //  no                      option
    private Long plantTime;         //  no                      option
    @JSONField(format="unixtime")
    private Timestamp deadline;     //  yes(require)            option
    @JSONField(format="unixtime")
    private Timestamp createAt;     //  no(auto generate)       no
    @JSONField(format="unixtime")
    private Timestamp completeAt;   //  no
    @JSONField(format="unixtime")
    private Timestamp updateAt;     //  no(auto generate)
    private String localId;           //  option(random)          option

    public void setParentId(Long parentId){
        this.parentId=(parentId==null||parentId<=0)?null:parentId;
    }
}
