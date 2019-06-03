package com.zzti.lsy.ninetingapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author lsy
 * @create 2018/10/10 23:37
 * @Describe 生产记录统计报表实体类
 */
public class StatisticalList implements Parcelable {
    private String slID;//统计编号  共有
    private String slDateTime;///日期  共有
    private String plateNumber;///车牌号  共有
    private String CarTypeID;//车辆类型ID  共有
    private String workSite;// 罐车：工程名称 泵车：施工地点
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
    public String siteNumber;// 站内编号 罐车
    private String produce;//生产厂家 罐车
    public String requester;// 委托单位 罐车
    private String distance;//运距
    private String price;// 罐车：单价 泵车：泵送单价
    private String squareQuantity;///方量 罐车
    private String eightBelow;//8方以下 罐车
    private String eightBelowtime;//8方以下车次 罐车
    private String additionQuantity;//补方  罐车
    private String remainder;//剩料  罐车
    private String washing;//洗料  罐车
    private String water;//水  罐车

    private String inOrderqua;//泵送方量 泵车
    private String acountQua;//结算方量 泵车
    private String inOrderpriceCount;// 泵车：泵送金额 结算方量*泵送单价
    private String onOrderqua;//外单方量 泵车 如果填写外单方量 那么外单单价必填
    private String onOrderprice;//外单单价 泵车
    private String onOrderpriceCount;//外单金额 泵车
    private String pactID;//泵车 外单合同ID 如果外单方量填写 那么必选 否则置灰不选
    private String pactName;//外单合同名称 返回值
    private String quantityCount;//合计方量 共有 罐车：用户填写 泵车：外单方量+外单方量

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getAdditionQuantity() {
        return additionQuantity;
    }

    public void setAdditionQuantity(String additionQuantity) {
        this.additionQuantity = additionQuantity;
    }

    public String getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getEightBelowtime() {
        return eightBelowtime;
    }

    public void setEightBelowtime(String eightBelowtime) {
        this.eightBelowtime = eightBelowtime;
    }

    public String getAcountQua() {
        return acountQua;
    }

    public void setAcountQua(String acountQua) {
        this.acountQua = acountQua;
    }

    public String getInOrderpriceCount() {
        return inOrderpriceCount;
    }

    public void setInOrderpriceCount(String inOrderpriceCount) {
        this.inOrderpriceCount = inOrderpriceCount;
    }

    public String getPactID() {
        return pactID;
    }

    public void setPactID(String pactID) {
        this.pactID = pactID;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.slID);
        dest.writeString(this.slDateTime);
        dest.writeString(this.plateNumber);
        dest.writeString(this.CarTypeID);
        dest.writeString(this.workSite);
        dest.writeString(this.produce);
        dest.writeString(this.workPart);
        dest.writeString(this.projectID);
        dest.writeString(this.qilWear);
        dest.writeString(this.wearPrice);
        dest.writeString(this.wearCount);
        dest.writeString(this.remark);
        dest.writeString(this.userID);
        dest.writeString(this.staffName);
        dest.writeString(this.projectName);
        dest.writeString(this.vehicleTypeName);
        dest.writeString(this.wearUser);
        dest.writeString(this.workTimes);
        dest.writeString(this.siteNumber);
        dest.writeString(this.requester);
        dest.writeString(this.distance);
        dest.writeString(this.price);
        dest.writeString(this.squareQuantity);
        dest.writeString(this.eightBelow);
        dest.writeString(this.eightBelowtime);
        dest.writeString(this.additionQuantity);
        dest.writeString(this.remainder);
        dest.writeString(this.washing);
        dest.writeString(this.water);
        dest.writeString(this.inOrderqua);
        dest.writeString(this.acountQua);
        dest.writeString(this.inOrderpriceCount);
        dest.writeString(this.onOrderqua);
        dest.writeString(this.onOrderprice);
        dest.writeString(this.onOrderpriceCount);
        dest.writeString(this.pactID);
        dest.writeString(this.pactName);
        dest.writeString(this.quantityCount);
    }

    public StatisticalList() {
    }

    protected StatisticalList(Parcel in) {
        this.slID = in.readString();
        this.slDateTime = in.readString();
        this.plateNumber = in.readString();
        this.CarTypeID = in.readString();
        this.workSite = in.readString();
        this.workPart = in.readString();
        this.projectID = in.readString();
        this.qilWear = in.readString();
        this.wearPrice = in.readString();
        this.wearCount = in.readString();
        this.remark = in.readString();
        this.userID = in.readString();
        this.staffName = in.readString();
        this.produce = in.readString();
        this.projectName = in.readString();
        this.vehicleTypeName = in.readString();
        this.wearUser = in.readString();
        this.workTimes = in.readString();
        this.siteNumber = in.readString();
        this.requester = in.readString();
        this.distance = in.readString();
        this.price = in.readString();
        this.squareQuantity = in.readString();
        this.eightBelow = in.readString();
        this.eightBelowtime = in.readString();
        this.additionQuantity = in.readString();
        this.remainder = in.readString();
        this.washing = in.readString();
        this.water = in.readString();
        this.inOrderqua = in.readString();
        this.acountQua = in.readString();
        this.inOrderpriceCount = in.readString();
        this.onOrderqua = in.readString();
        this.onOrderprice = in.readString();
        this.onOrderpriceCount = in.readString();
        this.pactID = in.readString();
        this.pactName = in.readString();
        this.quantityCount = in.readString();
    }

    public static final Parcelable.Creator<StatisticalList> CREATOR = new Parcelable.Creator<StatisticalList>() {
        @Override
        public StatisticalList createFromParcel(Parcel source) {
            return new StatisticalList(source);
        }

        @Override
        public StatisticalList[] newArray(int size) {
            return new StatisticalList[size];
        }
    };
}
