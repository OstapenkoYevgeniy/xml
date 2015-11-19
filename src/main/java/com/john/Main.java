package com.john;

import com.john.entity.Order;
import com.john.parser.ParserException;

import com.john.parser.SaxParserImpNew;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {


    public static void main(String[] args) throws Throwable {
        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("order.xml").getFile());


        Order order = null;
        SaxParserImpNew saxParser = new SaxParserImpNew();
        try {
            order = saxParser.parse(file, Order.class);
        } catch (ParserException e) {
            log.error("ParserException", e);
            System.exit(0);
        }

        System.out.println("\n\n\n\n\n\n\n\nMAIN!!!!!!!!!!!!!!!!");
        System.out.println(order);
        System.out.println(order.toSourceString());

//        log.info(beer.toSourceString());
    }
}

