package com.zzti.lsy.ninetingapp.event;

public class PartsPurchaseMsg {
    private int status;
    private String date;

    public PartsPurchaseMsg(int status, String date) {
        this.status = status;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
