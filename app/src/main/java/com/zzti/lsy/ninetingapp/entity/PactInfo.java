package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * author：anxin on 2018/10/12 19:17
 * 合同实体类
 */
public class PactInfo implements Serializable {
    private String pactID;//合同ID 系统生成
    private String pactType;//合同类型
    private String pactName;//合同名称/编号
    private String pactContent;//合同简介
    private String pactSchedule;//合同状态（已结清、未结清）
    private String pactTime;//合同到期时间
    private String pactMoney;//合同总金额
    private String pactRealMoney;//应收金额
    private String pactOutMoney;//合同未收款
    private String pactInMoney;//合同已收款
    private String projectID;/// 所属项目部ID
    private String addTime;/// 合同添加时间 无需填写
    private String projectName;/// 所属项目部 无需填写
    private String updateTime;/// 更新时间 无需填写

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPactID() {
        return pactID;
    }

    public void setPactID(String pactID) {
        this.pactID = pactID;
    }

    public String getPactType() {
        return pactType;
    }

    public void setPactType(String pactType) {
        this.pactType = pactType;
    }

    public String getPactContent() {
        return pactContent;
    }

    public void setPactContent(String pactContent) {
        this.pactContent = pactContent;
    }

    public String getPactSchedule() {
        return pactSchedule;
    }

    public void setPactSchedule(String pactSchedule) {
        this.pactSchedule = pactSchedule;
    }

    public String getPactTime() {
        return pactTime;
    }

    public void setPactTime(String pactTime) {
        this.pactTime = pactTime;
    }

    public String getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(String pactMoney) {
        this.pactMoney = pactMoney;
    }

    public String getPactRealMoney() {
        return pactRealMoney;
    }

    public void setPactRealMoney(String pactRealMoney) {
        this.pactRealMoney = pactRealMoney;
    }

    public String getPactOutMoney() {
        return pactOutMoney;
    }

    public void setPactOutMoney(String pactOutMoney) {
        this.pactOutMoney = pactOutMoney;
    }

    public String getPactInMoney() {
        return pactInMoney;
    }

    public void setPactInMoney(String pactInMoney) {
        this.pactInMoney = pactInMoney;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }
}
