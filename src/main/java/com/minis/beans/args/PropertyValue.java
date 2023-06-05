package com.minis.beans.args;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-13:46
 **/
public class PropertyValue {
    private final String name;
    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
