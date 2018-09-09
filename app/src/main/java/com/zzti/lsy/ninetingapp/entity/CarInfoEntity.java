package com.zzti.lsy.ninetingapp.entity;


import java.io.Serializable;

public class CarInfoEntity implements Serializable {
    private String plateNumber;//车牌号
    private String CarTypeID;//类型id
    private String vehicleTypeName;//类型名称

    private String dischargeID;//排放量id
    private String dischargeName;//排放量名称
    private String VIN;//识别码
    private String engineNumber;//发动机编号
    private String purchaseDate;//购买时间
    private String price;//购买金额
    private String drivingLicenseNumber;//行驶证号
    private String DLDate;//行驶证发放日期
    private String DLValidDate;//行驶证有效期
    private String IPDate;//保险购买日期
    private String IEDate;//保险到期日期
    private String AVEDate;//年审到期时间
    private String status;//车辆状态
    private String location;//车辆位置
    private String insuranceID;//保险类型ID
    private String insuranceName;//保险类型名称
    private String FactoryID;//生产厂家编号
    private String FactoryName;//生产厂家名称
    private String projectID;//车辆所属项目部ID
    private String projectName;//车辆所属项目部名称


    private boolean isDefault;//是否是对比的基准
    private boolean isCheck;//是否是选中的要对比的对象

//    private String carType;//车辆类型
//    private String carState;//车辆状态
//    private String projectAddress;//项目部
//    private String address;//存放地
//    private String inDestination;//出库目的地
//    private String inReason;//入库原因
//    private String outDestination;//出库目的地
//    private String outReason;//出库原因
//    private String inNum;//入库数量
//    private String outNum;//出库数量
//    private String reason;//维修原因
//    private String parts;//维修配件


    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getDischargeName() {
        return dischargeName;
    }

    public void setDischargeName(String dischargeName) {
        this.dischargeName = dischargeName;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getFactoryName() {
        return FactoryName;
    }

    public void setFactoryName(String factoryName) {
        FactoryName = factoryName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

//    public String getParts() {
//        return parts;
//    }
//
//    public void setParts(String parts) {
//        this.parts = parts;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
//
//    public String getInNum() {
//        return inNum;
//    }
//
//    public void setInNum(String inNum) {
//        this.inNum = inNum;
//    }
//
//    public String getOutNum() {
//        return outNum;
//    }
//
//    public void setOutNum(String outNum) {
//        this.outNum = outNum;
//    }
//
//    public String getInDestination() {
//        return inDestination;
//    }
//
//    public void setInDestination(String inDestination) {
//        this.inDestination = inDestination;
//    }
//
//    public String getInReason() {
//        return inReason;
//    }
//
//    public void setInReason(String inReason) {
//        this.inReason = inReason;
//    }
//
//    public String getOutDestination() {
//        return outDestination;
//    }
//
//    public void setOutDestination(String outDestination) {
//        this.outDestination = outDestination;
//    }
//
//    public String getOutReason() {
//        return outReason;
//    }
//
//    public void setOutReason(String outReason) {
//        this.outReason = outReason;
//    }

    public String getIPDate() {
        return IPDate;
    }

    public void setIPDate(String IPDate) {
        this.IPDate = IPDate;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

//    public String getCarState() {
//        return carState;
//    }
//
//    public void setCarState(String carState) {
//        this.carState = carState;
//    }
//
//    public String getProjectAddress() {
//        return projectAddress;
//    }
//
//    public void setProjectAddress(String projectAddress) {
//        this.projectAddress = projectAddress;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

//    public String getCarType() {
//        return carType;
//    }
//
//    public void setCarType(String carType) {
//        this.carType = carType;
//    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarTypeID() {
        return CarTypeID;
    }

    public void setCarTypeID(String carTypeID) {
        CarTypeID = carTypeID;
    }

    public String getDischargeID() {
        return dischargeID;
    }

    public void setDischargeID(String dischargeID) {
        this.dischargeID = dischargeID;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getDLDate() {
        return DLDate;
    }

    public void setDLDate(String DLDate) {
        this.DLDate = DLDate;
    }

    public String getDLValidDate() {
        return DLValidDate;
    }

    public void setDLValidDate(String DLValidDate) {
        this.DLValidDate = DLValidDate;
    }

    public String getIEDate() {
        return IEDate;
    }

    public void setIEDate(String IEDate) {
        this.IEDate = IEDate;
    }

    public String getAVEDate() {
        return AVEDate;
    }

    public void setAVEDate(String AVEDate) {
        this.AVEDate = AVEDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(String insuranceID) {
        this.insuranceID = insuranceID;
    }

    public String getFactoryID() {
        return FactoryID;
    }

    public void setFactoryID(String factoryID) {
        FactoryID = factoryID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
