package com.zzti.lsy.ninetingapp.entity;

/**
 * @author lsy
 * @create 2018/12/8 16:27
 * @Describe 外租车的实体类
 */
public class RentCarEntity {
    // 车牌号
    public String plateNumber;
    // 行驶证号
    public String drivingLicenseNumber;
    // 车主名称
    public String ownerName;
    // 租用开始时间
    public String startTime;
    // 租用结束时间
    public String overTime;
    /// 所属项目部
    public String projectID;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
