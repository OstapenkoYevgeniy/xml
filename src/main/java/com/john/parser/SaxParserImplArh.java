//package com.john.parser;
//
//import com.john.annotation.*;
//import com.john.entity.Beer;
//import com.john.parser.Parser;
//import com.john.parser.ParserException;
//import org.slf4j.Logger;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import java.io.File;
//import java.lang.annotation.Annotation;
//import java.lang.invoke.MethodHandle;
//import java.lang.invoke.MethodHandles;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.function.Function;
//import java.util.function.ObjDoubleConsumer;
//
//public class SaxParserImpl implements Parser {
//    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParserImpl.class);
//    private Map<String, MethodHandle> configuration = new HashMap<>();
//    private Map<Class, Function<String, ?>> converter = new HashMap<>();
//
//    public void configure(Class clazz) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
//        log.info("SaxParser.Configure() start!");
//
//        MethodHandles.Lookup lookup = MethodHandles.lookup();
//
//        Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
//        XmlRootElement xmlRootElement = (XmlRootElement) annotation;
//        String rootElement = xmlRootElement.name();
//
//        Method[] methods = clazz.getDeclaredMethods();
//
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(XmlElement.class)) {
//                if (method.isAnnotationPresent(XmlNewClass.class)) {
//                    configuration.put(method.getAnnotation(XmlElement.class).name(), lookup.unreflect(method));
//                    configure(method.getAnnotation(XmlNewClass.class).newClass());
//                } else {
//                    configuration.put(method.getAnnotation(XmlElement.class).name(), lookup.unreflect(method));
//                }
//            }
//        }
////        for (Field field : fields) {
////            String fieldName = field.getName();
////            for (Method method : methods) {
////                if (method.isAnnotationPresent(SetterField.class)) {
////
////
////                    if (fieldName.equals(method.getAnnotation(SetterField.class).name())) {
////                        configuration.put(fieldName, lookup.unreflect(method));
////                    }
////                }
////            }
////        }
////
////        for (Map.Entry<String, MethodHandle> entry : configuration.entrySet()) {
////            System.out.println(entry.getKey() + " - " + entry.getValue());
////        }
//
////        converter.put(int.class, s -> Integer.parseInt((String) s));
////        converter.put(double.class, s -> Double.parseDouble((String) s));
////        converter.put(float.class, s -> Float.parseFloat((String) s));
////        converter.put(String.class, String::valueOf);
////        converter.put(Beer.Alcohol.class, s -> Beer.Alcohol.valueOf((String) s));
//    }
//
//    public void showConfigure() {
//        for (Map.Entry<String, MethodHandle> entry : configuration.entrySet()) {
//            System.out.println(entry.getKey() + " - " + entry.getValue());
//        }
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
//        } catch (Exception e) {
//            log.error("Exception", e);
//            throw new ParserException("ParserException", e);
//        }
//
//        return (T) handler.obj;
//    }
//
//    private class Handler extends DefaultHandler {
//        Class clazz;
//        Object obj;
//        Object curretObject;
//        Class curretClass;
//        StringBuilder charaster;
//        String currentElement;
//        String rootElement = "";
//        Deque<Object> deque = new ArrayDeque<>();
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
//            currentElement = "";
//        }
//
//        @Override
//        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//            //log.debug("startElement(" + s + ", " + s1 + ", " + s2 + ", attributes)");
//             System.out.println("s|" + namespaceURI + "| |" + localName + "| |" + qName + "|");
//            currentElement = qName;
//
//        }
//
//        @Override
//        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//            // log.debug("endElement(" + s + ", " + s1 + ", " + s2 + ")");
//            // System.out.println("e|" + namespaceURI + "| |" + localName + "| |" + qName + "|");
//
////            try {
////                MethodHandle mh = configuration.get(currentElement);
////                if (mh != null) {
////                    System.out.println(charaster);
////                    //mh.invoke(obj, converter.get(mh.type().parameterType(1)).apply(charaster.toString()));
////                }
////            } catch (Throwable throwable) {
////                throwable.printStackTrace();
////            }
////            currentElement = "";
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
