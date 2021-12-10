package moe.hareru.mioe.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication(scanBasePackages = {"com.alibaba.fastjson"})
public class UseFastjson {
    @Bean
    public HttpMessageConverters converter() {
        FastJsonHttpMessageConverter convert = new FastJsonHttpMessageConverter();

        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                //SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty);
        config.setDateFormat("yyyy-MM-dd hh:mm:ss");
        convert.setFastJsonConfig(config);

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        convert.setSupportedMediaTypes(fastMediaTypes);

        return new HttpMessageConverters(convert);
    }

    public static void main(String[] args) {
        SpringApplication.run(UseFastjson.class, args);
    }

}

