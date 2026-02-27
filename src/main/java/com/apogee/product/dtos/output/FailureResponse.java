package com.apogee.product.dtos.output;

import com.apogee.product.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class FailureResponse extends Response {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int FAILURE_CODE = -1;

    public FailureResponse() {
        super(FAILURE_CODE, Status.FAILURE);
    }

    public FailureResponse(String errorDescription, String errorDescriptionAr){
        this();
        this.setError(new ErrorMessage(errorDescription, errorDescriptionAr));
    }
}
