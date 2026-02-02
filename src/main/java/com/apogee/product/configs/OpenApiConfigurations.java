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

import static com.apogee.product.configs.openAPIConstants.API_DESCRIPTION;
import static com.apogee.product.configs.openAPIConstants.API_TITLE;
import static com.apogee.product.configs.openAPIConstants.API_VERSION;
import static com.apogee.product.configs.openAPIConstants.BEARER_AUTH;
import static com.apogee.product.configs.openAPIConstants.BEARER_FORMAT;
import static com.apogee.product.configs.openAPIConstants.DESC_FAILURE;
import static com.apogee.product.configs.openAPIConstants.DESC_INTERNAL_ERROR;
import static com.apogee.product.configs.openAPIConstants.DESC_NOT_FOUND;
import static com.apogee.product.configs.openAPIConstants.MEDIA_TYPE_JSON;
import static com.apogee.product.configs.openAPIConstants.REF_FAILURE_RESPONSE;
import static com.apogee.product.configs.openAPIConstants.REF_FAILURE_RESPONSE_DTO;
import static com.apogee.product.configs.openAPIConstants.RESPONSE_FAILURE;
import static com.apogee.product.configs.openAPIConstants.RESPONSE_INTERNAL_ERROR;
import static com.apogee.product.configs.openAPIConstants.RESPONSE_NOT_FOUND;
import static com.apogee.product.configs.openAPIConstants.SCHEMA_FAILURE_RESPONSE_DTO;
import static com.apogee.product.configs.openAPIConstants.SCHEME_BEARER;
import static com.apogee.product.configs.openAPIConstants.SERVER_ROOT;

@Configuration
public class OpenApiConfigurations {

    @Bean
    public OpenAPI openApi() {
        Components components = new Components()
                .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(SCHEME_BEARER)
                        .bearerFormat(BEARER_FORMAT))
                .addSchemas(SCHEMA_FAILURE_RESPONSE_DTO, new Schema<>().$ref(REF_FAILURE_RESPONSE_DTO))
                .addResponses(RESPONSE_FAILURE, new ApiResponse()
                        .description(DESC_FAILURE)
                        .content(new Content()
                                .addMediaType(MEDIA_TYPE_JSON,
                                        new MediaType()
                                                .schema(new Schema<>().$ref(REF_FAILURE_RESPONSE_DTO)))));
        components.addResponses(RESPONSE_NOT_FOUND, new ApiResponse()
                .description(DESC_NOT_FOUND)
                .content(new Content().addMediaType(MEDIA_TYPE_JSON,
                        new MediaType().schema(new Schema<>().$ref(REF_FAILURE_RESPONSE)))));

        components.addResponses(RESPONSE_INTERNAL_ERROR, new ApiResponse()
                .description(DESC_INTERNAL_ERROR)
                .content(new Content().addMediaType(MEDIA_TYPE_JSON,
                        new MediaType().schema(new Schema<>().$ref(REF_FAILURE_RESPONSE)))));

        return new OpenAPI()
                .addServersItem(new Server().url(SERVER_ROOT))
                .info(new Info().title(API_TITLE).version(API_VERSION).description(API_DESCRIPTION))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(components);
    }

}
