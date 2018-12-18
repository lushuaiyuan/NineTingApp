package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * @author lsy
 * @create 2018/10/10 23:37
 * @Describe 生产记录统计报表实体类
 */
public class StatisticalList implements Serializable {
    private String slID;//统计编号
    private String slDateTime;///统计日期
    private String plateNumber;///车牌号
    private String projectID;///项目部编号
    private String squareQuantity;///方量
    private String qilWear; ///加油升数
    private String distance;///距离基地
    private String timeConsuming; ///耗时
    private String remark;///备注
    private String userID;///记录人ID
    private String staffName;///经手人姓名
    private String projectName;///项目部名称

    private String eightBelow;//8方以下
    private String sixBelow;//6方以下
    private String addQuantity;//补方
    private String remainder;//剩料
    private String washTime;//洗料耗时
    private String addWorkTime;//加班时长
    private String addWorkCount;//加班趟数

    private String travelKm;//行驶公里数

    public String getTravelKm() {
        return travelKm;
    }

    public void setTravelKm(String travelKm) {
        this.travelKm = travelKm;
    }

    public String getEightBelow() {
        return eightBelow;
    }

    public void setEightBelow(String eightBelow) {
        this.eightBelow = eightBelow;
    }

    public String getSixBelow() {
        return sixBelow;
    }

    public void setSixBelow(String sixBelow) {
        this.sixBelow = sixBelow;
    }

    public String getAddQuantity() {
        return addQuantity;
    }

    public void setAddQuantity(String addQuantity) {
        this.addQuantity = addQuantity;
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public String getWashTime() {
        return washTime;
    }

    public void setWashTime(String washTime) {
        this.washTime = washTime;
    }

    public String getAddWorkTime() {
        return addWorkTime;
    }

    public void setAddWorkTime(String addWorkTime) {
        this.addWorkTime = addWorkTime;
    }

    public String getAddWorkCount() {
        return addWorkCount;
    }

    public void setAddWorkCount(String addWorkCount) {
        this.addWorkCount = addWorkCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSlID() {
        return slID;
    }

    public void setSlID(String slID) {
        this.slID = slID;
    }

    public String getSlDateTime() {
        return slDateTime;
    }

    public void setSlDateTime(String slDateTime) {
        this.slDateTime = slDateTime;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getSquareQuantity() {
        return squareQuantity;
    }

    public void setSquareQuantity(String squareQuantity) {
        this.squareQuantity = squareQuantity;
    }

    public String getQilWear() {
        return qilWear;
    }

    public void setQilWear(String qilWear) {
        this.qilWear = qilWear;
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
}
