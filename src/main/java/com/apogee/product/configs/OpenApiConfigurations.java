package com.apogee.product.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfigurations {

    @Bean
    public OpenAPI openApi() {
        Components components = new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSchemas("FailureResponseDto", new Schema<>().$ref("#/components/schemas/FailureResponseDto"))
                .addResponses("FailureResponse", new ApiResponse()
                        .description("Failure response")
                        .content(new Content()
                                .addMediaType("application/json",
                                        new MediaType()
                                                .schema(new Schema<>().$ref("#/components/schemas/FailureResponseDto")))));
        components.addResponses("NotFoundResponse", new ApiResponse()
                .description("Resource not found")
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/FailureResponse")))));

        components.addResponses("InternalErrorResponse", new ApiResponse()
                .description("Internal server error")
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/FailureResponse")))));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(new Info().title("Apogee Product API").version("1.0.0").description("API for managing products in the Apogee system."))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(components);
    }
}
