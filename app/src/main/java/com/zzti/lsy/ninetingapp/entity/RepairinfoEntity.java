package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * 维修申请实体类
 */
public class RepairinfoEntity implements Serializable {
    private String repairID;//维修编号 实体中不用传 后台生成
    private String repairApplyTime;//维修单申请时间 后台填 以服务器时间为准
    private String plateNumber; //车牌号 用户选择
    private String repairContent;//维修内容 用户填写
    private String repairMoney;//维修金额 用户填写
    private String repairCauseID;//维修原因ID 下拉选择
    private String repairTypeID;//维修类型ID 下拉选择
    private String projectID;//项目部ID 下拉选择
    private String jobLocation;//暂无用 考虑去掉
    private String repairBeginTime;//计划开始维修时间 用户填写
    private String repairOverTime;//计划维修结束时间 用户填写
    private String userID;//申请人ID 获取当前登录用户的UserID
    private String remark;//备注 用户填写
    private String devPicture;//以base64字符加密传过来，最多三张以|字符分割
    private String status;//维修单状态（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过）不用填写 默认为2
    private String opinion;//审批意见 默认为空
    private String causeName;//维修原因
    private String typeName;//维修类型
    private String staffName;//申请人姓名
    private String projectName;//项目部名称
    private String showContent;//显示的维修内容

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRepairID() {
        return repairID;
    }

    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public String getRepairApplyTime() {
        return repairApplyTime;
    }

    public void setRepairApplyTime(String repairApplyTime) {
        this.repairApplyTime = repairApplyTime;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    public String getRepairMoney() {
        return repairMoney;
    }

    public void setRepairMoney(String repairMoney) {
        this.repairMoney = repairMoney;
    }

    public String getRepairCauseID() {
        return repairCauseID;
    }

    public void setRepairCauseID(String repairCauseID) {
        this.repairCauseID = repairCauseID;
    }

    public String getRepairTypeID() {
        return repairTypeID;
    }

    public void setRepairTypeID(String repairTypeID) {
        this.repairTypeID = repairTypeID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getRepairBeginTime() {
        return repairBeginTime;
    }

    public void setRepairBeginTime(String repairBeginTime) {
        this.repairBeginTime = repairBeginTime;
    }

    public String getRepairOverTime() {
        return repairOverTime;
    }

    public void setRepairOverTime(String repairOverTime) {
        this.repairOverTime = repairOverTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDevPicture() {
        return devPicture;
    }

    public void setDevPicture(String devPicture) {
        this.devPicture = devPicture;
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

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
