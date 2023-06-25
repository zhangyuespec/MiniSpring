package com.minis.test;


import com.minis.beans.factory.config.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-11:11
 **/
public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml", true);
        AService aService = (AService) ctx.getBean("aservice");
        aService.helloWorld();
    }
}
