package com.zzti.lsy.ninetingapp.entity;

/**
 * 排放标准
 */
public class DisChargeEntity {
    private String dischargeID;//排放类型ID
    private String dischargeName;//排放类型名称
    private String status;//状态

    public String getDischargeID() {
        return dischargeID;
    }

    public void setDischargeID(String dischargeID) {
        this.dischargeID = dischargeID;
    }

    public String getDischargeName() {
        return dischargeName;
    }

    public void setDischargeName(String dischargeName) {
        this.dischargeName = dischargeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
