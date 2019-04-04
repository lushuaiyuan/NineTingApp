package com.zzti.lsy.ninetingapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lsy
 * @create 2019/4/3 20:37
 * @Describe
 */
public class UpdateApply implements Parcelable {
    /// 申请单编号
    private String applyID;

    /// 申请时间
    private String applyTime;

    ///更新内容
    private String updateInfo;

    ///更新数据SQL 不做展示 可能也不会返回
    private String updateJson;

    ///申请人
    private String applyUser;

    ///申请状态
    private String status;

    ///审批时间
    private String updateTime;

    ///所属项目部
    private String projectID;

    /// 申请单类型 0为库存修改1为生产记录修改
    private String type;

    /// 项目部名称
    private String projectName;

    public String getApplyID() {
        return applyID;
    }

    public void setApplyID(String applyID) {
        this.applyID = applyID;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(String updateJson) {
        this.updateJson = updateJson;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.applyID);
        dest.writeString(this.applyTime);
        dest.writeString(this.updateInfo);
        dest.writeString(this.updateJson);
        dest.writeString(this.applyUser);
        dest.writeString(this.status);
        dest.writeString(this.updateTime);
        dest.writeString(this.projectID);
        dest.writeString(this.type);
        dest.writeString(this.projectName);
    }

    public UpdateApply() {
    }

    protected UpdateApply(Parcel in) {
        this.applyID = in.readString();
        this.applyTime = in.readString();
        this.updateInfo = in.readString();
        this.updateJson = in.readString();
        this.applyUser = in.readString();
        this.status = in.readString();
        this.updateTime = in.readString();
        this.projectID = in.readString();
        this.type = in.readString();
        this.projectName = in.readString();
    }

    public static final Parcelable.Creator<UpdateApply> CREATOR = new Parcelable.Creator<UpdateApply>() {
        @Override
        public UpdateApply createFromParcel(Parcel source) {
            return new UpdateApply(source);
        }

        @Override
        public UpdateApply[] newArray(int size) {
            return new UpdateApply[size];
        }
    };
}
