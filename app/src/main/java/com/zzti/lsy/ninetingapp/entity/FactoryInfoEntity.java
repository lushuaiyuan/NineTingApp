package com.zzti.lsy.ninetingapp.entity;

/**
 * 生产厂家
 */
public class FactoryInfoEntity {
    private String FactoryID;//生产厂家编号
    private String FactoryName;//生产厂家名
    private String FactoryAddress;//地址
    private String FactoryPhoneNumber;//电话
    private String FactoryType;//厂家类型

    public String getFactoryID() {
        return FactoryID;
    }

    public void setFactoryID(String factoryID) {
        FactoryID = factoryID;
    }

    public String getFactoryName() {
        return FactoryName;
    }

    public void setFactoryName(String factoryName) {
        FactoryName = factoryName;
    }

    public String getFactoryAddress() {
        return FactoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        FactoryAddress = factoryAddress;
    }

    public String getFactoryPhoneNumber() {
        return FactoryPhoneNumber;
    }

    public void setFactoryPhoneNumber(String factoryPhoneNumber) {
        FactoryPhoneNumber = factoryPhoneNumber;
    }

    public String getFactoryType() {
        return FactoryType;
    }

    public void setFactoryType(String factoryType) {
        FactoryType = factoryType;
    }
}
