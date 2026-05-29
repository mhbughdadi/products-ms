package com.apogee.product.dtos.output;

import com.apogee.spring.common.dto.Response;
import com.apogee.spring.common.dto.Status;

import java.io.Serial;

public class SuccessfulResponse extends Response {

    @Serial
    private static final long serialVersionUID = 1L;
    public SuccessfulResponse() {
        super(0, Status.SUCCESS);
    }
}
