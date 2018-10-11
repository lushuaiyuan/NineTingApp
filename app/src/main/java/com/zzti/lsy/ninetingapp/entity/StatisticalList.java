package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * @author lsy
 * @create 2018/10/10 23:37
 * @Describe 生产记录统计报表实体类
 */
public class StatisticalList implements Serializable{
    private String slID;//统计编号
    private String slDateTime;///统计日期
    private String plateNumber;///车牌号
    private String projectID;///项目部编号
    private String squareQuantity;///方量
    private String qilWear; ///油耗
    private String distance;///距离基地
    private String timeConsuming; ///耗时
    private String remark;///备注
    private String userID;///记录人ID
    private String staffName;///经手人姓名
    private String projectName;///项目部名称

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
