package com.zzti.lsy.ninetingapp.entity;

/**
 * 保险
 */
public class InsuranceEntity {
    private String insuranceID;//保险编号
    private String insuranceName;//保险名称
    private String insuranceContent;//保险内容

    public String getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(String insuranceID) {
        this.insuranceID = insuranceID;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getInsuranceContent() {
        return insuranceContent;
    }

    public void setInsuranceContent(String insuranceContent) {
        this.insuranceContent = insuranceContent;
    }
}
