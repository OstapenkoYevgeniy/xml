package com.john.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class CollectionType {
    // Interface Collection or Map
    private Object type;
    private String fieldName;
    private Object collectionItem;
    private Deque dequeItem = new ArrayDeque<>();

    public CollectionType(Field field, String qName) {
        this.fieldName = qName;
        this.type = getType(field);
        this.collectionItem = getCollectionItem(field);
    }

    public void addValueInCollection() {
        Collection collection = (Collection) type;
        collection.add(dequeItem.getLast());
        type = collection;
        dequeItem.removeLast();
    }

    public void addValueInCollection(String value) {
        Collection collection = (Collection) type;
        collection.add(value);
        type = collection;
        dequeItem.removeLast();
    }

    public void addItemToDeque(Object objcet) { dequeItem.add(objcet); }

    public Object getLastDequeItem() {
        if (dequeItem.size() == 0) {
            return "Crutch :(";
        } else {
            return dequeItem.getLast();
        }
    }

    public Object getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getCollectionItem() {
        return collectionItem;
    }

    private Object getType(Field field) {
            if (isList(field.getType())) {
                return new ArrayList<>();
            } else {
                return new HashMap<>();
            }
    }

    private Object getCollectionItem(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type[] types = parameterizedType.getActualTypeArguments();
        try {
            return Class.forName(types[0].getTypeName()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isList(Class clazz) {
        if (clazz == Collection.class) { return true; }
        Class[] interfaces = clazz.getInterfaces();
        // TODO Вопрос! Почему подсвечивает for? Как это исправить?
        for (Class anInterface : interfaces) return isList(anInterface);
        return false;
    }
}
