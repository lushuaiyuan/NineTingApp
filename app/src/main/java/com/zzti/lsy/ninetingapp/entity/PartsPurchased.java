package com.zzti.lsy.ninetingapp.entity;

/**
 * 配件入库传的参数
 */
public class PartsPurchased {
    private String ppID;//入库编号 无需填写
    private String purchasedChanel;///进货渠道 需要填写
    private String purchasedDate;///入库时间 无需填写
    private String purchasedPrice; ///配件单价
    private String number;///数量
    private String userID;///经手人ID
    private String partsID;///配件ID
    private String projectID;///项目部ID  无需填写

    public String getPpID() {
        return ppID;
    }

    public void setPpID(String ppID) {
        this.ppID = ppID;
    }

    public String getPurchasedChanel() {
        return purchasedChanel;
    }

    public void setPurchasedChanel(String purchasedChanel) {
        this.purchasedChanel = purchasedChanel;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPartsID() {
        return partsID;
    }

    public void setPartsID(String partsID) {
        this.partsID = partsID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
