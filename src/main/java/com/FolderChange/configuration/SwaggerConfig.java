package com.FolderChange.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("FolderChange").apiInfo(apiInfo())
				.select()
//				.apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.FolderChange.controller"))
			      .paths(PathSelectors.any())    
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Folder Change")
				.description("Project that trigger event functions when the folder changes").build();
				
	}
}
