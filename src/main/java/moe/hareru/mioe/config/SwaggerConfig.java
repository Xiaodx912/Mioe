package moe.hareru.mioe.config;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
//@EnableSwagger2WebMvc
@EnableOpenApi
@EnableWebMvc
//@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(new ApiKey("Authorization", "JWT Bearer token", "header")))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(Collections.singletonList(SecurityReference.builder().scopes(new AuthorizationScope[0]).reference("Authorization").build()))
                        //.forPaths(PathSelectors.regex("/api/.*"))
                        .operationSelector(
                                operationContext -> operationContext.requestMappingPattern().matches("/api/.*")
                        )
                        .build()))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mioe Server接口文档")
                .description("加，都可以加；改，都可以改")
                .contact(new Contact("Github", "https://github.com/Xiaodx912/Mioe", "admin@hareru.moe"))
                .version("0.1")
                .build();
    }
}
