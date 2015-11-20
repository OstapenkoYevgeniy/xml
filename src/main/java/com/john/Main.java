package com.john;

import com.john.entity.Order;
import com.john.parser.NewSaxParser;
import com.john.parser.ParserException;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Throwable {
        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("order.xml").getFile());


        Order order = null;
        NewSaxParser saxParser = new NewSaxParser();
        try {
            order = saxParser.parse(file, Order.class);
        } catch (ParserException e) {
            log.error("ParserException", e);
            System.exit(0);
        }

        System.out.println("\n\n\n\n\n\n\n\nMAIN!!!!!!!!!!!!!!!!");

        System.out.println(order.toSourceString());


    }
}

