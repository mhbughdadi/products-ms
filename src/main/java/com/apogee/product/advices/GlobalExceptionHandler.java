package com.apogee.product.advices;

import com.apogee.product.dtos.output.FailureResponse;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

@ControllerAdvice
@Slf4j
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

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getMessageFromBundle(String key, Locale locale, Object... args) {

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("errors", locale);
            return MessageFormat.format(bundle.getString(key), args);
        } catch (Exception e) {
            return key;
        }
    }

    //implement DBException handler here
    @ExceptionHandler(DBException.class)
    public ResponseEntity<FailureResponse> handleDBException(DBException ex) {
        FailureResponse errorResponse = new FailureResponse(
                this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH, toObjectArray(ex.getRecordIds())),
                this.getMessageFromBundle(ex.getMessage(), Locale.forLanguageTag("ar"), toObjectArray(ex.getRecordIds()))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MapperException.class})
    public ResponseEntity<FailureResponse> handleMapperException(MapperException ex, HttpServletRequest request) {

        FailureResponse errorResponse = new FailureResponse(
                this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH),
                this.getMessageFromBundle(ex.getMessage(), Locale.forLanguageTag("ar"))
        );
        errorResponse.setRequestId(request.getHeader("X-Request-ID"));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Converts a Long[] to Object[] for varargs compatibility.
     */
    private static Object[] toObjectArray(Long[] longs) {
        if (longs == null) return new Object[0];
        return Arrays.copyOf(longs, longs.length, Object[].class);
    }

}
