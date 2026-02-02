package com.apogee.product.aop;

import com.apogee.product.constants.ProductsConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StringMapMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.apogee.product.constants.ProductsConstant.NULL_STRING;
import static com.apogee.product.constants.ProductsConstant.REQUEST_ID;
import static com.apogee.product.utilities.Utilities.formatAsJsonObject;

import org.slf4j.MDC;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    // Intercept all public methods in GlobalExceptionHandler
    @AfterReturning(pointcut = "execution(public * com.apogee.product.advices.GlobalExceptionHandler.*(..))", returning = "response")
    public void logExceptionHandler(JoinPoint joinPoint, Object response) {
        try {
            Object[] args = joinPoint.getArgs();

            Throwable thrown = findThrowableArg(args);
            HttpServletRequest request = findHttpRequestArg(args);

            StringMapMessage mapMessage = new StringMapMessage();
            Date now = new Date();

            String requestId = MDC.get(REQUEST_ID);

            if (request != null) {
                mapMessage.with(ProductsConstant.URL, safeRequestUrl(request));
                mapMessage.with(ProductsConstant.HTTP_METHOD, request.getMethod());
                mapMessage.with(ProductsConstant.QUERY_PARAMS, formatAsJsonObject(extractQueryParams(request)));
                mapMessage.with(ProductsConstant.HEADERS, formatAsJsonObject(extractHeaders(request)));
            } else {
                mapMessage.with(ProductsConstant.URL, NULL_STRING)
                        .with(ProductsConstant.HTTP_METHOD, NULL_STRING)
                        .with(ProductsConstant.QUERY_PARAMS, NULL_STRING)
                        .with(ProductsConstant.HEADERS, NULL_STRING);
            }

            mapMessage.with(ProductsConstant.TIMESTAMP, now.toString())
                    .with(ProductsConstant.REQUEST_ID, requestId != null ? requestId : NULL_STRING)
                    .with(ProductsConstant.RESPONSE_BODY, response != null ? formatAsJsonObject(response) : NULL_STRING);

            if (thrown != null) {
                mapMessage.with(ProductsConstant.EXCEPTION_MESSAGE, thrown.getMessage() != null ? thrown.getMessage() : NULL_STRING)
                        .with(ProductsConstant.EXCEPTION_STACKTRACE, Arrays.toString(thrown.getStackTrace()));
                // log at error level for exceptions
                log.error(mapMessage.asString());
            } else {
                // handlers that return a failure response without throwable
                log.info(mapMessage.getFormattedMessage());
            }
        } catch (Exception e) {
            // Do not let logging failure impact exception handling flow
            log.warn("ExceptionLoggingAspect failed to log exception handler invocation: {}", e.getMessage());
        }
    }

    private Throwable findThrowableArg(Object[] args) {
        if (args == null) return null;
        for (Object a : args) {
            if (a instanceof Throwable thrown) return thrown;
        }
        return null;
    }

    private HttpServletRequest findHttpRequestArg(Object[] args) {
        if (args == null) return null;

        return (HttpServletRequest) Arrays.stream(args).filter(arg -> arg instanceof HttpServletRequest).findFirst().orElse(null);

    }

    private String safeRequestUrl(HttpServletRequest request) {
        try {
            return request.getRequestURL().toString();
        } catch (Exception e) {
            return NULL_STRING;
        }
    }

    private Map<String, String> extractQueryParams(HttpServletRequest request) {
        Map<String, String> queryParams = new HashMap<>();
        try {
            request.getParameterMap().forEach((key, values) -> queryParams.put(key, String.join(",", values)));
        } catch (Exception ignored) {
        }
        return queryParams;
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames != null && headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
        } catch (Exception ignored) {
        }
        return headers;
    }
}
