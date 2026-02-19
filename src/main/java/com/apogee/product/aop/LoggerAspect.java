package com.apogee.product.aop;

import com.apogee.product.constants.ProductsConstant;
import com.apogee.product.utilities.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.StringMapMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

import static com.apogee.product.constants.ProductsConstant.NULL_STRING;
import static com.apogee.product.constants.ProductsConstant.REQUEST_ID;
import static com.apogee.product.utilities.Utilities.formatAsJsonObject;

@Log4j2
@Aspect
@Component
public class LoggerAspect {

    @Around("within(com.apogee.product.controllers..*)")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes())
                        .getRequest();

        String requestId = MDC.get(REQUEST_ID);
        String requestBody = getRequestBody(joinPoint);

        StringMapMessage message = new StringMapMessage()
                .with(ProductsConstant.URL, request.getRequestURL().toString())
                .with(ProductsConstant.HTTP_METHOD, request.getMethod())
                .with(ProductsConstant.QUERY_PARAMS, formatAsJsonObject(Utilities.getQueryParams(request)))
                .with(ProductsConstant.REQUEST_BODY, requestBody != null ? requestBody : NULL_STRING)
                .with(ProductsConstant.TIMESTAMP, Instant.now().toString())
                .with(ProductsConstant.PATH_VARIABLES, Utilities.getPathVariables(request))
                .with(ProductsConstant.HEADERS, formatAsJsonObject(Utilities.getHeaders(request)))
                .with(ProductsConstant.REQUEST_ID, requestId != null ? requestId : NULL_STRING);

        Object response = null;
        try {

            response = joinPoint.proceed();

            message
                    .with(ProductsConstant.STATUS, String.valueOf(HttpStatus.OK.value()))
                    .with(ProductsConstant.DURATION_MS, String.valueOf(System.currentTimeMillis() - start))
                    .with(ProductsConstant.RESPONSE_BODY,
                            response != null ? formatAsJsonObject(response) : NULL_STRING);

            log.info(message);

            return response;

        } catch (Throwable ex) {

            message
                    .with(ProductsConstant.STATUS, String.valueOf(HttpStatus.OK.value()))
                    .with(ProductsConstant.DURATION_MS, String.valueOf(System.currentTimeMillis() - start))
                    .with(ProductsConstant.EXCEPTION_MESSAGE,
                            ex.getMessage() != null ? ex.getMessage() : NULL_STRING);

            log.error(message, ex);
            throw ex;
        }
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
