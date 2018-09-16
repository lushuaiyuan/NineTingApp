package com.zzti.lsy.ninetingapp.entity;

/**
 * 维修原因的实体类
 */
public class RepairCauseEntity {
    private String causeID;
    private String causeName;

    public String getCauseID() {
        return causeID;
    }

    public void setCauseID(String causeID) {
        this.causeID = causeID;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }
}
