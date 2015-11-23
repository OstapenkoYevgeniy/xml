package com.john.parser;

import com.john.entity.Beer;
import com.john.entity.Order;

import java.lang.reflect.*;
import java.util.*;

public class Scan {

    private String getSimpleName(String value) {
        return value.substring(value.lastIndexOf(".") + 1, value.length());
    }

    public Collection getCollection(Class aClass, String qName) {
        try {
            Field field = aClass.getDeclaredField(qName);
            if (field.getType() == List.class) {
                return new ArrayList<>();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("findFieldType", e);
        }
        return null;
    }


    public void setFieldValue (Object object, String field, Object value) {
        try {
            Field result = object.getClass().getDeclaredField(field);
            result.setAccessible(true);

            System.out.println("\\\\\\\\\\\\\\\\\\");
            System.out.println(result);
            System.out.println(field);
            System.out.println(object);
            System.out.println(value.getClass());
            System.out.println("\\\\\\\\\\\\\\\\\\");
            result.set(object,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("findFieldType", e);
        }
    }

    public Object findFieldType(Class clazz, String qName) {
        try {
            Field field = clazz.getDeclaredField(qName);
            field.setAccessible(true);
            Type type = field.getType();
            if (type == int.class || type == Integer.class) {
                return field;
            } else if (type == long.class || type == Long.class) {
                return field;
            } else if (type == String.class) {
                return field;
            } else {
                Class classType = (Class) type;
                classType.getInterfaces();
                Class[] interfaces = classType.getInterfaces();
                for (Class cInterface : interfaces) {
                    // TODO работает для коллекция с 1 значением, т.е. List и т.д.
                    System.out.println(cInterface);
                    if (cInterface == Collection.class) {
                        Deque deque = new ArrayDeque<>();
                        deque.add(field.getName());
                        return deque;
                    }
                }
                return Object.class;
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("findFieldType", e);
        }
    }

    public Object findTypeCollection(Class aClass, String last) {
        try {
            Field field = aClass.getDeclaredField(last);
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type type : types) {
                return Class.forName(type.getTypeName()).newInstance();
            }
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("findTypeCollection", e);
        }
        return null;
    }

    public static Object createObject(Class clazz, String qName) {
        try {
            Field field = clazz.getDeclaredField(qName);
            return field.getType().newInstance();
        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("createObject", e);
        }
    }

    public void setFieldValue(Object object, Field field, Object value) {
        System.out.println(value);
        Type type = field.getType();
        try {
            Class classForName = Class.forName(type.getTypeName());
            Constructor constructor = classForName.getConstructor(String.class);
            Object objectValue = constructor.newInstance(value); // TODO это плохо! разделить на ветки и все обертки проверить, newInstance \то плохо
            field.set(object, objectValue);
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("setFieldValue", e);
        }
    }

    public void setFieldValue(Object object, Field field, String value) {
        Type type = field.getType();
        try {
            if (type == long.class) {
                field.setLong(object, Long.parseLong(value));
            } else {
                Class classForName = Class.forName(type.getTypeName());

                Constructor constructor = classForName.getConstructor(String.class);
                Object objectValue = constructor.newInstance(value); // TODO это плохо! разделить на ветки и все обертки проверить
                field.set(object, objectValue);
            }
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("setFieldValue", e);
        }
    }

    public void setFieldValue(Object object, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(value.getClass().getSimpleName().toLowerCase());
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("setFieldValue", e);
        }
    }

    public void setFieldCollections(Field field, Object objcet, Object value) {
        try {
            field.setAccessible(true);
            field.set(objcet, value);
            Order order = (Order) objcet;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("setFieldValue", e);
        }
    }

    public Field getFieldCollection(Class clazz, String qName) {
        try {
            return clazz.getDeclaredField(qName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("setFieldValue", e);
        }
    }

    public Map findCollection(Class aClass, String qName) {
        System.out.println("-----------------------");
        Map<String, Map<String, Class>> result = new LinkedHashMap<>();
        Map<String, Class> inResult = new HashMap<>();

        Field field = null;
        try {
            field = aClass.getDeclaredField(qName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("setFieldValue", e);
        }

        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type[] types = parameterizedType.getActualTypeArguments();

        if (field.getType() == List.class) {
            try {
                inResult.put(getSimpleName(types[0].getTypeName()).toLowerCase(), Class.forName(types[0].getTypeName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("setFieldValue", e);
            }
           result.put(qName, inResult);
        }
        return result;
    }
}
