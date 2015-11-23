package com.john.parser;

import com.john.entity.Beer;
import com.john.entity.Characteristic;
import com.john.entity.Customer;
import com.john.parser.forParserClass.CollectionType;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class NewNewSaxParser implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(NewNewSaxParser.class);

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

        //return (T) new Object();
        return (T) handler.objectDeque.getFirst();
    }

    private class Handler extends DefaultHandler {
        ClassScanner classScanner = new ClassScanner();
        StringBuilder character = new StringBuilder();
        Deque<Object> objectDeque = new ArrayDeque<>();
        Object currentObject;

        public Handler(Class clazz) {
            try {
                objectDeque.add(clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Handler конструктор краш!");
            }
        }

        @Override
        public void startDocument() throws SAXException {
//            log.debug("startDocument...");
        }

        @Override
        public void endDocument() throws SAXException {
//            log.debug("startDocument...");
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            log.debug("startElement: " + qName);

            if (!objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (objectDeque.getLast() instanceof CollectionType) {
                    CollectionType collectionType = (CollectionType) objectDeque.getLast();

                    // Если элемент коллекции
                    if (collectionType.getCollectionItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                        try {
                            collectionType.addDequeItem(collectionType.getCollectionItem().getClass().newInstance());
                            System.out.println(collectionType.getDequeItem());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("------------------------");
                        System.out.println("|- " + collectionType.getLastDequeItem());
                        System.out.println("|- " + qName);
                        Object obj = classScanner.find(collectionType.getLastDequeItem().getClass(), qName);
                        System.out.println(obj);
                        if (obj instanceof Field) {
                            currentObject = obj;
                        } else if (obj instanceof CollectionType) {

                            objectDeque.add(obj);
                            currentObject = (CollectionType) obj;

                        } else {
                            System.out.println("Это объект!!!" + obj);
                        }
                    }
                } else {
                    Object obj = classScanner.find(objectDeque.getLast().getClass(), qName);
                    if (obj instanceof Field) {
                        currentObject = obj;
                    } else if (obj instanceof CollectionType) {
                        System.out.println("Создание коллекции");
                        objectDeque.add(obj);
                        currentObject = (CollectionType) obj;
                    } else if (obj instanceof Object) {
                        objectDeque.add(obj);
                        currentObject = obj;
                    }
                }
            }
//            System.out.println("FINISH startElement ---------------------------------");
        }

        private void test(String qName) {
            CollectionType collectionType = (CollectionType) objectDeque.getLast();
            // закрытие последнего объекта в коллекциях
            if (collectionType.getLastDequeItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                // Если другие примитивы и обертки то не сработает!
                if (qName.equals("string")) {
                    collectionType.addLastDeqeItemInCollection(character.toString());
                } else  if (collectionType.getCollectionItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                    collectionType.addLastDeqeItemInCollection();
                }
            } else if (collectionType.getFieldName().equals(qName)) {

                Object resultCollection = collectionType.getType();

                System.out.println(objectDeque);
                objectDeque.removeLast();

                if (objectDeque.getLast() instanceof CollectionType) {
                    System.out.println("*************************");

                    CollectionType colType = (CollectionType) objectDeque.getLast();
                    System.out.println("1 " +colType.getLastDequeItem());
                    System.out.println("2 " +qName);
                    System.out.println("3 " +resultCollection);
                    classScanner.setField(colType.getLastDequeItem(), qName, resultCollection);
                } else {
                    classScanner.setField(objectDeque.getLast(), qName, resultCollection);
                }
            } else {

                System.out.println("||||" + currentObject);
                if (currentObject instanceof Field) {
                    System.out.println("---------------------------------");
                    System.out.println(collectionType.getLastDequeItem());
                    classScanner.setField(collectionType.getLastDequeItem(), qName, character.toString());
                    currentObject = null;

                    Object obj = collectionType.getLastDequeItem();
                    if (obj instanceof Characteristic) {
                       Characteristic ch = (Characteristic) obj;
                        System.out.println(ch.getDescription());
                        System.out.println(ch.getValue());
                    }

//                    Beer beer = (Beer) collectionType.getLastDequeItem();
//                    System.out.println(beer.getId());
//                    System.out.println(beer.getName());
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            log.debug("endElement: " + qName + " CurrentObject: " + currentObject);
            if (objectDeque.getLast() instanceof CollectionType) {
                test(qName);

            } else if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (objectDeque.size() > 1) {
                    System.out.println("objectDeque.getLast() - " + objectDeque.getLast());
                    Object resultObject = objectDeque.getLast();
                    objectDeque.removeLast();
                    classScanner.setField(objectDeque.getLast(), qName, resultObject);
                }
            } else {
                classScanner.setField(objectDeque.getLast(), qName, character.toString());
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
