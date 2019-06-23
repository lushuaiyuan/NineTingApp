package com.zzti.lsy.ninetingapp.entity;


import java.io.Serializable;

public class CarInfoEntity implements Serializable {
    private String plateNumber;//车牌号
    private String CarTypeID;//类型id
    private String dischargeID;//排放量id
    private String CarSource;//车辆来源
    private String oldLevel;//新旧程度
    private String drivingStatus;//行驶证保存情况
    private String VIN;//识别码
    private String engineNumber;//发动机编号
    private String DLDate;//行驶证发放日期
    private String deviceUse;//设备所属
    private String yearTime;//年检日期
    private String projectID;//车辆所属项目部ID
    private String yearExprie;//年检时限
    private String qStartTime;//强制保险生效时间
    private String qOverTime;//强制保险到期时间
    private String qCompany;//强制保险公司
    private String qAddress;//强险保单原件所在地
    private String sStartTime;//商业保险生效日期
    private String sOverTime;//商业保险到期日期
    private String sCompany;//商业保险公司
    private String sAddress;//商险保单原件所在地
    private String dischargeName;//排放量名称
    private String vehicleTypeName;//汽车类型
    private String projectName;//车辆所属项目部名称
    private String qName;//强险保险人
    private String sName;//商险保险人

    public String getqName() {
        return qName;
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getCarSource() {
        return CarSource;
    }

    public void setCarSource(String carSource) {
        CarSource = carSource;
    }

    public String getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(String oldLevel) {
        this.oldLevel = oldLevel;
    }

    public String getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(String drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public String getDeviceUse() {
        return deviceUse;
    }

    public void setDeviceUse(String deviceUse) {
        this.deviceUse = deviceUse;
    }

    public String getYearTime() {
        return yearTime;
    }

    public void setYearTime(String yearTime) {
        this.yearTime = yearTime;
    }

    public String getYearExprie() {
        return yearExprie;
    }

    public void setYearExprie(String yearExprie) {
        this.yearExprie = yearExprie;
    }

    public String getqStartTime() {
        return qStartTime;
    }

    public void setqStartTime(String qStartTime) {
        this.qStartTime = qStartTime;
    }

    public String getqOverTime() {
        return qOverTime;
    }

    public void setqOverTime(String qOverTime) {
        this.qOverTime = qOverTime;
    }

    public String getqCompany() {
        return qCompany;
    }

    public void setqCompany(String qCompany) {
        this.qCompany = qCompany;
    }

    public String getqAddress() {
        return qAddress;
    }

    public void setqAddress(String qAddress) {
        this.qAddress = qAddress;
    }

    public String getsStartTime() {
        return sStartTime;
    }

    public void setsStartTime(String sStartTime) {
        this.sStartTime = sStartTime;
    }

    public String getsOverTime() {
        return sOverTime;
    }

    public void setsOverTime(String sOverTime) {
        this.sOverTime = sOverTime;
    }

    public String getsCompany() {
        return sCompany;
    }

    public void setsCompany(String sCompany) {
        this.sCompany = sCompany;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    //    private String purchaseDate;//购买时间
//    private String price;//购买金额
//    private String drivingLicenseNumber;//行驶证号
//    private String DLValidDate;//行驶证有效期
//    private String IPDate;//保险购买日期
//    private String IEDate;//保险到期日期
//    private String AVEDate;//年审到期时间
//    private String status;//车辆状态
//    private String location;//车辆位置
//    private String insuranceID;//保险类型ID
//    private String insuranceName;//保险类型名称
//    private String FactoryID;//生产厂家编号
//    private String FactoryName;//生产厂家名称


    private boolean isDefault;//是否是对比的基准
    private boolean isCheck;//是否是选中的要对比的对象


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

//    public String getInsuranceName() {
//        return insuranceName;
//    }
//
//    public void setInsuranceName(String insuranceName) {
//        this.insuranceName = insuranceName;
//    }
//
//    public String getFactoryName() {
//        return FactoryName;
//    }
//
//    public void setFactoryName(String factoryName) {
//        FactoryName = factoryName;
//    }

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

//    public String getIPDate() {
//        return IPDate;
//    }
//
//    public void setIPDate(String IPDate) {
//        this.IPDate = IPDate;
//    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }


    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }


    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

//    public String getPurchaseDate() {
//        return purchaseDate;
//    }
//
//    public void setPurchaseDate(String purchaseDate) {
//        this.purchaseDate = purchaseDate;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }

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

//    public String getDrivingLicenseNumber() {
//        return drivingLicenseNumber;
//    }
//
//    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
//        this.drivingLicenseNumber = drivingLicenseNumber;
//    }

    public String getDLDate() {
        return DLDate;
    }

    public void setDLDate(String DLDate) {
        this.DLDate = DLDate;
    }

//    public String getDLValidDate() {
//        return DLValidDate;
//    }

//    public void setDLValidDate(String DLValidDate) {
//        this.DLValidDate = DLValidDate;
//    }

//    public String getIEDate() {
//        return IEDate;
//    }

//    public void setIEDate(String IEDate) {
//        this.IEDate = IEDate;
//    }
//
//    public String getAVEDate() {
//        return AVEDate;
//    }

//    public void setAVEDate(String AVEDate) {
//        this.AVEDate = AVEDate;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

//    public String getInsuranceID() {
//        return insuranceID;
//    }
//
//    public void setInsuranceID(String insuranceID) {
//        this.insuranceID = insuranceID;
//    }
//
//    public String getFactoryID() {
//        return FactoryID;
//    }

//    public void setFactoryID(String factoryID) {
//        FactoryID = factoryID;
//    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
