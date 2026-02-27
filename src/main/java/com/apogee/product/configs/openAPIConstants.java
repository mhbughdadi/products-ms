package com.apogee.product.configs;

public final class OpenApiConstants {

    private OpenApiConstants() {
    }

    public static final String BEARER_AUTH = "bearerAuth";

    public static final String SCHEME_BEARER = "bearer";
    public static final String BEARER_FORMAT = "JWT";

    public static final String SCHEMA_FAILURE_RESPONSE_DTO = "FailureResponseDto";
    public static final String SCHEMA_FAILURE_RESPONSE = "FailureResponse";

    public static final String REF_FAILURE_RESPONSE_DTO = "#/components/schemas/" + SCHEMA_FAILURE_RESPONSE_DTO;
    public static final String REF_FAILURE_RESPONSE = "#/components/schemas/" + SCHEMA_FAILURE_RESPONSE;

    public static final String RESPONSE_FAILURE = "FailureResponse";
    public static final String RESPONSE_NOT_FOUND = "NotFoundResponse";
    public static final String RESPONSE_INTERNAL_ERROR = "InternalErrorResponse";

    public static final String MEDIA_TYPE_JSON = "application/json";
    public static final String SERVER_ROOT = "/";

    public static final String DESC_FAILURE = "Failure response";
    public static final String DESC_NOT_FOUND = "Resource not found";
    public static final String DESC_INTERNAL_ERROR = "Internal server error";

    public static final String API_TITLE = "Apogee Product API";
    public static final String API_VERSION = "1.0.0";
    public static final String API_DESCRIPTION = "API for managing products in the Apogee system.";
}
