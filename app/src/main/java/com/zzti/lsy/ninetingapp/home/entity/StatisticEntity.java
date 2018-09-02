package com.zzti.lsy.ninetingapp.home.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * 统计的实体类
 */
public class StatisticEntity extends BaseEntity {
    private String date;
    private String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
