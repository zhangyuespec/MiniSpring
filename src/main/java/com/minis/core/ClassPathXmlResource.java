package com.minis.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-11:49
 **/
public class ClassPathXmlResource implements Resource{
    Document document;
    Element rootElement;
    Iterator<Element> elementIterable;

    public ClassPathXmlResource(String fileName) {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        try{
            this.document = saxReader.read(xmlPath);
            this.rootElement = document.getRootElement();
            this.elementIterable = this.rootElement.elementIterator();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        return this.elementIterable.hasNext();
    }
    @Override
    public Object next() {
        return elementIterable.next();
    }
}
