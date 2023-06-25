package com.minis.beans.factory.support;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.beans.factory.config.BeansException;

import java.lang.reflect.Field;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/25-10:28
 **/
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor{
    protected AutowireCapableBeanFactory beanFactory;
    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }
    public void setBeanFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.beanFactory = autowireCapableBeanFactory;
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getFields();
        if(fields != null) {
            for(Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if(isAutowired) {
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("Autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
