package com.john;

import com.john.entity.Demo;
import com.john.entity.Order;
import com.john.parser.ParserException;

import com.john.parser.SaxParserImpl;
import org.slf4j.Logger;

import java.io.File;


public class Main {


    public static void main(String[] args) throws Throwable {
        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("order.xml").getFile());



        Order order = null;
        SaxParserImpl saxParser = new SaxParserImpl();
        try {
            order = saxParser.parse(file, Order.class);
        } catch (ParserException e) {
            log.error("ParserException", e);
            System.exit(0);
        }

        System.out.println("EPAAAAAAAAAAAAAA!!!");
        System.out.println(order);
        System.out.println(order.toSourceString());

//        log.info(beer.toSourceString());
    }
}

