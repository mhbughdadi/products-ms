package com.apogee.product.utilities;

import com.google.gson.Gson;
import org.springframework.util.function.ThrowingFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utilities {

    public static <S, R> List<R> transformCollection(Collection<S> sourceCollection, ThrowingFunction<S, R> mappingFunction) throws Exception {

        if (sourceCollection != null && !sourceCollection.isEmpty()) {
            return sourceCollection.stream().map(element -> {
                try {
                    return mappingFunction.apply(element);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }
        return new ArrayList<>();
    }

    public static String formatAsJsonObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
