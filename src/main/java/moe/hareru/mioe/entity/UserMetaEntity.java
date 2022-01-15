package moe.hareru.mioe.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("USER_META")
public class UserMetaEntity {
    @TableId
    private Long userId;
    private Long plantTime;
    @JSONField(format = "unixtime")
    private Timestamp syncAt;
    @JSONField(format = "unixtime")
    private Timestamp revokeAt;
}
