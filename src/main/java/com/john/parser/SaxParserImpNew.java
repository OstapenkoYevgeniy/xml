package com.john.parser;

import com.john.entity.Customer;
import com.john.entity.Order;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class SaxParserImpNew implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParserImpNew.class);

    @Override
    public <T> T parse(File file, Class<T> clazz) throws Exception {
        log.debug("Start SaxParser.parseBeer(File " + file + ", Class<T> " + clazz + ")");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;

        Handler handler = new Handler(clazz);
        try {
            parser = factory.newSAXParser();
            parser.parse(file, handler);
        } catch (Exception e) {
            log.error("Exception", e);
            throw new ParserException("ParserException", e);
        }

        return (T) handler.result;
    }

    private class Handler extends DefaultHandler {

        Object result;
        Deque<Object> objectDeque = new ArrayDeque<>();

        ClassScanner classScanner = new ClassScanner();
        StringBuilder character;

        List currentList;

        Object startObject;

        Object currentObject;

        public Handler(Class clazz) throws Exception {
            objectDeque.add(clazz.newInstance());
        }

        @Override
        public void startDocument() throws SAXException {
            log.debug("Handler.startDocument()");
            character = new StringBuilder();
        }

        @Override
        public void endDocument() throws SAXException {
            log.debug("Handler.endDocument()");
            System.out.println(objectDeque.getFirst());
            objectDeque.removeFirst();
            result = objectDeque.getFirst();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            log.debug("startElement: " + qName);
            log.debug("startElement: objectDeque.getLast() - " + objectDeque.getLast());

            startObject = null;


            try {
                startObject = classScanner.findField(objectDeque.getLast().getClass(), qName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (startObject instanceof Method) {
                System.out.println("ЭТО МЕТОД!!!");
            }  else {
                System.out.println("ЭТО ОБЪЕКТ!!!");
                objectDeque.add(startObject);
            }
            character.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            log.debug("endElement: " + qName);
            log.debug("endElement: objectDeque.getLast() - " + objectDeque.getLast());
            log.debug("endElement: startObject - " + startObject);

            if (startObject instanceof Method) {
                System.out.println("ЭТО МЕТОД!!!!");
                Type[] types = ((Method) startObject).getParameterTypes();
                try {
                    System.out.println("set: " + converter(character.toString(), types[0]));
                    System.out.println(character.toString());
                    ((Method) startObject).invoke(objectDeque.getLast(), converter(character.toString(), types[0]));
                    startObject = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (objectDeque.getLast() instanceof List) {
                System.out.println("------------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------------");
                System.out.println(classScanner.findMethod(objectDeque.getLast().getClass(), qName, List.class));
                System.out.println("------------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------------");
                startObject = null;
            } else {
                System.out.println("Это не метод!!!");
                if (objectDeque.size() > 2) {
                    System.out.println("**************");
                    Object obj = objectDeque.getLast();
                    objectDeque.removeLast();
                    System.out.println("888888888888888888888888888");
                    System.out.println(objectDeque.getLast().getClass() + " - " + qName);
                    try {
                        Method mh = classScanner.findMethod(objectDeque.getLast().getClass(), qName);
                        mh.invoke(objectDeque.getLast(), obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("**************");
                }

            }
//            if (currentObject == null) {
//            } else if (currentObject instanceof Method) {
//                System.out.println("Объект в старт элементе: " + objectDeque.getLast());
//                System.out.println("Это метод! Его надо закончить. Текущий объект: " + currentObject);
//                Type[] types = ((Method) currentObject).getParameterTypes();
//                try {
//                    System.out.println(objectDeque.getLast());
//                    ((Method) currentObject).invoke(objectDeque.getLast(), converter(character.toString(),types[0]));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                currentObject = null;
//            } else {
//                System.out.println("Это " + currentObject);
//            }
            character.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            character.setLength(0);
            character.append(ch, start, length);
        }
    }

    private Object converter(String strint, Object toObject) {
        switch (toObject.toString()) {
            case "long":
                return Long.parseLong(strint);
            case "class java.lang.String":
                return strint;
            default:
                System.out.println(toObject.toString());
                throw new UnsupportedOperationException("Неизвестынй тип!");
        }
    }
}
