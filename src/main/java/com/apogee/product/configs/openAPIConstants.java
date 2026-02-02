package com.apogee.product.configs;

public interface openAPIConstants {
    public static final String BEARER_AUTH = "bearerAuth";

    String SCHEME_BEARER = "bearer";
    String BEARER_FORMAT = "JWT";

    String SCHEMA_FAILURE_RESPONSE_DTO = "FailureResponseDto";
    String SCHEMA_FAILURE_RESPONSE = "FailureResponse";

    String REF_FAILURE_RESPONSE_DTO = "#/components/schemas/" + SCHEMA_FAILURE_RESPONSE_DTO;
    String REF_FAILURE_RESPONSE = "#/components/schemas/" + SCHEMA_FAILURE_RESPONSE;

    String RESPONSE_FAILURE = "FailureResponse";
    String RESPONSE_NOT_FOUND = "NotFoundResponse";
    String RESPONSE_INTERNAL_ERROR = "InternalErrorResponse";

    String MEDIA_TYPE_JSON = "application/json";
    String SERVER_ROOT = "/";

    String DESC_FAILURE = "Failure response";
    String DESC_NOT_FOUND = "Resource not found";
    String DESC_INTERNAL_ERROR = "Internal server error";

    String API_TITLE = "Apogee Product API";
    String API_VERSION = "1.0.0";
    String API_DESCRIPTION = "API for managing products in the Apogee system.";
}
