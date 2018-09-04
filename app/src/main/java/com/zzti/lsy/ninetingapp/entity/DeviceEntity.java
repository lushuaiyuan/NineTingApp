package com.zzti.lsy.ninetingapp.entity;


import com.zzti.lsy.ninetingapp.base.BaseEntity;

public class DeviceEntity extends BaseEntity {
    private String carNumber;//车牌号
    private String carType;//车辆类型
    private String carVin;//识别码
    private String carBuyTime;//购买时间
    private String carBuyMoney;//购买金额
    private String carState;//车辆状态
    private String projectAddress;//项目部
    private String address;//存放地
    private String timeNs;//年审日期
    private String timeBx;//保险购买日期
    private String engineNumber;//发动机编号
    private String inDestination;//出库目的地
    private String inReason;//入库原因
    private String outDestination;//出库目的地
    private String outReason;//出库原因
    private String inNum;//入库数量
    private String outNum;//出库数量

    public String getInNum() {
        return inNum;
    }

    public void setInNum(String inNum) {
        this.inNum = inNum;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public String getInDestination() {
        return inDestination;
    }

    public void setInDestination(String inDestination) {
        this.inDestination = inDestination;
    }

    public String getInReason() {
        return inReason;
    }

    public void setInReason(String inReason) {
        this.inReason = inReason;
    }

    public String getOutDestination() {
        return outDestination;
    }

    public void setOutDestination(String outDestination) {
        this.outDestination = outDestination;
    }

    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
    }

    public String getTimeNs() {
        return timeNs;
    }

    public void setTimeNs(String timeNs) {
        this.timeNs = timeNs;
    }

    public String getTimeBx() {
        return timeBx;
    }

    public void setTimeBx(String timeBx) {
        this.timeBx = timeBx;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

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
