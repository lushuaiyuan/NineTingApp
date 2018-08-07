package com.zzti.lsy.ninetingapp.home.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * author：anxin on 2018/8/7 14:55
 * 项目部的名称
 */
public class ProjectAddressEntitiy extends BaseEntity {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
