package com.myown.app.sample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiSwaggerConfiguration {

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(new Info().title("Java Rest Eks API").description("Sample java rest eks application")
				.version("v0.0.1"));
	}
}
