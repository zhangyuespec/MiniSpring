package com.minis.beans;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-12:03
 **/
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry{
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new LinkedList<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);
        if(null == singleton) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition == null) {
                throw new BeansException("No Bean");
            }
            try {
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.registerSingleton(beanName, singleton);
        }
        return singleton;
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
    }

    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

    @Override
    public Boolean isSingleton(String name) {
        return beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public Boolean isPrototype(String name) {
        return beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if(!beanDefinition.isLazyInit()) {
            try{
                getBean(name);
            }catch (BeansException e) {
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }
}
