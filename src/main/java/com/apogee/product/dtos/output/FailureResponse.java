package com.apogee.product.dtos.output;

import com.apogee.product.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FailureResponse extends Response {

    private final static int FAILURE_CODE = -1;

    private String errorDescription;
    private String errorDescriptionAr;

    public FailureResponse() {
        super(FAILURE_CODE, Status.FAILURE);
    }

    public FailureResponse(String errorDescription, String errorDescriptionAr){
        this();
        this.errorDescription = errorDescription;
        this.errorDescriptionAr = errorDescriptionAr;
    }
}
