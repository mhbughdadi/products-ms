package com.apogee.product.enums;

public enum ProductStatus {

    ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), DRAFT("DRAFT"), DISCONTINUED("DISCONTINUED");

    private final String code;

    ProductStatus(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

}
