package com.john.parser;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    Logger log = org.slf4j.LoggerFactory.getLogger(ClassScanner.class);

    public Object findField(Class clazz, String pfield) {
        System.out.println("456456456");
        System.out.println(clazz + " --- " + pfield);
        System.out.println("456456456");
        // Если текущий элемент XML equals Имени класса, иначе это поле
        if (clazz.getSimpleName().toLowerCase().equals(pfield)) {
            System.out.println("111111111111111111111111111111111111111111");
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("!!!");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("!!!");
            }
        } else if (clazz == ArrayList.class) {
            System.out.println("222222222222222222222222222222222222222222");
            System.out.println("***********************************");
            System.out.println("***********************************");
            System.out.println("***********************************");
            System.out.println("***********************************");
            System.out.println("***********************************");
            System.out.println("***********************************");
            System.out.println("***********************************");
        } else {
            System.out.println("33333333333333333333333333333333333333");
            Field field = null;
            try {
                field = clazz.getDeclaredField(pfield);
                // Если поле примитив, то вернуть метод этого поля
                if (field.getType().isPrimitive()) {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    Method method = clazz.getDeclaredMethod("set" + firstUpper(pfield), field.getType());
                    return method;
                } else if (field.getType() == String.class) {
                    log.debug("set" + firstUpper(pfield), field.getType());
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    Method method = clazz.getDeclaredMethod("set" + firstUpper(pfield), field.getType());
                    return method;
                } else if (isListITD(field.getType())) {
                    switch (field.getType().getSimpleName()) {
                        case "List":
                            return new ArrayList<>();
                        default:
                            throw new RuntimeException("BASD");
                    }
                } else if (field.getName().equals(pfield)) {
                    return field.getType().newInstance();
                }
            } catch (Exception e) {
//                throw new RuntimeException("Class  scanner findField error NoSuchFieldException");
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR ");
                return null;
            }
        }
        return null;
    }

    public Method findMethod(Class aClass, String qName) {
        System.out.println("asdasdqweqwe");
        System.out.println(aClass + " - " + qName);
        try {

            Field field = aClass.getDeclaredField(qName);
            System.out.println("set" + firstUpper(qName) + " - " + field.getType());
            return aClass.getMethod("set" + firstUpper(qName), field.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Method findAddMethod(Class aClass, String qName) {
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(qName)) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] types = parameterizedType.getActualTypeArguments();
                for (Type type : types) {
                    try {
                        return aClass.getMethod("add" + deletaLastLetter(firstUpper(qName)),Class.forName(type.getTypeName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

//    public Method findMethod(Class aClass, String field, Class classParam) {
//        switch (classParam.getSimpleName()) {
//            case "List":
//                System.out.println("add" + deletaLastLetter(firstUpper(field)));
//                return null;
//            default:
//                throw new RuntimeException("Illegal argument!!");
//        }
//    }

    // Если класс реализует интерфейс листа, мапы, и прочего
    public boolean isListITD(Class clazz) {
        switch (clazz.getSimpleName()) {
            case "List":
                return true;
            case "Map":
                return true;
            default:
                return false;
        }
    }

    private String firstUpper(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1, value.length());
    }

    private String deletaLastLetter(String value) {
        return value.substring(0, value.length() - 1);
    }

    private static String classSimpleName(String className) {
        return className.substring(className.lastIndexOf(".") + 1, className.length());
    }

    public Object findFieldList(Class aClass, String qName) {
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isInterface()) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] types = parameterizedType.getActualTypeArguments();
                for (Type type : types) {
                    if (classSimpleName(type.getTypeName()).toLowerCase().equals(qName)) {
                        try {
                            try {
                                return Class.forName(type.getTypeName()).newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}

//
//
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
