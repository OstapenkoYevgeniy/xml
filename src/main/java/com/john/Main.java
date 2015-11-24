package com.john;

import com.john.entity.Order;
import com.john.parser.SaxParser;
import com.john.parser.ParserException;
import com.john.touristvoucherentity.TouristVoucher;
import org.slf4j.Logger;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Throwable {
        Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("order.xml").getFile());
        // Other xml
        File fileVoucher = new File(classLoader.getResource("otherXML/tourist-voucher.xml").getFile());

        Order order = null;
        TouristVoucher voucher = null;
        SaxParser saxParser = new SaxParser();

        try {
            order = saxParser.parse(file, Order.class);
            voucher = saxParser.parse(fileVoucher, TouristVoucher.class);
        } catch (ParserException e) {
            log.error("ParserException", e);
        }

        log.info("\n" + order.toSourceString());
        log.info("\n" + voucher.toSourceString());
    }
}

