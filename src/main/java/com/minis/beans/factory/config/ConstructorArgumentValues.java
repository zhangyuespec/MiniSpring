package com.minis.beans.factory.config;

import java.util.*;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-13:52
 **/


public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<>();
    public ConstructorArgumentValues() {
    }
    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.constructorArgumentValueList.add(constructorArgumentValue);
    }
    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        ConstructorArgumentValue constructorArgumentValue = this.constructorArgumentValueList.get(index);
        return constructorArgumentValue;
    }
    public int getArgumentCount() {
        return (this.constructorArgumentValueList.size());
    }
    public boolean isEmpty() {
        return (this.constructorArgumentValueList.isEmpty());
    }
}