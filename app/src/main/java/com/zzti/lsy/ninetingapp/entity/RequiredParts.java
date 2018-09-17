package com.zzti.lsy.ninetingapp.entity;

/**
 * 维休设备的实体类
 */
public class RequiredParts {
    private String repairID;//维修id
    private String partsID;//配件id
    private String rpNumber;
    private String partsName;//配件名称
    private String partsAmount;//配件数量
    private String money;//维修金额



    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPartsAmount() {
        return partsAmount;
    }

    public void setPartsAmount(String partsAmount) {
        this.partsAmount = partsAmount;
    }
}
