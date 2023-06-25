package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeansException;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-12:03
 **/
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new LinkedList<>();

    public void refresh() {
        for(String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        // 尝试从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if(null == singleton) {
            // 如果没有实例，则尝试从毛坯实例中获取（毛坯实例就是某些引用未初始化的实例， 为了解决循环引用）
            singleton = this.earlySingletonObjects.get(beanName);
            if(singleton == null) {
                // 如果连毛坯都没有，则创建bean并且注册
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                singleton = createBean(beanDefinition);
                this.registerSingleton(beanName, singleton);
            }
        }
        return singleton;
    }

    /**
     * 创建bean
     * @param beanDefinition beanDefinition
     * @return Object
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try{
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // region 处理属性
        handleProperties(beanDefinition, clz, obj);
        // endregion
        return obj;
    }

    /**
     * 处理属性注入， 如果注入的是引用类型， 则直接调用getBean方法再次创建bean
     *
     * @param beanDefinition
     * @param clz
     * @param obj
     */
    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        System.out.println("handle properties for bean:" + beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(!propertyValues.isEmpty()) {
            for(int i = 0; i < propertyValues.size(); i++) {
                // 对每一个属性， 分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String type = propertyValue.getType();
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                Boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class[1];
                Object[] paramValues = new Object[1];
                if(!isRef) {
                    // 不是ref，只是普通属性
                    if("String".equals(type) || "java.lang.String".equals(type)){
                        paramTypes[0] = String.class;
                    }else if("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[0] = Integer.class;
                    }else if("int".equals(type)) {
                        paramTypes[0] = int.class;
                    }else {
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = value;
                }else {
                    // 是引用类型
                    try {
                        paramTypes[0] = Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try{
                        // 再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String) value);
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }

                // 按照setXxxx的规范查找setter方法
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建毛坯实例， 仅仅是调用构造， 没有进行属性处理*
     * @param beanDefinition
     * @return
     */
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if(!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for(int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }else if("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.parseInt((String) constructorArgumentValue.getValue());
                    }else if("int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.parseInt((String) constructorArgumentValue.getValue());
                    }
                }
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            }else {
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
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
