package com.john.parser;

import com.john.classtools.ClassTools;
import com.john.classtools.ClassToolsException;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;

public class SaxParser implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParser.class);

    @Override
    public <T> T parse(File file, Class<T> clazz) throws ParserException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;

        Handler handler = new Handler(clazz);

        try {
            parser = factory.newSAXParser();
            parser.parse(file, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("SaxParserException", e);
            throw new ParserException(e);
        }

        // TODO Вопрос! Почему IDEA подвечивает эту строку? Как это исправить?
        return (T) handler.objectDeque.getFirst();
    }

    private class Handler extends DefaultHandler {
        ClassTools classTools = new ClassTools();
        StringBuilder character = new StringBuilder();
        Deque<Object> objectDeque = new ArrayDeque<>();
        Object currentObject;

        public Handler(Class clazz) throws ParserException {
            try {
                objectDeque.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("SaxParserException", e);
                throw new ParserException(e);
            }

        }

        private Object returnCurrentObject(Object obj) throws ParserException {
            if (obj instanceof Field) {
                return obj;
            } else {
                objectDeque.add(obj);
                return obj;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            log.debug("startElement - " + qName);
            // Unless the root element
            if (!objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (objectDeque.getLast() instanceof CollectionType) {
                    CollectionType collectionType = (CollectionType) objectDeque.getLast();
                    // If the item collection
                    if (collectionType.getCollectionItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                        try {
                            collectionType.addItemToDeque(collectionType.getCollectionItem().getClass().newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            log.error("SaxParserException", e);
                            throw new SAXException(e);
                        }
                        // Otherwise, this field is an object item in the collection
                    } else {
                        Object obj;

                        try {
                            obj = classTools.find(collectionType.getLastDequeItem().getClass(), qName);
                        } catch (ClassToolsException e) {
                            log.error("SaxParserException", e);
                            throw new SAXException(e);
                        }

                        try {
                            currentObject = returnCurrentObject(obj);
                        } catch (ParserException e) {
                            log.error("SaxParserException", e);
                            throw new SAXException(e);
                        }
                    }
                } else {
                    Object obj;

                    try {
                        obj = classTools.find(objectDeque.getLast().getClass(), qName);
                    } catch (ClassToolsException e) {
                        log.error("SaxParserException", e);
                        throw new SAXException(e);
                    }

                    try {
                        currentObject = returnCurrentObject(obj);
                    } catch (ParserException e) {
                        log.error("SaxParserException", e);
                        throw new SAXException(e);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            log.debug("endElement - " + qName);
            if (objectDeque.getLast() instanceof CollectionType) {
                try {
                    collectionTypeProcess(qName);
                } catch (ClassToolsException e) {
                    log.error("SaxParserException", e);
                    throw new SAXException(e);
                }
                // If the end of the collection
            } else if (objectDeque.getLast().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (objectDeque.size() > 1) {
                    Object resultObject = objectDeque.getLast();
                    objectDeque.removeLast();

                    if (objectDeque.getLast() instanceof CollectionType) {
                         CollectionType collectionType = (CollectionType) objectDeque.getLast();
                        try {
                            classTools.setField(collectionType.getLastDequeItem(), qName, resultObject);
                        } catch (ClassToolsException e) {
                            log.error("SaxParserException", e);
                            throw new SAXException(e);
                        }
                    } else {
                        try {
                            classTools.setField(objectDeque.getLast(), qName, resultObject);
                        } catch (ClassToolsException e) {
                            log.error("SaxParserException", e);
                            throw new SAXException(e);
                        }
                    }
                }
            } else {
                try {
                    classTools.setField(objectDeque.getLast(), qName, character.toString());
                } catch (ClassToolsException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            character.setLength(0);
            character.append(ch, start, length);
        }

        private void collectionTypeProcess(String qName) throws ClassToolsException {
            CollectionType collectionType = (CollectionType) objectDeque.getLast();
            // If the last element of the object in the collection
            if (collectionType.getLastDequeItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                if (qName.equals("string")) {
                    collectionType.addValueInCollection(character.toString());
                } else if (qName.equals("integer")) {
                    throw new UnsupportedOperationException("Unsupported Integer");
                } else if (collectionType.getCollectionItem().getClass().getSimpleName().toLowerCase().equals(qName)) {
                    collectionType.addValueInCollection();
                }
                // If the end of the collection
            } else if (collectionType.getFieldName().equals(qName)) {
                Object resultCollection = collectionType.getType();
                objectDeque.removeLast();

                if (objectDeque.getLast() instanceof CollectionType) {
                    CollectionType colType = (CollectionType) objectDeque.getLast();
                    classTools.setField(colType.getLastDequeItem(), qName, resultCollection);
                } else {
                    classTools.setField(objectDeque.getLast(), qName, resultCollection);
                }
            } else {
                if (currentObject instanceof Field) {
                    classTools.setField(collectionType.getLastDequeItem(), qName, character.toString());
                    currentObject = null;
                }
            }
        }
    }
}
