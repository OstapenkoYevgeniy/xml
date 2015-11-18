package com.john.parser;

import com.john.annotation.XmlElement;
import com.john.annotation.XmlNewClass;
import com.john.annotation.XmlRootElement;
import com.john.entity.Customer;
import org.boon.Boon;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SaxParserImpl implements Parser {
    private Logger log = org.slf4j.LoggerFactory.getLogger(SaxParserImpl.class);
    private Map<String, MethodHandle> configuration = new HashMap<>();
    private Map<Class, Function<String, ?>> converter = new HashMap<>();

    private Map<String, Map<String, Method>> config = new HashMap<>();


    public void configure(Class clazz) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        log.info("SaxParser.Configure() start!");

        Map<String, Method> sm = new HashMap<>();

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        Field[] fields = clazz.getDeclaredFields();
        String object = clazz.getSimpleName();
        for (Field field : fields) {
            String simpleName = field.getName();
            Class classType = field.getType();
            if (!classType.getSimpleName().equals("List")) {
                sm.put(simpleName, clazz.getMethod("set" + firstUpper(simpleName), classType));
            } else {
                sm.put(simpleName,clazz.getMethod("add" + firstUpper(simpleName),getGenericType(field.getGenericType())));
            }

            System.out.println(field.getType());
            System.out.println(field.getType().getDeclaredFields());
        }
        config.put(object, sm);
    }

    private Class getGenericType(Type genericType) throws ClassNotFoundException {
        String result = genericType.getTypeName();
        result = result.substring(result.lastIndexOf("<") + 1, result.lastIndexOf(">"));
        return Class.forName(result);
    }

    private String firstUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
    }


    public void showConfigure() {
        System.out.println("SHOW CONFIGURE!!!!!!");
        for (Map.Entry<String, MethodHandle> entry : configuration.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println("--------");
        for (Map.Entry<String, Map<String, Method>> entry : config.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());

        }
    }

    @Override
    public <T> T parse(File file, Class<T> clazz) throws ParserException {
        log.debug("Start SaxParser.parseBeer(File file)");
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

        return (T) handler.obj;
    }

    private class Handler extends DefaultHandler {
        Class clazz;
        Object obj;
        Object curretObject;
        Class curretClass;
        StringBuilder charaster;
        String currentElement;
        String rootElement = "";
        Deque<Object> deque = new ArrayDeque<>();

        public Handler(Class clazz) {
            this.clazz = clazz;
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
            currentElement = "";
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            //log.debug("startElement(" + s + ", " + s1 + ", " + s2 + ", attributes)");
            System.out.println("s|" + namespaceURI + "| |" + localName + "| |" + qName + "|");
            currentElement = qName;

        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            // log.debug("endElement(" + s + ", " + s1 + ", " + s2 + ")");
            // System.out.println("e|" + namespaceURI + "| |" + localName + "| |" + qName + "|");

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

        }

        @Override
        public void characters(char[] chars, int start, int end) throws SAXException {
            //log.debug("characters(" + new String(chars, start, end) + ", " + start + ", " + end + ")");
            charaster.setLength(0);
            charaster.append(chars, start, end);
        }
    }
}
