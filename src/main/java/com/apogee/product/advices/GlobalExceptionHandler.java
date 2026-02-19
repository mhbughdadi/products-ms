package com.apogee.product.advices;

import com.apogee.product.constants.ProductsConstant;
import com.apogee.product.dtos.output.FailureResponse;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.utilities.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.message.StringMapMessage;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.apogee.product.constants.ProductsConstant.NULL_STRING;
import static com.apogee.product.constants.ProductsConstant.REQUEST_ID;
import static com.apogee.product.utilities.Utilities.formatAsJsonObject;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<FailureResponse> handleResourceNotFound(RecordNotFoundException ex) {

        FailureResponse errorResponse = new FailureResponse();
        if (ex.getRecordId() != null) {
            errorResponse = new FailureResponse(
                    this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH, ex.getRecordId().toString()),
                    this.getMessageFromBundle(ex.getMessage(), Locale.of("ar"), ex.getRecordId().toString()));

        }
        if (ex.getRecordIds() != null && ex.getRecordIds().length > 0) {
            errorResponse = new FailureResponse(
                    this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH, toObjectArray(ex.getRecordIds())),
                    this.getMessageFromBundle(ex.getMessage(), Locale.forLanguageTag("ar"), toObjectArray(ex.getRecordIds())));
        }

        logErrorResponse(errorResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DBException.class)
    public ResponseEntity<FailureResponse> handleDBException(DBException ex) {

        FailureResponse errorResponse = new FailureResponse(
                this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH, toObjectArray(ex.getRecordIds())),
                this.getMessageFromBundle(ex.getMessage(), Locale.forLanguageTag("ar"), toObjectArray(ex.getRecordIds()))
        );

        logErrorResponse(errorResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MapperException.class})
    public ResponseEntity<FailureResponse> handleMapperException(MapperException ex, HttpServletRequest request) {

        FailureResponse errorResponse = new FailureResponse(
                this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH),
                this.getMessageFromBundle(ex.getMessage(), Locale.forLanguageTag("ar"))
        );

        logErrorResponse(errorResponse, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Converts a Long[] to Object[] for varargs compatibility.
     */
    private static Object[] toObjectArray(Long[] longs) {
        if (longs == null) return new Object[0];
        return Arrays.copyOf(longs, longs.length, Object[].class);
    }

    private static void logErrorResponse(FailureResponse errorResponse, HttpStatus status) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes())
                        .getRequest();

        String requestId = MDC.get(REQUEST_ID);

        StringMapMessage message = new StringMapMessage()
                .with(ProductsConstant.URL, request.getRequestURL().toString())
                .with(ProductsConstant.HTTP_METHOD, request.getMethod())
                .with(ProductsConstant.QUERY_PARAMS, formatAsJsonObject(Utilities.getQueryParams(request)))
                .with(ProductsConstant.TIMESTAMP, Instant.now().toString())
                .with(ProductsConstant.PATH_VARIABLES, Utilities.getPathVariables(request))
                .with(ProductsConstant.HEADERS, formatAsJsonObject(Utilities.getHeaders(request)))
                .with(ProductsConstant.RESPONSE_BODY, formatAsJsonObject(errorResponse))
                .with(ProductsConstant.STATUS, status.toString())
                .with(ProductsConstant.REQUEST_ID, requestId != null ? requestId : NULL_STRING);

        log.error(message);
    }

    private String getMessageFromBundle(String key, Locale locale, Object... args) {

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("errors", locale);
            return MessageFormat.format(bundle.getString(key), args);
        } catch (Exception e) {
            return key;
        }
    }

}
