package com.apogee.product.utilities;

import com.apogee.product.exceptions.MapperException;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.function.ThrowingBiFunction;
import org.springframework.util.function.ThrowingFunction;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Utilities {

    private Utilities() {
    }

    /**
     * Transform a collection of source objects to a list of destination objects using the provided mapping function.
     *
     * @param sourceCollection the collection of source objects to be transformed
     * @param mappingFunction  a function that maps a source object to a destination object
     * @param <S>              the type of the source objects
     * @param <R>              the type of the destination objects
     * @return a list of transformed destination objects
     * @throws MapperException if any error occurs during the mapping process
     */
    public static <S, R> List<R> transformCollection(Collection<S> sourceCollection, ThrowingFunction<S, R> mappingFunction) throws MapperException {

        if (sourceCollection != null && !sourceCollection.isEmpty()) {
            return sourceCollection.stream().map(element -> {
                try {
                    return mappingFunction.apply(element);
                } catch (Exception e) {
                    throw new MapperException(e.getMessage(), e);
                }
            }).toList();
        }
        return new ArrayList<>();
    }

    /**
     * Transform a collection of source objects to a list of destination objects using the Mapper utility.
     *
     * @param sourceCollection the collection of source objects to be transformed
     * @param destinationClass the class of the destination objects
     * @param <S>              the type of the source objects
     * @param <R>              the type of the destination objects
     * @return a list of transformed destination objects
     * @throws MapperException if any error occurs during the mapping process
     */
    public static <S, R> List<R> transformCollection(Collection<S> sourceCollection, Class<R> destinationClass) throws MapperException {

        if (sourceCollection != null && !sourceCollection.isEmpty()) {
            return sourceCollection.stream().map(element -> {
                try {
                    return Mapper.map(element, destinationClass);
                } catch (Exception e) {
                    throw new MapperException(e.getMessage(), e);
                }
            }).toList();
        }
        return new ArrayList<>();
    }


    /**
     * Transform a collection of source objects to a list of destination objects using the Mapper utility,
     * and apply a complementary function to each mapped object.
     *
     * @param sourceCollection      the collection of source objects to be transformed
     * @param destinationClass      the class of the destination objects
     * @param complementaryFunction a function that takes a source object and its corresponding mapped destination object,
     * @param <S>                   the type of the source objects
     * @param <R>                   the type of the destination objects
     * @return a list of transformed destination objects after applying the complementary function
     * @throws MapperException if any error occurs during the mapping or function application process
     */
    public static <S, R> List<R> transformCollection(Collection<S> sourceCollection, Class<R> destinationClass, ThrowingBiFunction<S, R, R> complementaryFunction) throws MapperException {

        if (sourceCollection != null && !sourceCollection.isEmpty()) {
            return sourceCollection.stream().map(element -> {
                try {
                    R mapped = Mapper.map(element, destinationClass);

                    return complementaryFunction != null ? complementaryFunction.apply(element, mapped) : mapped;
                } catch (Exception e) {
                    throw new MapperException(e.getMessage(), e);
                }
            }).toList();
        }
        return new ArrayList<>();
    }

    /**
     * Transform a collection of source objects to a list of destination objects using the provided mapping function,
     *
     * @param sourceCollection      the collection of source objects to be transformed
     * @param mappingFunction       a function that maps a source object to a destination object
     * @param complementaryFunction a function that takes a source object and its corresponding mapped destination object,
     * @param <S>                   the type of the source objects
     * @param <R>                   the type of the destination objects
     * @return a list of transformed destination objects after applying the complementary function
     * @throws MapperException if any error occurs during the mapping or function application process
     */
    @Deprecated(since = "22/2/2026", forRemoval = true)
    public static <S, R> List<R> transformCollection(Collection<S> sourceCollection, ThrowingFunction<S, R> mappingFunction, ThrowingBiFunction<S, R, R> complementaryFunction) throws MapperException {

        List<R> destinationCollection = transformCollection(sourceCollection, mappingFunction);

        if (!destinationCollection.isEmpty() && complementaryFunction != null) {
            List<R> result = new ArrayList<>();
            int i = 0;
            for (S sourceElement : sourceCollection) {
                R destinationElement = destinationCollection.get(i);
                try {
                    result.add(complementaryFunction.apply(sourceElement, destinationElement));
                } catch (Exception e) {
                    throw new MapperException(e.getMessage(), e);
                }
                i++;
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public static <S, R> R transform(S sourceObject, Class<R> destinationClass, ThrowingBiFunction<S, R, R> complementaryFunction) throws MapperException {

        if (sourceObject != null) {
            try {
                R mapped = Mapper.map(sourceObject, destinationClass);

                return complementaryFunction != null ? complementaryFunction.apply(sourceObject, mapped) : mapped;
            } catch (Exception e) {
                throw new MapperException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static <S, R> R transform(S sourceObject, Class<R> destinationClass) throws MapperException {

        if (sourceObject != null) {
            try {

                return Mapper.map(sourceObject, destinationClass);
            } catch (Exception e) {
                throw new MapperException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static String formatAsJsonObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Map<String, Object> getPathVariables(HttpServletRequest request) {

        Map<String, Object> pathVariables = new HashMap<>();

        if (request == null) {
            return pathVariables;
        }

        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (attribute instanceof Map<?, ?> variables) {
            variables.forEach((key, value) ->
                    pathVariables.put(String.valueOf(key), value)
            );
        }

        return pathVariables;
    }

    public static Map<String, Object> getQueryParams(HttpServletRequest request) {

        Map<String, Object> queryParams = new HashMap<>();

        if (request == null || request.getParameterMap() == null) {
            return queryParams;
        }

        request.getParameterMap().forEach((key, values) -> {
            if (values == null) {
                queryParams.put(key, null);
            } else if (values.length == 1) {
                queryParams.put(key, values[0]);
            } else {
                queryParams.put(key, values); // multi-value support
            }
        });

        return queryParams;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }
}
