package com.zzti.lsy.ninetingapp.home.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * author：anxin on 2018/8/9 10:27
 * 配件的实体类
 */
public class PartsEntity extends BaseEntity {
    private String name;
    private String state;
    private String model;
    private String num;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
