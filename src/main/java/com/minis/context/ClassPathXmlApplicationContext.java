package com.minis.context;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeansException;
import com.minis.beans.factory.support.AutowireCapableBeanFactory;
import com.minis.beans.factory.support.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.SimpleBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

import java.util.List;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-12:10
 **/
public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher {
    private final AutowireCapableBeanFactory beanFactory;
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if(isRefresh) {
            refresh();
        }
    }
    public List<AutowiredAnnotationBeanPostProcessor> getBeanFactoryPostProcessors() {
        return this.getBeanFactoryPostProcessors();
    }
    public void addBeanFactoryPostProcessor(AutowiredAnnotationBeanPostProcessor postProcessor) {
        this.addBeanFactoryPostProcessor(postProcessor);
    }

    public void refresh() {
        registerBeanPostProcessors(this.beanFactory);
        onRefresh();
    }
    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }
    private void onRefresh() {
        this.beanFactory.refresh();
    }
    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public Boolean isSingleton(String name) {
        return null;
    }

    @Override
    public Boolean isPrototype(String name) {
        return null;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
