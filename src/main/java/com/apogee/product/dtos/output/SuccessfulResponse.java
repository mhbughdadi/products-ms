package com.apogee.product.dtos.output;

import com.apogee.product.enums.Status;

public class SuccessfulResponse extends Response {

    public SuccessfulResponse() {
        super(0, Status.SUCCESS);
    }
}
