package com.zzti.lsy.ninetingapp.entity;

/**
 * 维休设备的实体类
 */
public class RequiredParts {
    private String repairID;//维修id
    private String partsID;//配件id
    private String rpNumber;//配件数量
    private String partsName;//配件名称
    private String partsModel;//配件类型

    public String getPartsModel() {
        return partsModel;
    }

    public void setPartsModel(String partsModel) {
        this.partsModel = partsModel;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

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
