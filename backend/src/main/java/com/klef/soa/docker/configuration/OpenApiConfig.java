package com.klef.soa.docker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Waste Collection Management API")
                        .version("1.0.0")
                        .description("REST API for Waste Collection operations (Docker Exam Task - Student ID: 2400032862)")
                        .contact(new Contact()
                                .name("Student 2400032862")
                                .email("shashirpremdandyala@gmail.com")));
    }
}
