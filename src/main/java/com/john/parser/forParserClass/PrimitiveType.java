package com.john.parser.forParserClass;

import java.lang.reflect.Field;

public class PrimitiveType {
    private Field field;
    private Class type;
    private String fieldName;

    public PrimitiveType(Field field, Class type, String fieldName) {
        this.field = field;
        this.type = type;
        this.fieldName = fieldName;
    }

    public Field getField() {
        return field;
    }

    public Class getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }
}