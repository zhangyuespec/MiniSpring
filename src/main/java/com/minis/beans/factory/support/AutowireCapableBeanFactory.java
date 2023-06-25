package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeansException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/25-11:34
 **/
public class AutowireCapableBeanFactory extends AbstractBeanFactory{
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();
    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor) {
        this.beanPostProcessors.remove(autowiredAnnotationBeanPostProcessor);
        this.beanPostProcessors.add(autowiredAnnotationBeanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }
    public List<AutowiredAnnotationBeanPostProcessor> getBeanProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanProcessors()) {
            beanPostProcessor.setBeanFactory(this);
            result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if(result == null) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor beanPostProcessor : getBeanProcessors()) {
            result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if(result == null) {
                return result;
            }
        }
        return result;
    }
}
