//package com.john.parser.impl;
//
//
//import com.john.annotation.SetterField;
//import com.john.entity.Beer;
//import com.john.parser.Parser;
//import com.john.parser.ParserException;
//import org.slf4j.Logger;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import java.io.File;
//import java.io.IOException;
//
//import java.lang.invoke.MethodHandle;
//import java.lang.invoke.MethodHandles;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.function.Function;
//
//
//public class SaxParser implements Parser {
//    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParser.class);
//    private Map<String, MethodHandle> configuration;
//    private Map<Class, Function<String, ?>> converter;
//
//    public void configure(Class clazz) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
//        log.info("SaxParser.Configure() start!");
//        configuration = new HashMap<>();
//        converter = new HashMap<>();
//
//        MethodHandles.Lookup lookup = MethodHandles.lookup();
//
//        Field[] fields = clazz.getDeclaredFields();
//        Method[] methods = clazz.getDeclaredMethods();
//        for (Field field : fields) {
//            String fieldName = field.getName();
//            for (Method method : methods) {
//                if (method.isAnnotationPresent(SetterField.class)) {
//                    if (fieldName.equals(method.getAnnotation(SetterField.class).name())) {
//                        configuration.put(fieldName, lookup.unreflect(method));
//                    }
//                }
//            }
//        }
//
//        converter.put(int.class, s -> Integer.parseInt((String) s));
//        converter.put(double.class, s -> Double.parseDouble((String) s));
//        converter.put(float.class, s -> Float.parseFloat((String) s));
//        converter.put(String.class, String::valueOf);
//        converter.put(Beer.Alcohol.class, s -> Beer.Alcohol.valueOf((String) s));
//    }
//
//    public void configure(Map<String, MethodHandle> configuration, Map<Class, Function<String, ?>> converter) {
//        this.configuration = configuration;
//        this.converter = converter;
//    }
//
//    @Override
//    public <T> T parse(File file, Class<T> clazz) throws ParserException {
//        log.debug("Start SaxParser.parseBeer(File file)");
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        SAXParser parser;
//
//        Handler handler = new Handler(clazz);
//        try {
//            parser = factory.newSAXParser();
//            parser.parse(file, handler);
//        } catch (ParserConfigurationException e) {
//            log.error("ParserConfigurationException", e);
//            throw new ParserException("ParserConfigurationException", e);
//        } catch (SAXException e) {
//            log.error("SAXException", e);
//            throw new ParserException("SAXException", e);
//        } catch (IOException e) {
//            log.error("IOException", e);
//            throw new ParserException("IOException", e);
//        }
//
//        return (T) handler.obj;
//    }
//
//
//    private class Handler extends DefaultHandler {
//        Class clazz;
//        Object obj;
//        StringBuilder charaster;
//        String currentElement;
//
//        public Handler(Class clazz) {
//            this.clazz = clazz;
//        }
//
//
//        @Override
//        public void startDocument() throws SAXException {
//            log.debug("BeerHandler.startDocument()");
//            try {
//                obj = clazz.newInstance();
//
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            charaster = new StringBuilder();
//        }
//
//        @Override
//        public void endDocument() throws SAXException {
//            log.debug("BeerHandler.endDocument()");
//            charaster.setLength(0);
//        }
//
//        @Override
//        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//            //log.debug("startElement(" + s + ", " + s1 + ", " + s2 + ", attributes)");
//            currentElement = qName;
//        }
//
//        @Override
//        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//            // log.debug("endElement(" + s + ", " + s1 + ", " + s2 + ")");
//            try {
//                MethodHandle mh = configuration.get(currentElement);
//                if (mh != null) {
//                    System.out.println(charaster);
//                    //mh.invoke(obj, converter.get(mh.type().parameterType(1)).apply(charaster.toString()));
//                }
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//            currentElement = "";
//
//        }
//
//        @Override
//        public void characters(char[] chars, int start, int end) throws SAXException {
//            //log.debug("characters(" + new String(chars, start, end) + ", " + start + ", " + end + ")");
//            charaster.setLength(0);
//            charaster.append(chars, start, end);
//        }
//    }
//}
