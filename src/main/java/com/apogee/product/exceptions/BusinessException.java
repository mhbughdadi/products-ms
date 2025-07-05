package com.apogee.product.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorCode;
    private String errorDescription;
    private String errorDescriptionAr;


    public BusinessException(String message, String errorCode, String errorDescription, String errorDescriptionAr, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        ;
        this.errorDescription = errorDescription;
        this.errorDescriptionAr = errorDescriptionAr;
    }

    public BusinessException(String message, String errorCode) {
        this(message, errorCode, null, null, null);

    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
