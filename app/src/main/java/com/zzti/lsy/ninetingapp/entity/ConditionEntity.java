package com.zzti.lsy.ninetingapp.entity;

/**
 * 条件的实体类
 */
public class ConditionEntity {
    private String name;
    private String id;

    public ConditionEntity() {
    }

    public ConditionEntity(String name, String id) {
        this.name = name;
        this.id = id;
    }

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
