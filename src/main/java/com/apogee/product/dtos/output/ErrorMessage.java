package com.apogee.product.dtos.output;

import org.springframework.http.HttpStatus;

public record ErrorMessage(HttpStatus httpStatus, String errorCode, String errorDescriptionEn, String errorDescriptionAr
) {
}
