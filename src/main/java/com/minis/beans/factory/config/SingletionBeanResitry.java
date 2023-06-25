package com.minis.beans.factory.config;

/**
 * @author yue.zhang5
 * @otherThing bean仓库
 * @time 2023/6/5-12:45
 **/
public interface SingletionBeanResitry {
    void registerSingleton(String beanName, Object singletonObject);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
    String[] getSingletonNames();
}
