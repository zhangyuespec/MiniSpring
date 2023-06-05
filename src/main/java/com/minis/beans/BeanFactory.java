package com.minis.beans;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @author yue.zhang5
 * @otherThing bean工厂
 * @time 2023/6/5-11:26
 **/
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBean(String beanName, Object Obj);
    Boolean containsBean(String name);
}
