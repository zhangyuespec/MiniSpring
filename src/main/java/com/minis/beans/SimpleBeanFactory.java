package com.minis.beans;

import com.minis.beans.args.ArgumentValue;
import com.minis.beans.args.ArgumentValues;
import com.minis.beans.args.PropertyValue;
import com.minis.beans.args.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
            singleton = createBean(beanDefinition);
            this.registerSingleton(beanName, singleton);
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
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            // region 处理构造器
            ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            // 如果有参数
            if(!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for(int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if("String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }else if("Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    }else if("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String)argumentValue.getValue());
                    }else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                obj = clz.newInstance();
            }
            // endregion
        } catch (Exception e) {
            e.printStackTrace();
        }
        // region 处理属性
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(!propertyValues.isEmpty()) {
            for(int i = 0; i < propertyValues.size(); i++) {
                // 对每一个属性， 分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String type = propertyValue.getType();
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                Class<?>[] paramTypes = new Class[1];
                if("String".equals(type) || "java.lang.String".equals(type)){
                    paramTypes[0] = String.class;
                }else if("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                    paramTypes[0] = Integer.class;
                }else if("int".equals(type)) {
                    paramTypes[0] = int.class;
                }else {
                    paramTypes[0] = String.class;
                }
                Object[] paramValues = new Object[1];
                paramValues[0] = value;

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
        // endregion
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
