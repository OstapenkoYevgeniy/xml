package com.john.parser;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ClassScanner {
    Logger log = org.slf4j.LoggerFactory.getLogger(ClassScanner.class);
//    private Map<String, MethodHandle> configuration = new HashMap<>();
//    private Map<Class, Function<String, ?>> converter = new HashMap<>();
//
//    private Map<String, Map<String, Method>> config = new HashMap<>();
//    public void configure(Class clazz) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
//        log.info("SaxParser.Configure() start!");
//
//        Map<String, Method> sm = new HashMap<>();
//
//        MethodHandles.Lookup lookup = MethodHandles.lookup();
//
//        Field[] fields = clazz.getDeclaredFields();
//        String object = clazz.getSimpleName();
//        for (Field field : fields) {
//            String simpleName = field.getName();
//            Class classType = field.getType();
//            if (!classType.getSimpleName().equals("List")) {
//                sm.put(simpleName, clazz.getMethod("set" + firstUpper(simpleName), classType));
//            } else {
//                sm.put(simpleName,clazz.getMethod("add" + firstUpper(simpleName),getGenericType(field.getGenericType())));
//            }
//
//            System.out.println(field.getType());
//            System.out.println(field.getType().getDeclaredFields());
//        }
//        config.put(object, sm);
//    }

    public Object findField(Class clazz, String pfield) throws IllegalAccessException, InstantiationException {
        if (clazz.getSimpleName().toLowerCase().equals(pfield)) {
            System.out.println("Class - " + pfield);
            return clazz.newInstance();
        } else {
            Field field = null;
            try {
                field = clazz.getDeclaredField(pfield);
                if (field.getType().isPrimitive()) {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    Method method = clazz.getDeclaredMethod("set" + firstUpper(pfield), field.getType());
                    return method;
                } else if (isListITD(field.getType())) {
                    switch (field.getType().getSimpleName()) {
                        case "List": return new ArrayList<>();
                          default: throw new RuntimeException("BASD");
                    }
                } else if (field.getName().equals(pfield)) {
                    System.out.println("NEW OBJECT");
                    System.out.println(field.getType().isInterface());
                    return field.getType().newInstance();
                }

                return null;
            } catch (NoSuchFieldException e) {
//                throw new RuntimeException("Class  scanner findField error NoSuchFieldException");
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR NoSuchFieldException");
            } catch (NoSuchMethodException e) {
               // throw new RuntimeException("Class scanner findField error NoSuchMethodException");
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR NoSuchFieldException");
            }
        }
        return null;
    }

    public boolean isListITD (Class clazz) {
        switch (clazz.getSimpleName()) {
            case "List" : return true;
            case "Map" : return true;
            default: return false;
        }
    }

    private String firstUpper(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1, value.length());
    }
}
