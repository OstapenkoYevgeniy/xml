//package com.john.parser;
//
//import com.john.annotation.XmlElement;
//import com.john.annotation.XmlNewClass;
//import com.john.annotation.XmlRootElement;
//import com.john.entity.Customer;
//import com.john.entity.Order;
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
//import java.lang.reflect.*;
//import java.util.ArrayDeque;
//import java.util.Deque;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//public class SaxParserImpl implements Parser {
//    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParserImpl.class);
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
//        ClassScanner classScanner = new ClassScanner();
//        Class clazz;
//        Object obj;
//        StringBuilder charaster;
//        String currentElement;
//        //Object currentObject;
//        Class currentClass;
//        Method currentMethod;
//        Deque<Object> deque = new ArrayDeque<>();
//
//        public Handler(Class clazz) {
//            this.clazz = clazz;
//            this.currentClass = clazz;
//
//        }
//
//        @Override
//        public void startDocument() throws SAXException {
////            log.debug("BeerHandler.startDocument()");
////            try {
////                deque.add(clazz.newInstance());
////            } catch (InstantiationException | IllegalAccessException e) {
////                e.printStackTrace();
////            }
//            charaster = new StringBuilder();
//        }
//
//        @Override
//        public void endDocument() throws SAXException {
////            log.debug("BeerHandler.endDocument()");
//            charaster.setLength(0);
//            currentElement = "";
//            System.out.println("Конец документа!");
//            System.out.println(deque.size());
//            System.out.println("deque.getFirst() - " + deque.getFirst());
//            obj = deque.getFirst();
//        }
//
//        @Override
//        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//            log.debug("s|" + namespaceURI + "| |" + localName + "| |" + qName + "|");
//
//            try {
//                System.out.println("----------------------------");
//                System.out.println(currentClass);
//                System.out.println(deque.size());
//                System.out.println("----------------------------");
//
//                Object object = classScanner.findField(currentClass, qName);
//                if (object instanceof Method) {
//                    System.out.println("Добавление метода: " + object);
//                    currentMethod = (Method) object;
//                } else {
//                    System.out.println("Добавление объекта в очередь: " + object);
//                    deque.add(object);
//                    currentClass = object.getClass();
//                }
////                Object object = classScanner.findField(currentClass, qName);
////                if (object instanceof Method) {
////                    System.out.println("Это метод!!!");
////                } else {
////                    currentObject = object;
////                    currentClass = clazz;
////                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//        @Override
//        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//             log.debug("e|" + namespaceURI + "| |" + localName + "| |" + qName + "|");
//            if (currentMethod != null) {
//                Type[] type = currentMethod.getGenericParameterTypes();
//                try {
//                    System.out.println("end elem METHOD>>");
//                    System.out.println(deque.getLast());
//
//                    currentMethod.invoke(deque.getLast(), converter(charaster.toString(), type[0]));
////                    Order od = (Order) deque.getLast();
////                    System.out.println(od.getId());
////                    currentMethod.invoke(converter(charaster.toString(), type[0]));
//                    System.out.println("<< end elem");
//                    currentMethod = null;
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            } else if (currentClass.getSimpleName().toLowerCase().equals(qName)) {
//                Object objjj = deque.getLast();
//                deque.removeLast();
//                Object objEnd = deque.getLast();
//                objEnd
//            }
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
//
//    private Object converter (String strint, Object toObject) {
//        switch (toObject.toString()) {
//            case "long": return Long.parseLong(strint);
//            default: throw new UnsupportedOperationException("Неизвестынй тип!");
//        }
//    }
//}
