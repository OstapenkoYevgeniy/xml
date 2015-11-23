//package com.john.parser;
//
//import com.john.entity.Beer;
//import org.slf4j.Logger;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import java.io.File;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.util.*;
//
//public class NewSaxParser implements Parser {
//    private Logger log = org.slf4j.LoggerFactory.getLogger(NewSaxParser.class);
//
//    @Override
//    public <T> T parse(File file, Class<T> clazz) throws Exception {
//        log.debug("parse(File " + file + ", Class<T> " + clazz + ")");
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
//        return (T) handler.objectDeque.getFirst();
//    }
//
//    private class Handler extends DefaultHandler {
//        Scan scan = new Scan();
//
//        StringBuilder character = new StringBuilder();
//        Deque<Object> objectDeque = new ArrayDeque<>();
//        Object currentObject;
//
//        Deque<String> collectionDeque = new ArrayDeque<>();
//        LinkedHashMap<String, Map<String, Class>> collectionMap = new LinkedHashMap<>();
//        Deque<Collection> currentCollection = new ArrayDeque<>(); // TODO так же переделать в очередь
//
//
//        public Handler(Class clazz) {
//            try {
//                objectDeque.add(clazz.newInstance());
//            } catch (Exception e) {
//                throw new RuntimeException("Handler конструктор краш!");
//            }
//        }
//
//        @Override
//        public void startDocument() throws SAXException {
//            log.debug("startDocument...");
//        }
//
//        @Override
//        public void endDocument() throws SAXException {
//            log.debug("startDocument...");
//        }
//
//
//        @Override
//        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            log.debug("startElement: " + qName);
//            log.debug("collectionMap.size() = " + collectionMap.size());
//            if (collectionMap.size() > 0) {
//                System.out.println("ЕСТЬ МАПА!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                if (collectionMap.get(collectionDeque.getLast()).get(qName) != null) {
//                    try {
//                        System.out.println("|||" + collectionMap.get(collectionDeque.getLast()).get(qName));
//
//                        if (collectionMap.get(collectionDeque.getLast()).get(qName) == Integer.class) {
//                            Constructor constructor = collectionMap.get(collectionDeque.getLast()).get(qName).getConstructor(String.class);
//                            objectDeque.add(constructor.newInstance("0"));
//                        } else {
//                            objectDeque.add(collectionMap.get(collectionDeque.getLast()).get(qName).newInstance());
//                        }
//                    } catch (Exception e) {
//                        throw new RuntimeException("ParserException", e);
//                    }
//                } else {
//                    System.out.println("--------------------------------");
//                    System.out.println("+" + objectDeque.getLast().getClass());
//                    System.out.println("+" + qName);
//                    currentObject = scan.findFieldType(objectDeque.getLast().getClass(), qName);
//                }
//
//
//            } else if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
//                System.out.println("Current object");
//            } else {
//                currentObject = scan.findFieldType(objectDeque.getLast().getClass(), qName);
//                log.debug(currentObject.getClass().toString());
//                System.out.println("new currentObject: " + currentObject);
//                if (currentObject.getClass() == Field.class) {
//                    System.out.println("This is field");
//                } else if (currentObject.getClass() == ArrayDeque.class) {
//                    //currentCollection = scan.getCollection(objectDeque.getLast().getClass(), qName);
//                    System.out.println("-----------------------654+54+97474-----------------------654+54+97474-----------------------654+54+97474-----------------------654+54+97474");
//                    System.out.println(objectDeque.getLast().getClass());
//                    collectionMap.putAll(scan.findCollection(objectDeque.getLast().getClass(), qName));
//                    collectionDeque.add(qName);
//
//                        // currentCollection.getLast() = new ArrayList<>(); // TODO переписать, жестко зашито только на ArrayList
//                        if (currentCollection.size() == 0) {
//                            Collection collection = new ArrayList<>();
//                            currentCollection.add(collection);
//                        } else {
//                        Collection collection = currentCollection.getLast();
//                        collection = new ArrayList<>();
//                        currentCollection.removeLast();
//                        currentCollection.add(collection);}
//
//                    System.out.println(collectionMap.size());
//                } else if (currentObject == Object.class) {
//                    objectDeque.add(Scan.createObject(objectDeque.getLast().getClass(), qName));
//                } else {
//                    throw new RuntimeException("startElement - странное значение");
//                }
//            }
//        }
//
//        @Override
//        public void endElement(String uri, String localName, String qName) throws SAXException {
//            log.debug("endElement: " + qName);
//            log.debug("objectDeque: " + objectDeque);
//            if (collectionMap.size() > 0) {
//                if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
//                    System.out.println("Object------------------------------------------Object------------------------------------------Object------------------------------------------Object------------------------------------------");
//
//                    if (objectDeque.getLast().getClass() == String.class) {
//                        if (objectDeque.size() > 1) {
//                            currentCollection.getLast().add(character.toString());
//                            objectDeque.removeLast();
//                        }
//                    } else if (objectDeque.getLast().getClass() == Integer.class) {
//                        try {
//                            currentCollection.getLast().add(collectionMap.get(collectionDeque.getLast()).get(qName).getConstructor(String.class).newInstance(character.toString()));
//                        } catch (Exception e) {
//                            throw new RuntimeException("startElement - странное значение");
//                        }
//                    } else {
//                        currentCollection.getLast().add(objectDeque.getLast());
//                    }
//
//                    System.out.println(currentCollection);
//                    if (objectDeque.size() > 1) {
////
//                        objectDeque.removeLast();
//                    }
//                } else if (collectionMap.get(qName) != null) {
//                    System.out.println("EEEEEEEEEEEEEEEEEEEEND COLLECTION!!!!!!!!!");
//                    System.out.println(objectDeque.getLast());
//                    scan.setFieldValue(objectDeque.getLast(), qName, currentCollection.getLast());
//                    collectionMap.clear();
//                } else if (currentObject.getClass() == Field.class) {
//                    System.out.println("Field------------------------------------------");
//                    System.out.println(currentObject);
//                    scan.setFieldValue(objectDeque.getLast(), (Field) currentObject, character.toString());
//                    Beer beer = (Beer) objectDeque.getLast();
//                    System.out.println(beer.getName());
//                }
//
//
//            } else if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
//                if (objectDeque.size() > 1) {
//                    Object object = objectDeque.getLast();
//                    objectDeque.removeLast();
//                    scan.setFieldValue(objectDeque.getLast(), object);
//                }
//            } else if (currentObject.getClass() == Field.class) {
//                scan.setFieldValue(objectDeque.getLast(), (Field) currentObject, character.toString());
//            }
//        }
//
//        @Override
//        public void characters(char[] ch, int start, int length) throws SAXException {
//            character.setLength(0);
//            character.append(ch, start, length);
//        }
//
//        private String getSimpleName(String value) {
//            return value.substring(value.lastIndexOf(".") + 1, value.length());
//        }
//    }
//}
