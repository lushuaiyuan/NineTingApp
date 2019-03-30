package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * 劳保品传的参数
 */
public class LaobaoPurchased implements Serializable {
    private String lpID;///劳保品入库工单编号 无需填写
    private String purchasedDate; ///入库时间 无需填写
    private String purchasedMoney;///劳保品单价 用户填写
    private String number;/// 数量 用户填写
    private String userID;///经手人ID 当前用户
    private String lbID;///劳保品编号
    private String projectID;//项目部编号 无需填写
    private String status; //申请单状态（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1为拒绝）
    private String opinion;//审批意见
    private String applyTime;//申请时间
    private String staffName;//经手人名称
    private String projectName;//项目部
    private String lbName;//名称
    private String shipaddr;//发货地
    private String receiptNo;//单据号

    public String getShipaddr() {
        return shipaddr;
    }

    public void setShipaddr(String shipaddr) {
        this.shipaddr = shipaddr;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLbName() {
        return lbName;
    }

    public void setLbName(String lbName) {
        this.lbName = lbName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

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

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
