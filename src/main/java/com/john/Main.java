package com.john;

import com.john.entity.Beer;
import com.john.parser.ParserException;
import com.john.parser.impl.SaxParser;
import org.slf4j.Logger;

import java.io.File;


public class Main {


    public static void main(String[] args) throws Throwable {
        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("beer.xml").getFile());

        Beer beer = null;
        SaxParser saxParser = new SaxParser();
        saxParser.configure();
        try {

            beer = saxParser.parse(file, Beer.class);
        } catch (ParserException e) {
            log.error("ParserException", e);
            System.exit(0);
        }

        log.info(beer.toSourceString());
    }
}

