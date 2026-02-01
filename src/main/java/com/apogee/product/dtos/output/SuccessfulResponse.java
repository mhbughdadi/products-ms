package com.apogee.product.dtos.output;

import com.apogee.product.enums.Status;

import java.io.Serial;

public class SuccessfulResponse extends Response {

    @Serial
    private static final long serialVersionUID = 1L;
    public SuccessfulResponse() {
        super(0, Status.SUCCESS);
    }
}
