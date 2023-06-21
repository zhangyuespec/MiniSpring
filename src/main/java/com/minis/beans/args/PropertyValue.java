package com.minis.beans.args;

import lombok.Data;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-13:46
 **/
@Data
public class PropertyValue {
    private final String type;
    private final String name;
    private final Object value;
    private final Boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
