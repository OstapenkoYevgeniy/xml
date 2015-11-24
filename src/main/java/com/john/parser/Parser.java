package com.john.parser;

import java.io.File;

public interface Parser {
   <T>T parse(File file, Class<T> clazz) throws ParserException;
}
