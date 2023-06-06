package com.minis.beans;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/6-12:43
 **/
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}
