package com.apogee.product.aop;

import com.apogee.product.constants.ProductsConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StringMapMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.apogee.product.constants.ProductsConstant.NULL_STRING;
import static com.apogee.product.constants.ProductsConstant.REQUEST_ID;
import static com.apogee.product.utilities.Utilities.formatAsJsonObject;

@Slf4j
@Aspect
@Component
public class LoggerAspect {


    @Before("execution(* com.apogee.product.controllers.ProductController.*(..))")
    void logRequestInformation(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logRequestDetails(joinPoint, request, null);

    }

    @AfterReturning(pointcut = "execution(* com.apogee.product.controllers..*(..))", returning = "response")
    public void logResponseDetails(JoinPoint joinPoint, Object response) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logRequestDetails(joinPoint, request, response);

    }

    @AfterThrowing(pointcut = "execution(* com.apogee.product.controllers..*(..))", throwing = "exception")
    public void logExceptionDetails(JoinPoint joinPoint, Throwable exception, HttpServletRequest request) {

        logExceptionDetails(joinPoint, request, exception);
    }

    private void logExceptionDetails(JoinPoint joinPoint, HttpServletRequest request, Throwable exception) {

        // Use StringMapMessage which implements MapMessage<String,String> to avoid ClassCastException
        StringMapMessage mapMessage = new StringMapMessage();
        Date now = new Date();
        String requestBody = getRequestBody(joinPoint);
        String requestId = MDC.get(REQUEST_ID);

        mapMessage.with(ProductsConstant.URL, request.getRequestURL().toString()).
                with(ProductsConstant.HTTP_METHOD, request.getMethod())
                .with(ProductsConstant.QUERY_PARAMS, formatAsJsonObject(getQueryParams(request)))
                .with(ProductsConstant.REQUEST_BODY, requestBody != null ? requestBody : NULL_STRING)
                .with(ProductsConstant.TIMESTAMP, now.toString())
                .with(ProductsConstant.PATH_VARIABLES, getPathVariables(joinPoint))
                .with(ProductsConstant.HEADERS, formatAsJsonObject(getHeaders(request)))
                .with(ProductsConstant.REQUEST_ID, requestId != null ? requestId : NULL_STRING)
                .with(ProductsConstant.EXCEPTION_MESSAGE, exception.getMessage() != null ? exception.getMessage() : NULL_STRING)
                .with(ProductsConstant.EXCEPTION_STACKTRACE, Arrays.toString(exception.getStackTrace()));

        log.error(mapMessage.asString());
    }

    private void logRequestDetails(JoinPoint joinPoint, HttpServletRequest request, Object response) {

        // Use StringMapMessage consistently
        StringMapMessage mapMessage = new StringMapMessage();
        Date now = new Date();
        String requestBody = getRequestBody(joinPoint);
        String requestId = MDC.get(REQUEST_ID);

        mapMessage.with(ProductsConstant.URL, request.getRequestURL().toString()).
                with(ProductsConstant.HTTP_METHOD, request.getMethod())
                .with(ProductsConstant.QUERY_PARAMS, formatAsJsonObject(getQueryParams(request)))
                .with(ProductsConstant.REQUEST_BODY, requestBody != null ? requestBody : NULL_STRING)
                .with(ProductsConstant.TIMESTAMP, now.toString())
                .with(ProductsConstant.PATH_VARIABLES, getPathVariables(joinPoint))
                .with(ProductsConstant.HEADERS, formatAsJsonObject(getHeaders(request)))
                .with(ProductsConstant.REQUEST_ID, requestId != null ? requestId : NULL_STRING)
                .with(ProductsConstant.RESPONSE_BODY, response != null ? formatAsJsonObject(response) : NULL_STRING);

        log.debug(mapMessage.asString());
    }


    private String getPathVariables(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Map<String, Object> pathVariables = new HashMap<>();

        // Get method parameters and annotations
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof PathVariable annotationPathVariable) {
                    String pathVariableName = annotationPathVariable.value();
                    // Use parameter name if @PathVariable name is empty
                    if (pathVariableName.isBlank()) {
                        pathVariableName = parameterNames[i];
                    }
                    pathVariables.put(pathVariableName, args[i]);
                }
            }
        }

        return formatAsJsonObject(pathVariables);
    }

    private Map<String, String> getQueryParams(HttpServletRequest request) {

        Map<String, String> queryParams = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> queryParams.put(key, String.join(",", values)));

        return queryParams;
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }

    private String getRequestBody(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : getParameterizedAnnotations(joinPoint)[i]) {
                if (annotation instanceof RequestBody) {
                    return formatAsJsonObject(args[i]);
                }
            }
        }

        return null;
    }

    private Annotation[][] getParameterizedAnnotations(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        return method.getParameterAnnotations();
    }
}
