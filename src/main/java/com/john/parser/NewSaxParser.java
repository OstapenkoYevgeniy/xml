package com.john.parser;

import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class NewSaxParser implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(NewSaxParser.class);

    @Override
    public <T> T parse(File file, Class<T> clazz) throws Exception {
        log.debug("parse(File " + file + ", Class<T> " + clazz + ")");
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

        return (T) handler.objectDeque.getFirst();
    }

    private class Handler extends DefaultHandler {
        Scan scan = new Scan();

        StringBuilder character = new StringBuilder();
        Deque<Object> objectDeque = new ArrayDeque<>();
        Object currentObject;
        String crutch;

        Deque<String> collectionDeque = new ArrayDeque<>();

        Collection currentCollection;

        public Handler(Class clazz) {
            try {
                objectDeque.add(clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Handler конструктор краш!");
            }
        }

        @Override
        public void startDocument() throws SAXException {
            log.debug("startDocument...");
        }

        @Override
        public void endDocument() throws SAXException {
            log.debug("startDocument...");
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            log.debug("startElement: " + qName);
            if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                System.out.println("Current object");
            } else {
                currentObject = scan.findFieldType(objectDeque.getLast().getClass(), qName);
                System.out.println("new currentObject: " + currentObject);
                if (currentObject.getClass() == Field.class) {
                    System.out.println("This is field");
                } else if (currentObject.getClass() == ArrayDeque.class) {
                    currentCollection = scan.getCollection(objectDeque.getLast().getClass(), qName);
                    System.out.println("-----------------------654+54+97474");
                    System.out.println(currentCollection.getClass());
                    collectionDeque.addAll((ArrayDeque) currentObject);
                } else {
                    objectDeque.add(Scan.createObject(objectDeque.getLast().getClass(), qName));
                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            log.debug("endElement: " + qName);
            if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (objectDeque.size() > 1) {
                    Object object = objectDeque.getLast();
                    objectDeque.removeLast();
                    scan.setFieldValue(objectDeque.getLast(), object);
                }
            } else if (currentObject.getClass() == Field.class) {
                scan.setFieldValue(objectDeque.getLast(), (Field) currentObject, character.toString());
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            character.setLength(0);
            character.append(ch, start, length);
        }

        private String getSimpleName(String value) {
            return value.substring(value.lastIndexOf(".") + 1, value.length());
        }
    }
}
