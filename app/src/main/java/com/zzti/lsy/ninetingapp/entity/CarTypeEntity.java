package com.zzti.lsy.ninetingapp.entity;

import java.util.List;

/**
 * 车辆类型
 */
public class CarTypeEntity {

    private String vehicleTypeID;//车辆类型id
    private String vehicleTypeName;//车辆类型名称
    private int status;//车辆类型状态

    public String getVehicleTypeID() {
        return vehicleTypeID;
    }

    public void setVehicleTypeID(String vehicleTypeID) {
        this.vehicleTypeID = vehicleTypeID;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
