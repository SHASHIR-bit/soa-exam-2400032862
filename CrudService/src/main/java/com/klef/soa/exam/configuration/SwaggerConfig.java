package com.klef.soa.exam.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WasteCollection CRUD Service API")
                        .version("1.0")
                        .description("REST API for WasteCollection CRUD Operations")
                        .contact(new Contact()
                                .name("DANDYALA SHASHIR PREM")
                                .email("2400032862@kluniversity.in")));
    }
}
