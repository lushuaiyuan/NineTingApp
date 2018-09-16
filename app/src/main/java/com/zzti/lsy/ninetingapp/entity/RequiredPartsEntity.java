package com.zzti.lsy.ninetingapp.entity;

/**
 * 维修配件实体类
 */
public class RequiredPartsEntity {
    private String repairID;
    private String partsID;
    private String rpNumber;

    public String getRepairID() {
        return repairID;
    }

    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public String getPartsID() {
        return partsID;
    }

    public void setPartsID(String partsID) {
        this.partsID = partsID;
    }

    public String getRpNumber() {
        return rpNumber;
    }

    public void setRpNumber(String rpNumber) {
        this.rpNumber = rpNumber;
    }
}
