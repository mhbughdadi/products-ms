package com.apogee.product.mappings;


public interface Mapper {

    <S, D> D map(S source, Class<D> destination) throws Exception;
}
