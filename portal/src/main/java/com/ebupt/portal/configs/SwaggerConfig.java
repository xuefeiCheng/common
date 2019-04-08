package com.ebupt.portal.configs;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String env;

    @Bean public Docket authApi() {
        return createApi("权限管理", "com.ebupt.portal.shiro");
    }

    @Bean public Docket allApi() {
        return createApi("全部接口", "com.ebupt.portal");
    }

    private Docket createApi(String groupName, String basePackage) {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("服务器正常响应")
                .responseModel(new ModelRef("JSONResult")).build());

        Predicate<RequestHandler> predicate = requestHandler -> {
            if (RequestHandlerSelectors.basePackage(basePackage).apply(requestHandler)) // 匹配成功
                return requestHandler.isAnnotatedWith(ApiOperation.class);
            return false;
        };

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo()).select()
                .apis(predicate)
                .paths(PathSelectors.any())
                .build();

        if ("dev".equals(env)) { // 开发环境
            docket.enable(true);
        } else { // 生产环境
            docket.enable(false);
        }

        return docket;
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("ebupt", "http://www.ebupt.com", "dengna@ebupt.com");

        return new ApiInfoBuilder().title("EBUPT API文档")
                .description("本公司所有接口全部采用Restful风格。公司官网:http://www.ebupt.com")
                .contact(contact).version("1.0").build();
    }

}
