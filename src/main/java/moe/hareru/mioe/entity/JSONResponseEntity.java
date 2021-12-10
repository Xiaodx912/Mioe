package moe.hareru.mioe.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONResponseEntity {
    @JSONField(ordinal = 1)
    private int status;
    @JSONField(ordinal = 2)
    private String msg;
    @JSONField(ordinal = 3)
    private Object data;

    public Object getData() {
        return data == null ? "" : data;
    }

    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
