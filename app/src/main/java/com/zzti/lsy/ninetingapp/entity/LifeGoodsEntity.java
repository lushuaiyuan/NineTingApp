package com.zzti.lsy.ninetingapp.entity;

/**
 * 日用品
 */
public class LifeGoodsEntity {
    private String lbID;
    private String lbName;
    private String price;
    private String laobaoNumber;
    private String projectName;
    private String outTime;
    private String operatorName;
    private String recipient;//领用人

    public String getLbID() {
        return lbID;
    }

    public void setLbID(String lbID) {
        this.lbID = lbID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getLbName() {
        return lbName;
    }

    public void setLbName(String lbName) {
        this.lbName = lbName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getLaobaoNumber() {
        return laobaoNumber;
    }

    public void setLaobaoNumber(String laobaoNumber) {
        this.laobaoNumber = laobaoNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
