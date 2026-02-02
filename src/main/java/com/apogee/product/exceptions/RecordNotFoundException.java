package com.apogee.product.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;

@Getter
@Setter
public class RecordNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long recordId;
    private Long[] recordIds;


    public RecordNotFoundException(String message, Long recordId) {
        super(message, null);
        this.recordId = recordId;
    }

    public RecordNotFoundException(String message, Long... recordIds) {
        super(message, null);
        this.recordIds = recordIds;
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordNotFoundException(Throwable cause) {
        super(cause);
    }
}
