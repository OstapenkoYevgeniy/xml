package com.john.parser;

import javax.xml.bind.JAXBException;
import java.io.File;

public interface Parser {
   <T>T parse(File file, Class clazz) throws ParserException;
}
