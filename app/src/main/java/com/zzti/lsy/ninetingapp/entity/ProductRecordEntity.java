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
    private String eight;//8方以下
    private String six;//6方以下
    private String remainMaterial;//剩料
    private String washMaterial;//洗料耗时
    private String overTime;//加班时长
    private String overTimeTrainNumber;//加班趟数
    private String timeConsuming;//耗时
    private String remark;//备注

    public String getEight() {
        return eight;
    }

    public void setEight(String eight) {
        this.eight = eight;
    }

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getRemainMaterial() {
        return remainMaterial;
    }

    public void setRemainMaterial(String remainMaterial) {
        this.remainMaterial = remainMaterial;
    }

    public String getWashMaterial() {
        return washMaterial;
    }

    public void setWashMaterial(String washMaterial) {
        this.washMaterial = washMaterial;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getOverTimeTrainNumber() {
        return overTimeTrainNumber;
    }

    public void setOverTimeTrainNumber(String overTimeTrainNumber) {
        this.overTimeTrainNumber = overTimeTrainNumber;
    }

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
