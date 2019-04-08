package com.ebupt.portal.canyon.common.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import java.util.function.Predicate;

/**
 * Swagger配置类
 *
 * @author chy
 * @date 2019-02-27 19:29
 */
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket systemApi() {
		return createApi("系统管理", "com.ebupt.portal.canyon.system.controller");
	}

	@Bean
	public Docket allApi() {
		return createApi("全部接口", "com.ebupt.portal.canyon");
	}

	private Docket createApi(String groupName, String basePackage) {
		List<ResponseMessage> list = new ArrayList<>();
		list.add(new ResponseMessageBuilder().code(404).message("未找到")
				.responseModel(new ModelRef("JsonResult")).build());
		list.add(new ResponseMessageBuilder().code(500).message("请求失败")
				.responseModel(new ModelRef("JsonResult")).build());

		Predicate<RequestHandler> predicate = requestHandler -> {
			if (RequestHandlerSelectors.basePackage(basePackage).apply(requestHandler)) {
				return requestHandler.isAnnotatedWith(ApiOperation.class);
			}
			return false;
		};

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.groupName(groupName)
				.globalResponseMessage(RequestMethod.GET, list)
				.globalResponseMessage(RequestMethod.POST, list)
				.globalResponseMessage(RequestMethod.DELETE, list)
				.apiInfo(apiInfo()).select()
				.apis(predicate::test)
				.paths(PathSelectors.any())
				.build();

		docket.enable(true);
		return docket;
	}

	private ApiInfo apiInfo() {
		Contact contact = new Contact("ebupt", "http://www.ebupt.com", "dengna@ebupt.com");

		return new ApiInfoBuilder().title("EBUPT 对外API文档").description("本项目使用RESTful设计接口")
				.contact(contact).version("1.0").build();
	}
}
