package com.zzti.lsy.ninetingapp.entity;

/**
 * 劳保品传的参数
 */
public class LaobaoPurchased {
    private String lpID;///劳保品入库工单编号 无需填写
    private String purchasedDate; ///入库时间 无需填写
    private String purchasedMoney;///劳保品单价 用户填写
    private String number;/// 数量 用户填写
    private String userID;///经手人ID 当前用户
    private String lbID;///劳保品编号
    private String toProject;///项目部编号 无需填写

    public String getLpID() {
        return lpID;
    }

    public void setLpID(String lpID) {
        this.lpID = lpID;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public String getPurchasedMoney() {
        return purchasedMoney;
    }

    public void setPurchasedMoney(String purchasedMoney) {
        this.purchasedMoney = purchasedMoney;
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

    public String getLbID() {
        return lbID;
    }

    public void setLbID(String lbID) {
        this.lbID = lbID;
    }

    public String getToProject() {
        return toProject;
    }

    public void setToProject(String toProject) {
        this.toProject = toProject;
    }
}
