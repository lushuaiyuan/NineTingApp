package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * 项目部
 */
public class ProjectEntity implements Serializable{
    private String projectID;//项目编号
    private String projectName;//项目名称
    private String projectAddress;//项目地址
    private String pactID;//合同编号
    private String projectStartTime;//项目开工时间
    private String projectManagerStaffID;///项目经理ID
    private String ProjectCaptainStaffID;//项目车队长ID
    private String ProjectStatisticianStaffID;//项目统计员ID
    private String MangerName;//项目经理姓名
    private String CartainName;//车队长姓名
    private String StatisName;//统计员姓名

    public String getMangerName() {
        return MangerName;
    }

    public void setMangerName(String mangerName) {
        MangerName = mangerName;
    }

    public String getCartainName() {
        return CartainName;
    }

    public void setCartainName(String cartainName) {
        CartainName = cartainName;
    }

    public String getStatisName() {
        return StatisName;
    }

    public void setStatisName(String statisName) {
        StatisName = statisName;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getPactID() {
        return pactID;
    }

    public void setPactID(String pactID) {
        this.pactID = pactID;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectManagerStaffID() {
        return projectManagerStaffID;
    }

    public void setProjectManagerStaffID(String projectManagerStaffID) {
        this.projectManagerStaffID = projectManagerStaffID;
    }

    public String getProjectCaptainStaffID() {
        return ProjectCaptainStaffID;
    }

    public void setProjectCaptainStaffID(String projectCaptainStaffID) {
        ProjectCaptainStaffID = projectCaptainStaffID;
    }

    public String getProjectStatisticianStaffID() {
        return ProjectStatisticianStaffID;
    }

    public void setProjectStatisticianStaffID(String projectStatisticianStaffID) {
        ProjectStatisticianStaffID = projectStatisticianStaffID;
    }
}
