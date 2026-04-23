package com.apogee.product.aop;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AOPUtilities {

    private AOPUtilities() {
        // Private constructor to prevent instantiation
    }

    public static String formatAsJsonObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Map<String, Object> getPathVariables(HttpServletRequest request) {

        Map<String, Object> pathVariables = new HashMap<>();

        if (request == null) {
            return pathVariables;
        }

        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (attribute instanceof Map<?, ?> variables) {
            variables.forEach((key, value) ->
                    pathVariables.put(String.valueOf(key), value)
            );
        }

        return pathVariables;
    }

    public static Map<String, Object> getQueryParams(HttpServletRequest request) {

        Map<String, Object> queryParams = new HashMap<>();

        if (request == null || request.getParameterMap() == null) {
            return queryParams;
        }

        request.getParameterMap().forEach((key, values) -> {
            if (values == null) {
                queryParams.put(key, null);
            } else if (values.length == 1) {
                queryParams.put(key, values[0]);
            } else {
                queryParams.put(key, values); // multi-value support
            }
        });

        return queryParams;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }
}
