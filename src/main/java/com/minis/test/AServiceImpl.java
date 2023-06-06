package com.minis.test;

import lombok.Data;

/**
 * @author yue.zhang5
 * @otherThing BUG FREE PLEASE
 * @time 2023/6/5-11:10
 **/
@Data
public class AServiceImpl implements AService{
    private String name;
    private int level;
    private String property1;
    private String property2;
    public AServiceImpl(){}
    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }
    @Override
    public void helloWorld() {
        System.out.println("hello spring");
    }
}
