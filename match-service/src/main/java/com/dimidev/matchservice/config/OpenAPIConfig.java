package com.dimidev.matchservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI matchServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Match Service API")
                        .description("This is REST API for Match Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer to the Match Service Wiki Documentation")
                        .url("https://match-service-dummy-url.com/docs"));
    }
}
