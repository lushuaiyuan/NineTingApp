package com.zzti.lsy.ninetingapp.entity;

/**
 * @author lsy
 * @create 2018/10/10 23:37
 * @Describe 生产记录统计报表实体类
 */
public class StatisticalList {
    private String slID;
    private String slDateTime;
    private String plateNumber;
    private String projectID;
    private String squareQuantity;
    private String qilWear;
    private String distance;
    private String timeConsuming;
    private String remark;

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
