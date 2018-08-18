package com.zzti.lsy.ninetingapp.home.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * 维休设备的实体类
 */
public class CarMaintenanceEntity extends BaseEntity {
    private String reason;//维修原因
    private String partsName;//配件名称
    private String money;//金额
    private String partsAmount;//配件数量

    private String carNumber;//车牌号
    private String plantTime;//计划维修时间
    private String state;//维修申请状态

    public String getPlantTime() {
        return plantTime;
    }

    public void setPlantTime(String plantTime) {
        this.plantTime = plantTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPartsAmount() {
        return partsAmount;
    }

    public void setPartsAmount(String partsAmount) {
        this.partsAmount = partsAmount;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
