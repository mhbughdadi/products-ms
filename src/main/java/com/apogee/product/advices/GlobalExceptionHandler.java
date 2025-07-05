package com.apogee.product.advices;

import com.apogee.product.dtos.output.FailureResponse;
import com.apogee.product.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<FailureResponse> handleResourceNotFound(RecordNotFoundException ex) {

        FailureResponse errorResponse = new FailureResponse(
                this.getMessageFromBundle(ex.getMessage(), Locale.ENGLISH, ex.getRecordId().toString()),
                this.getMessageFromBundle(ex.getMessage(), Locale.of("ar"), ex.getRecordId().toString()));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private String getMessageFromBundle(String key, Locale locale, Object... args) {

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("errors", locale);
            return MessageFormat.format(bundle.getString(key),args);
        } catch (Exception e) {
            return key;
        }
    }
}
