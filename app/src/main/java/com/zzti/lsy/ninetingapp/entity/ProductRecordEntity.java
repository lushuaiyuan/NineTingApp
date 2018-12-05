package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * @author lsy
 * @create 2018/12/5 14:28
 * @Describe 生产记录的实体类
 */
public class ProductRecordEntity implements Serializable {

    private String carNumber;//车牌号
    private String time;//时间
    private String productAmount;//生产方量
    private String oilMass;//加油升数
    private String distance;//距离基地数
    private String orilMassRatio;//油耗比
    private String timeConsuming;//耗时
    private String remark;//备注


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getOilMass() {
        return oilMass;
    }

    public void setOilMass(String oilMass) {
        this.oilMass = oilMass;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(String timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrilMassRatio() {
        return orilMassRatio;
    }

    public void setOrilMassRatio(String orilMassRatio) {
        this.orilMassRatio = orilMassRatio;
    }
}
