package moe.hareru.mioe.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class SwaggerAddition4Login implements ApiListingScannerPlugin {
    /**
     * Implement this method to manually add ApiDescriptions
     * 实现此方法可手动添加ApiDescriptions
     *
     * @param context - Documentation context that can be used infer documentation context
     * @return List of {@link ApiDescription}
     * @see ApiDescription
     */
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        Operation usernamePasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户登录")
                .notes("复制data字段Bearer xxx字符串，填入右上角Authorize内即可使用需鉴权的api。参数也可通过body内JSON传递，JSON参数覆盖query参数。")
                .consumes(new HashSet<>(Collections.singleton(MediaType.APPLICATION_FORM_URLENCODED_VALUE))) // 接收参数格式
                .produces(new HashSet<>(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))) // 返回参数格式
                .tags(new HashSet<>(Collections.singleton("User Login")))
                .requestParameters(Arrays.asList(
                        new RequestParameterBuilder()
                                .description("userId")
                                .name("userId")
                                .parameterIndex(1)
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("password")
                                .name("password")
                                .parameterIndex(2)
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("expire")
                                .name("expire")
                                .parameterIndex(3)
                                .in(ParameterType.QUERY)
                                .required(false)
                                .build()
                ))
                .responses(Collections.singleton(
                        new ResponseBuilder().code(String.valueOf(200)).description("登录返回").build()
                        )
                ).build();

        ApiDescription loginApiDescription = new ApiDescription("login", "/login", "登录接口","登录接口",
                List.of(usernamePasswordOperation), false);

        return List.of(loginApiDescription);
    }

    /**
     * 是否使用此插件
     *
     * @param documentationType swagger文档类型
     * @return true 启用
     */
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.OAS_30.equals(documentationType);
    }

}
