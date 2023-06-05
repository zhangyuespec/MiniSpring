package com.minis.context;

import java.util.EventObject;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-13:28
 **/
public class ApplicationEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
