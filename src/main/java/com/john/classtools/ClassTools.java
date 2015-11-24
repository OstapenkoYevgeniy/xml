package com.john.classtools;

import com.john.parser.CollectionType;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Class class fields for searching and for setting the values of these fields.
 */
public class ClassTools {
    Logger log = org.slf4j.LoggerFactory.getLogger(ClassTools.class);

    /**
     * Seeking a field in the class.
     * Returns the type of Field, if the field type is primitive or primitive wrapper over.
     * Returns the type of CollectionType, if the field type Collection or Map.
     * Returns the type of CollectionType, if the field type Collection or Map.
     * If the field type object creates an instance of the class and returns.
     */
    public Object find(Class<?> aClass, String qName) throws ClassToolsException {

        Field field;

        try {
            field = aClass.getDeclaredField(qName);
        } catch (NoSuchFieldException e) {
            log.error("ClassToolsException", e);
            throw new ClassToolsException(e);
        }

        Type type = field.getType();

        // TODO найти информацию о том, какие типы как часто встречаються и переставить условия, т.к. работает до первого TRUE
        // Микрооптимизация
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
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("ClassToolsException", e);
                throw new ClassToolsException(e);
            }
        }
    }

    // Set value to field of object
    // TODO Не сетать значения напрямую в поля, искать сеттеры этих полей и вызывать их. Безопасность.
    public void setField(Object object, String field, Object value) throws ClassToolsException {
        Field resultField;

        try {
            resultField = object.getClass().getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            log.error("ClassToolsException", e);
            throw new ClassToolsException(e);
        }

        resultField.setAccessible(true);

        /**
         * There is support int, Integer, long, Long, String and other Objects;
         * To support the remaining primitives and wrappers on them need to add code.
         */
        try {
            if (resultField.getType() == int.class) {
                resultField.setInt(object, Integer.parseInt((String) value));
            } else if (resultField.getType() == Integer.class) {
                resultField.set(object, new Integer((String) value));
            } else if (resultField.getType() == long.class) {
                resultField.setLong(object, Long.parseLong((String) value));
            } else if (resultField.getType() == Long.class) {
                resultField.set(object, new Long((String) value));
            } else {
                resultField.set(object, value);
            }
        } catch (IllegalAccessException e) {
            log.error("ClassToolsException", e);
            throw new ClassToolsException(e);
        }
    }


    public boolean isCollection(Class clazz) {
        if (clazz == Collection.class || clazz == Map.class) {
            return true;
        }

        Class[] interfaces = clazz.getInterfaces();

        // TODO Вопрос! Почему IDEA подсвечивает желтым строку ниже? Как это исправить?
        for (Class anInterface : interfaces) return isCollection(anInterface);

        return false;
    }
}
