package com.apogee.product.enums;

public enum TagType {
    CATEGORY("CATEGORY"), SKU("SKU"), PRODUCT("PRODUCT");

    private final String value;

    TagType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
