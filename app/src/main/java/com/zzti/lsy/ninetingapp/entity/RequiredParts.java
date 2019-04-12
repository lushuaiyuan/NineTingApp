package com.zzti.lsy.ninetingapp.entity;

/**
 * 维休设备的实体类
 */
public class RequiredParts {
    private String repairID;//维修id
    private String partsID;//配件id
    private String rpNumber;//配件数量
    private String partsNumber;//配件库存
    private String partsName;//配件名称
    private String partsModel;//配件类型
    private String purchasedPrice;//配件单价
    private Integer type;//模式 已有为0  现采为1
    private String number;//退库数量

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPartsNumber() {
        return partsNumber;
    }

    public void setPartsNumber(String partsNumber) {
        this.partsNumber = partsNumber;
    }

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
