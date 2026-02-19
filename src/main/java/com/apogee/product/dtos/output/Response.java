package com.apogee.product.dtos.output;

import com.apogee.product.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static com.apogee.product.constants.ProductsConstant.REQUEST_ID;

@Getter
@Setter
public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 100L;

    private int code;
    private ErrorMessage error;
    private Date timeStamp = new Date();
    private Status status;
    private String requestId = MDC.get(REQUEST_ID);

    public Response(int code, Status status) {
        this.code = code;
        this.status = status;
        this.timeStamp = getTimeStamp();
    }
}
