package com.minis.context;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-13:27
 **/
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
