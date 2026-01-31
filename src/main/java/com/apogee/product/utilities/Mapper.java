package com.apogee.product.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class Mapper {

    private static final Set<Class<?>> SIMPLE_TYPES = Set.of(
            String.class, UUID.class, BigDecimal.class, BigInteger.class
    );
    private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Enum<?>[]> ENUM_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Field>> DESTINATION_FIELD_MAP_CACHE = new ConcurrentHashMap<>();


    public static <S, D> D map(S source, Class<D> destinationClass) throws Exception {

        if (source == null) {
            return null;
        }

        if (source.getClass().isEnum()) {
            return mapEnum(source, destinationClass);
        }

        D destinationObj = getNewDestinationInstance(destinationClass);

        Field[] sourceFields = FIELD_CACHE.computeIfAbsent(source.getClass(), Class::getDeclaredFields);
        Field[] destinationFields = FIELD_CACHE.computeIfAbsent(destinationClass, Class::getDeclaredFields);

        Map<String, Field> destinationFieldMap = getDestinationFieldMap(destinationClass);

        // iterating on source field list.
        for (Field sourceField : sourceFields) {

            sourceField.setAccessible(true);
            Object sourceValue = sourceField.get(source);

            Field destinationField = destinationFieldMap.get(sourceField.getName());
            if (null == destinationField) {
                continue;
            }

            if (isSimpleField(sourceField.getType())) {

                destinationField.set(destinationObj, sourceValue);
            } else if (Collection.class.isAssignableFrom(sourceField.getType())) {

                destinationField.set(destinationObj, mapCollection(sourceValue, destinationField));
            } else if (Map.class.isAssignableFrom(sourceField.getType())) {

                destinationField.set(destinationObj, mapMap(sourceValue, destinationField));
            } else {

                destinationField.set(destinationObj, map(sourceValue, destinationField.getType()));
            }
        }

        return destinationObj;
    }

    private static <S, D> D mapEnum(S source, Class<D> destinationClass) {

        Enum<?> sourceEnum = (Enum<?>) source;

        if (destinationClass.isEnum()) {

            Enum<?>[] destinationConstants = ENUM_CACHE.computeIfAbsent(destinationClass, aClass -> (Enum<?>[]) aClass.getEnumConstants());

            for (Enum<?> constant : destinationConstants) {
                if (constant.name().equals(sourceEnum.name())) {
                    return (D) constant;
                }
            }

            throw new IllegalArgumentException(
                    "No matching enum constant for " + sourceEnum.name() +
                            " in " + destinationClass.getName()
            );
        } else if (destinationClass == String.class) {
            // Allow Enum -> String
            return (D) sourceEnum.name();
        } else if (destinationClass == int.class || destinationClass == Integer.class) {
            // Allow Enum -> ordinal
            return (D) Integer.valueOf(sourceEnum.ordinal());
        } else {
            throw new IllegalArgumentException(
                    "Cannot map enum type " + source.getClass().getName() +
                            " to non-enum type " + destinationClass.getName()
            );
        }
    }

    private static Collection<Object> mapCollection(Object sourceValue, Field destinationField) throws Exception {

        if (sourceValue == null) {
            return null;
        }

        Collection<?> sourceValueCollection = (Collection<?>) sourceValue;
        Collection<Object> destinationCollection = createCollectionInstance(destinationField.getType());

        Class<?> genericType = getGenericType(destinationField);

        for (Object item : sourceValueCollection) {

            if (item == null) {
                destinationCollection.add(null);
            } else if (isSimpleField(item.getClass())) {
                destinationCollection.add(item);
            } else {
                destinationCollection.add(map(item, genericType));
            }
        }

        return destinationCollection;

    }

    private static <D> D getNewDestinationInstance(Class<D> destinationClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        // getting reference to the destination class constructor from cache or creating a new one.
        @SuppressWarnings("unchecked")
        Constructor<D> constructor = (Constructor<D>) CONSTRUCTOR_CACHE.computeIfAbsent(destinationClass, cls -> {
            try {
                @SuppressWarnings("unchecked")
                Constructor<D> construct = (Constructor<D>) cls.getDeclaredConstructor();
                construct.setAccessible(true);

                return construct;
            } catch (Exception e) {
                throw new RuntimeException("No default constructor found for class: " + cls.getName(), e);
            }
        });

        // creating an instance from the destination class constructor.
        return constructor.newInstance();
    }

    private static Map<String, Field> getDestinationFieldMap(Class<?> destinationClass) {
        return DESTINATION_FIELD_MAP_CACHE.computeIfAbsent(destinationClass, cls -> {
            Field[] fields = FIELD_CACHE.computeIfAbsent(cls, Class::getDeclaredFields);
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field);
            }
            return fieldMap;
        });
    }

    private static Object mapMap(Object sourceValue, Field destinationField) throws Exception {
        if (sourceValue == null) {
            return null;
        }

        Map<?, ?> sourceMap = (Map<?, ?>) sourceValue;
        Map<Object, Object> destinationMap = createMapInstance(destinationField.getType());

        Class<?>[] genericTypes = getGenericTypes(destinationField);

        for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object mappedKey = (isSimpleField(key.getClass())) ? key : map(key, genericTypes[0]);
            Object mappedValue = (isSimpleField(value.getClass())) ? value : map(value, genericTypes[1]);

            destinationMap.put(mappedKey, mappedValue);
        }

        return destinationMap;
    }

    private static Class<?>[] getGenericTypes(Field targetField) throws ClassNotFoundException {

        Type type = targetField.getGenericType();

        if (type instanceof ParameterizedType parameterizedType) {

            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            Class<?> keyType = Class.forName(actualTypes[0].getTypeName());
            Class<?> valueType = Class.forName(actualTypes[1].getTypeName());
            return new Class<?>[]{keyType, valueType};
        }
        return new Class<?>[]{Object.class, Object.class};
    }

    private static Class<?> getGenericType(Field targetField) throws ClassNotFoundException {

        Type type = targetField.getGenericType();

        if (type instanceof ParameterizedType parameterizedType) {

            Type actual = parameterizedType.getActualTypeArguments()[0];

            return Class.forName(actual.getTypeName());
        }

        return Object.class;
    }


    private static Collection<Object> createCollectionInstance(Class<?> type) {

        if (List.class.isAssignableFrom(type)) {

            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(type)) {

            return new HashSet<>();
        } else {

            throw new RuntimeException("Not Supported Collection Type: " + type.getName());
        }
    }

    private static Map<Object, Object> createMapInstance(Class<?> type) {

        if (Map.class.isAssignableFrom(type)) {
            return new HashMap<>();
        } else {
            throw new RuntimeException("Not Supported Map Type: " + type.getName());
        }
    }


    private static boolean isSimpleField(Class<?> sourceClass) {

        return sourceClass.isPrimitive()
                || SIMPLE_TYPES.contains(sourceClass)
                || Number.class.isAssignableFrom(sourceClass)
                || Character.class.isAssignableFrom(sourceClass)
                || Boolean.class.isAssignableFrom(sourceClass)
                || Date.class.isAssignableFrom(sourceClass)
                || java.time.temporal.Temporal.class.isAssignableFrom(sourceClass);
    }

}
