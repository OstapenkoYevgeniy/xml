package com.john.parser.impl;


import com.john.entity.Beer;
import com.john.parser.Parser;
import com.john.parser.ParserException;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SaxParser implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParser.class);
    private Map<String, MethodHandle> configuration;
    private Map<Class, Function<String, ?>> converter;

    public void configure() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {

        configuration = new HashMap<>();
        converter = new HashMap<>();
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        configuration.put("name", lookup.findVirtual(Beer.class, "setName", MethodType.methodType(void.class, String.class)));
        configuration.put("type", lookup.findVirtual(Beer.class, "setType", MethodType.methodType(void.class, String.class)));
        configuration.put("alcohol", lookup.findVirtual(Beer.class, "setAlcohol", MethodType.methodType(void.class, String.class)));
        configuration.put("manufacturer", lookup.findVirtual(Beer.class, "setManufacturer", MethodType.methodType(void.class, String.class)));
        configuration.put("ingredient", lookup.findVirtual(Beer.class, "addIngredient", MethodType.methodType(void.class, String.class)));
        configuration.put("abv", lookup.findVirtual(Beer.class, "setAbv", MethodType.methodType(void.class, float.class)));
        configuration.put("transparent", lookup.findVirtual(Beer.class, "setTransparent", MethodType.methodType(void.class, int.class)));
        configuration.put("filtered", lookup.findVirtual(Beer.class, "setFiltered", MethodType.methodType(void.class, String.class)));
        configuration.put("nutritionFacts", lookup.findVirtual(Beer.class, "setNutritionFacts", MethodType.methodType(void.class, int.class)));
        configuration.put("material", lookup.findVirtual(Beer.class, "setMaterial", MethodType.methodType(void.class, String.class)));
        configuration.put("volume", lookup.findVirtual(Beer.class, "setVolume", MethodType.methodType(void.class, float.class)));

        converter.put(int.class, s -> Integer.parseInt((String) s));
        converter.put(float.class, s -> Float.parseFloat((String) s));
        converter.put(String.class, String::valueOf);
    }

    public void configure(Map<String, MethodHandle> configuration, Map<Class, Function<String, ?>> converter) {
        this.configuration = configuration;
        this.converter = converter;
    }

    @Override
    public <T> T parse(File file, Class clazz) throws ParserException {
        log.debug("Start SaxParser.parseBeer(File file)");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;

        Handler handler = new Handler(clazz);
        try {
            parser = factory.newSAXParser();
            parser.parse(file, handler);
        } catch (ParserConfigurationException e) {
            log.error("ParserConfigurationException", e);
            throw new ParserException("ParserConfigurationException", e);
        } catch (SAXException e) {
            log.error("SAXException", e);
            throw new ParserException("SAXException", e);
        } catch (IOException e) {
            log.error("IOException", e);
            throw new ParserException("IOException", e);
        }

        return (T) handler.getResult();
    }


    private class Handler extends DefaultHandler {
        private Class clazz;
        private Object obj;
        private StringBuilder charaster;
        private String currentElement;

        public Handler(Class clazz) {
            this.clazz = clazz;
        }

        public Object getResult() {
            return obj;
        }

        @Override
        public void startDocument() throws SAXException {
            log.debug("BeerHandler.startDocument()");
            try {
                obj = clazz.newInstance();

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            charaster = new StringBuilder();
        }

        @Override
        public void endDocument() throws SAXException {
            log.debug("BeerHandler.endDocument()");
            charaster.setLength(0);
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            //log.debug("startElement(" + s + ", " + s1 + ", " + s2 + ", attributes)");
            currentElement = qName;
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            // log.debug("endElement(" + s + ", " + s1 + ", " + s2 + ")");
            currentElement = "";
        }

        @Override
        public void characters(char[] chars, int start, int end) throws SAXException {
            //log.debug("characters(" + new String(chars, start, end) + ", " + start + ", " + end + ")");
            charaster.setLength(0);
            charaster.append(chars, start, end);
            try {
                MethodHandle mh = configuration.get(currentElement);
                if (mh != null) {
                    mh.invoke(obj, converter.get(mh.type().parameterType(1)).apply(charaster.toString()));
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
