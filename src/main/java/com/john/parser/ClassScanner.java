package com.john.parser;

import com.john.parser.forParserClass.CollectionType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class ClassScanner {
    public Object find(Class<?> aClass, String qName) {

        Field field = null;

        try {
            field = aClass.getDeclaredField(qName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Type type = field.getType();

        // TODO найти информацию о том, какие типы как часто встречаються и переставить условия
        if (type == boolean.class || type == byte.class || type == char.class ||
                type == short.class || type == int.class || type == long.class) {
            return field;
        } else if (type == Boolean.class || type == Byte.class || type == Character.class ||
                type == Short.class || type == Integer.class || type == Long.class || type == String.class) {
            return field;
        } else if (isCollection((Class) type)) {
            return new CollectionType(field, qName);
        } else {
            try {
                return ((Class) type).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setField(Object object, String field, Object value) {
        Field resultField = null;
        try {
            resultField = object.getClass().getDeclaredField(field);
            resultField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            if (resultField.getType() == long.class) {
                resultField.setLong(object, Long.parseLong((String) value));
            } else if (resultField.getType() == Long.class) {
                resultField.set(object, new Long((String) value));
            } else {
                resultField.set(object, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isCollection(Class clazz) {
        if (clazz == Collection.class || clazz == Map.class) {
            return true;
        }
        Class[] interfaces = clazz.getInterfaces();
        for (Class anInterface : interfaces) {
            return isCollection(anInterface);
        }
        return false;
    }
}
