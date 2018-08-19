package com.zzti.lsy.ninetingapp.home.entity;


import com.zzti.lsy.ninetingapp.base.BaseEntity;

public class DeviceEntity extends BaseEntity {
    private String carNumber;
    private String carType;
    private String carVin;
    private String carBuyTime;
    private String carBuyMoney;
    private String carState;
    private String projectAddress;
    private String address;

    public String getCarState() {
        return carState;
    }

    public void setCarState(String carState) {
        this.carState = carState;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getCarBuyTime() {
        return carBuyTime;
    }

    public void setCarBuyTime(String carBuyTime) {
        this.carBuyTime = carBuyTime;
    }

    public String getCarBuyMoney() {
        return carBuyMoney;
    }

    public void setCarBuyMoney(String carBuyMoney) {
        this.carBuyMoney = carBuyMoney;
    }
}
