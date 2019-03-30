package com.zzti.lsy.ninetingapp.entity;

import java.io.Serializable;

/**
 * 日用品
 */
public class LaoBao implements Serializable {
    private String lbID;///劳保品编号 无需填写
    private String lbName;///劳保品名称 用户填写
    private String price; ///劳保品单价 用户填写
    private String laobaoNumber;/// 劳保数量 返回值
    private String projectName; /// 项目部名称 返回值
    private String status;
    private String alarmNumber;//告警值

    public String getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public int getLpID() {
        return lpID;
    }

    public void setLpID(int lpID) {
        this.lpID = lpID;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //入库工单字段
    private int lpID;

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


    public String getLbName() {
        return lbName;
    }

    public void setLbName(String lbName) {
        this.lbName = lbName;
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
