package com.zzti.lsy.ninetingapp.entity;

/**
 * author：anxin on 2018/8/9 10:27
 * 配件的实体类
 */
public class PartsInfoEntity {
    private String partsName; //配件名称
    private String partsModel;//配件类型
    private String partsNumber;//配件库存
    private String partsID;//配件ID
    private String FactoryID;//生产厂家ID
    private String projectID;//项目部ID


    private String state;//状态
    private String price;//价格

    public String getPartsID() {
        return partsID;
    }

    public void setPartsID(String partsID) {
        this.partsID = partsID;
    }

    public String getFactoryID() {
        return FactoryID;
    }

    public void setFactoryID(String factoryID) {
        FactoryID = factoryID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPartsModel() {
        return partsModel;
    }

    public void setPartsModel(String partsModel) {
        this.partsModel = partsModel;
    }

    public String getPartsNumber() {
        return partsNumber;
    }

    public void setPartsNumber(String partsNumber) {
        this.partsNumber = partsNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
