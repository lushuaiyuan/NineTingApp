package com.zzti.lsy.ninetingapp.entity;

/**
 * 项目部
 */
public class ProjectEntity {
    private String projectID;//项目编号
    private String projectName;//项目名称
    private String projectAddress;//项目地址
    private String pactID;//合同编号
    private String projectStartTime;//项目开工时间
    private String projectManagerStaffID;//项目负责人
    private String ProjectCaptainStaffID;//车队长
    private String ProjectStatisticianStaffID;//统计员

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
