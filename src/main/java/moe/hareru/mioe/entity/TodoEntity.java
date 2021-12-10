package moe.hareru.mioe.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TodoEntity {
    private Long todoId;
    private String title;
    private String description;
    private Long importance;
    private Long parentId;
    private Long plantTime;
    private Timestamp deadline;
    private Timestamp createAt;
    private Timestamp completeAt;
    private Timestamp updateAt;
}
