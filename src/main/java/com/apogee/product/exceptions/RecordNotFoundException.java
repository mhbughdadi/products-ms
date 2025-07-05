package com.apogee.product.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Long recordId;


    public RecordNotFoundException(String message, Long recordId) {
        super(message, null);
        this.recordId = recordId;
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordNotFoundException(Throwable cause) {
        super(cause);
    }
}
