package com.zzti.lsy.ninetingapp.entity;

/**
 * author：anxin on 2018/9/28 16:26
 * 出库传给后台的实体类
 */
public class PartsDelivery {
    private String poutID;///配件出库编号 无需填写
    private String pdDate;///配件出库时间 用户填写
    private String pdCause;///配件出库原因 用户填写
    private String pdNumber;///出库数量 用户填写
    private String projectID;///项目部ID 用户选择
    private String psID;///配件状态 1库存 2途中 3维修 比如他要出库去别的项目部就是2 用来维修就是3 默认为1
    private String PartsID; ///配件编号 用户选择
    private String formProject;///当前项目部编号 无需填写

    public String getPoutID() {
        return poutID;
    }

    public void setPoutID(String poutID) {
        this.poutID = poutID;
    }

    public String getPdDate() {
        return pdDate;
    }

    public void setPdDate(String pdDate) {
        this.pdDate = pdDate;
    }

    public String getPdCause() {
        return pdCause;
    }

    public void setPdCause(String pdCause) {
        this.pdCause = pdCause;
    }

    public String getPdNumber() {
        return pdNumber;
    }

    public void setPdNumber(String pdNumber) {
        this.pdNumber = pdNumber;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPsID() {
        return psID;
    }

    public void setPsID(String psID) {
        this.psID = psID;
    }

    public String getPartsID() {
        return PartsID;
    }

    public void setPartsID(String partsID) {
        PartsID = partsID;
    }

    public String getFormProject() {
        return formProject;
    }

    public void setFormProject(String formProject) {
        this.formProject = formProject;
    }
}
