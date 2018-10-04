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
    private String opinion;// 拒绝时的 拒绝意见
    private String applyTime;/// 申请时间
    private String pType;  /// \入库单类型（0为采购1为入库）
    private String staffName;
    private String partsName;//配件名称
    private String partsModel;//配件类型
    private String projectName;//项目部名称
    private String FactoryName;//生产厂家名称


    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getPartsModel() {
        return partsModel;
    }

    public void setPartsModel(String partsModel) {
        this.partsModel = partsModel;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFactoryName() {
        return FactoryName;
    }

    public void setFactoryName(String factoryName) {
        FactoryName = factoryName;
    }

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
