package com.john;

import com.john.entity.Order;
import com.john.parser.NewNewSaxParser;
import com.john.parser.ParserException;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Throwable {

        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("order.xml").getFile());

        Order order = null;
        NewNewSaxParser saxParser = new NewNewSaxParser();
        try {
           order = saxParser.parse(file, Order.class);
            //System.out.println(saxParser.parse(file, Order.class));
        } catch (ParserException e) {
            log.error("ParserException", e);
            System.exit(0);
        }

        System.out.println("\n\n\nMAIN!!!!!!!!!!!!!!!!");

      System.out.println(order.toSourceString());


    }
}

