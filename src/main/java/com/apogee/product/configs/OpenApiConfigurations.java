package com.apogee.product.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfigurations {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Apogee Product API")
                        .version("1.0.0")
                        .description("API for managing products in the Apogee system."));
    }
}
