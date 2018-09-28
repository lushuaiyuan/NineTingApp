package com.zzti.lsy.ninetingapp.entity;

/**
 * 劳保品出库提交的数据
 */
public class LaobaoDelivery {
    private String ldID;/// 出库编号 无需填写
    private String ldDate;/// 出库时间 用户填写
    private String ldNumber;/// 出库数量 用户填写
    private String lbID;/// 劳保品编号 用户选择
    private String StaffID;/// 领用人ID
    private String fromProject; /// 出库项目部编号 即为当前项目部编号
    private String userID;/// 经手人ID  当前用户ID

    public String getLdID() {
        return ldID;
    }

    public void setLdID(String ldID) {
        this.ldID = ldID;
    }

    public String getLdDate() {
        return ldDate;
    }

    public void setLdDate(String ldDate) {
        this.ldDate = ldDate;
    }

    public String getLdNumber() {
        return ldNumber;
    }

    public void setLdNumber(String ldNumber) {
        this.ldNumber = ldNumber;
    }

    public String getLbID() {
        return lbID;
    }

    public void setLbID(String lbID) {
        this.lbID = lbID;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getFromProject() {
        return fromProject;
    }

    public void setFromProject(String fromProject) {
        this.fromProject = fromProject;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
