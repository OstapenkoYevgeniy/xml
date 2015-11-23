package com.john.parser.forParserClass;

import com.john.parser.ClassScanner;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

public class CollectionType {
    private Object type;
    private String fieldName;
    private Object collectionItem;
    private Deque dequeItem = new ArrayDeque<>();


    public CollectionType(Field field, String qName) {
        this.fieldName = qName;
        this.type = getType(field);
        this.collectionItem = getCollectionItem(field);

    }

    public Deque getDequeItem() {
        return dequeItem;
    }

    public void addLastDeqeItemInCollection() {
        Collection collection = (Collection) type;
        collection.add(dequeItem.getLast());
        type = collection;
        dequeItem.removeLast();
    }

    public void addLastDeqeItemInCollection(String value) {

        Collection collection = (Collection) type;
        collection.add(value);
        type = collection;
        System.out.println(fieldName);
        System.out.println(type);
        System.out.println("||||" + dequeItem.getLast());
        dequeItem.removeLast();
    }

    public void addDequeItem (Object objcet) {
        System.out.println("addDequeItem ******************************");
        System.out.println(objcet);
        dequeItem.add(objcet);
        System.out.println("addDequeItem ******************************");
    }

    public Object getLastDequeItem() {
        if (dequeItem.size() == 0) {
            return "false";
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
        if (field.getType().isInterface()) {
            if (isList(field.getType())) {
                return new ArrayList<>();
            } else {
                // При добавлении Map необходимо и пересмотреть логику сollectionItem, и сollectionItem(Field field)
                throw new UnsupportedOperationException("Not supported map");
            }
        } else {
            try {
                return field.getType().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Error когда получаем List<List<Object>> lists
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
        if (clazz == Collection.class) {
            return true;
        }
        Class[] interfaces = clazz.getInterfaces();
        for (Class anInterface : interfaces) {
            return isList(anInterface);
        }
        return false;
    }
}
