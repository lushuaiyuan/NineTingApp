package com.zzti.lsy.ninetingapp.home.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * 首页提醒的实体类
 */
public class HomeHintEntity extends BaseEntity {
    private String carNumber;
    private String endDay;
    private String endDate;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
