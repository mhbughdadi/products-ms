package com.apogee.product.mappings;

import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MapperImpl implements Mapper {

    @Override
    public <S, D> D map(S source, Class<D> destinationClass) throws Exception {

        if (source == null) {
            return null;
        }
        // getting reference to the destination class constructor
        Constructor<D> constructor = destinationClass.getDeclaredConstructor();

        // creating an instance from the destination class
        D destinationObj = constructor.newInstance();

        // fetching all declared fields in source object.
        Field[] sourceFields = source.getClass().getDeclaredFields();

        // iterating on source field list.
        for (Field sourceField : sourceFields) {

            sourceField.setAccessible(true);
            Object sourceValue = sourceField.get(source);

            try {

                Field destinationField = destinationObj.getClass().getDeclaredField(sourceField.getName());
                destinationField.setAccessible(true);

                if (isSimpleField(sourceField.getType())) {

                    destinationField.set(destinationObj, sourceValue);
                } else if (Collection.class.isAssignableFrom(sourceField.getType())) {

                    destinationField.set(destinationObj, mapCollection(sourceValue, destinationField));

                } else {

                    destinationField.set(destinationObj, this.map(sourceValue, destinationField.getType()));
                }

            } catch (NoSuchFieldException ignored) {
            }

        }

        return destinationObj;
    }

    private Collection<Object> mapCollection(Object sourceValue, Field destinationField) throws Exception {

        if (sourceValue != null) {

            Collection<?> sourceValueCollection = (Collection<?>) sourceValue;
            Collection<Object> destinationCollection = this.createCollectionInstance(destinationField.getType());

            for (Object item : sourceValueCollection) {

                destinationCollection.add(map(item, getGenericType(destinationField)));
            }

            return destinationCollection;
        }
        return null;
    }


    private Class<?> getGenericType(Field targetField) throws ClassNotFoundException {

        Type type = targetField.getGenericType();

        if (type instanceof ParameterizedType parameterizedType) {

            Type actual = parameterizedType.getActualTypeArguments()[0];

            return Class.forName(actual.getTypeName());
        }

        return Object.class;
    }


    private Collection<Object> createCollectionInstance(Class<?> type) {

        if (List.class.isAssignableFrom(type)) {

            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(type)) {

            return new HashSet<>();
        } else {

            throw new RuntimeException("Not Supported Collection Type: " + type.getName());
        }
    }


    private boolean isSimpleField(Class<?> sourceClass) {
        return sourceClass.isPrimitive()
                || sourceClass == String.class
                || Number.class.isAssignableFrom(sourceClass)
                || Character.class.isAssignableFrom(sourceClass)
                || Boolean.class.isAssignableFrom(sourceClass)
                || Date.class.isAssignableFrom(sourceClass);
    }

}
