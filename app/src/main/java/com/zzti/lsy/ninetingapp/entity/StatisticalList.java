package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * @author lsy
 * @create 2018/10/10 23:37
 * @Describe 生产记录统计报表实体类
 */
public class StatisticalList implements Serializable {
    private String slID;//统计编号  共有
    private String slDateTime;///日期  共有
    private String plateNumber;///车牌号  共有
    private String CarTypeID;//车辆类型ID  共有
    private String workSite;//施工地点 填写  共有
    private String workPart;//施工部位 填写  共有
    private String projectID;///项目部编号  共有

    private String qilWear; ///加油升数 共有
    private String wearPrice;//油价 共有
    private String wearCount;//加油金额 共有


    private String remark;///备注 共有
    private String userID;///记录人ID 共有
    private String staffName;///经手人姓名 共有
    private String projectName;///项目部名称 共有
    private String vehicleTypeName;//车辆类型 共有
    private String wearUser;//加油负责人 共有

    private String workTimes;//车次 罐车
    private String Intwenty;//20公里以内 罐车
    private String Onforty;//20公里至40公里 罐车
    private String Ontwenty;//40公里以上 罐车
    private String price;//单价 罐车
    private String squareQuantity;///方量 罐车
    private String eightBelow;//8方以下 罐车
    private String sixBelow;//6方以下 罐车
    private String remainder;//剩料  罐车
    private String washing;//洗料  罐车
    private String water;//水  罐车

    private String inOrderqua;//内单方量 泵车
    private String onOrderqua;//外单方量 泵车 如果填写外单方量 那么外单单价必填
    private String onOrderprice;//外单单价 泵车
    private String onOrderpriceCount;//外单金额 泵车

    private String quantityCount;//合计方量 共有 罐车：用户填写 泵车：外单方量+内单方量

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

    public String getCarTypeID() {
        return CarTypeID;
    }

    public void setCarTypeID(String carTypeID) {
        CarTypeID = carTypeID;
    }

    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }

    public String getWorkPart() {
        return workPart;
    }

    public void setWorkPart(String workPart) {
        this.workPart = workPart;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getQilWear() {
        return qilWear;
    }

    public void setQilWear(String qilWear) {
        this.qilWear = qilWear;
    }

    public String getWearPrice() {
        return wearPrice;
    }

    public void setWearPrice(String wearPrice) {
        this.wearPrice = wearPrice;
    }

    public String getWearCount() {
        return wearCount;
    }

    public void setWearCount(String wearCount) {
        this.wearCount = wearCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getWearUser() {
        return wearUser;
    }

    public void setWearUser(String wearUser) {
        this.wearUser = wearUser;
    }

    public String getWorkTimes() {
        return workTimes;
    }

    public void setWorkTimes(String workTimes) {
        this.workTimes = workTimes;
    }

    public String getIntwenty() {
        return Intwenty;
    }

    public void setIntwenty(String intwenty) {
        Intwenty = intwenty;
    }

    public String getOnforty() {
        return Onforty;
    }

    public void setOnforty(String onforty) {
        Onforty = onforty;
    }

    public String getOntwenty() {
        return Ontwenty;
    }

    public void setOntwenty(String ontwenty) {
        Ontwenty = ontwenty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSquareQuantity() {
        return squareQuantity;
    }

    public void setSquareQuantity(String squareQuantity) {
        this.squareQuantity = squareQuantity;
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

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public String getWashing() {
        return washing;
    }

    public void setWashing(String washing) {
        this.washing = washing;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getInOrderqua() {
        return inOrderqua;
    }

    public void setInOrderqua(String inOrderqua) {
        this.inOrderqua = inOrderqua;
    }

    public String getOnOrderqua() {
        return onOrderqua;
    }

    public void setOnOrderqua(String onOrderqua) {
        this.onOrderqua = onOrderqua;
    }

    public String getOnOrderprice() {
        return onOrderprice;
    }

    public void setOnOrderprice(String onOrderprice) {
        this.onOrderprice = onOrderprice;
    }

    public String getOnOrderpriceCount() {
        return onOrderpriceCount;
    }

    public void setOnOrderpriceCount(String onOrderpriceCount) {
        this.onOrderpriceCount = onOrderpriceCount;
    }

    public String getQuantityCount() {
        return quantityCount;
    }

    public void setQuantityCount(String quantityCount) {
        this.quantityCount = quantityCount;
    }
}
