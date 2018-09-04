package com.zzti.lsy.ninetingapp.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * 日用品
 */
public class LifeGoodsEntity extends BaseEntity {
    private String outTime;
    private String goodsName;
    private String operatorName;
    private String amount;
    private String price;
    private String recipient;//领用人

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
