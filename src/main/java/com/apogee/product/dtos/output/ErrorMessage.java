package com.apogee.product.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorMessage{

    private String message;
    private String messageAr;

    public ErrorMessage(String message, String messageAr) {
        this.message = message;
        this.messageAr = messageAr;
    }

    public ErrorMessage() {
        this.message = "An error occurred";
        this.messageAr = "\\u062d\\u062f\\u062b\\u0020\\u062e\\u0637\\u0623\\u0020\\u0645\\u0627";
    }
}
