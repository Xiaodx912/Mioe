package moe.hareru.mioe.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("JSON响应结构")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONResponseEntity {
    @ApiModelProperty(value = "响应状态码",notes = "定义同HTTP状态码")
    @JSONField(ordinal = 1)
    private int status;
    @ApiModelProperty("响应信息")
    @JSONField(ordinal = 2)
    private String msg;
    @ApiModelProperty("返回数据")
    @JSONField(ordinal = 3)
    private Object data;

    public Object getData() {
        return data == null ? "" : data;
    }

    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
