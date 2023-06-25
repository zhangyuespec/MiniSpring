package com.minis.beans.factory;

import com.minis.beans.factory.config.BeansException;

/**
 * @author yue.zhang5
 * @otherThing bean工厂
 * @time 2023/6/5-11:26
 **/
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    Boolean containsBean(String namxe);
    Boolean isSingleton(String name);
    Boolean isPrototype(String name);
    Class<?> getType(String name);
}
