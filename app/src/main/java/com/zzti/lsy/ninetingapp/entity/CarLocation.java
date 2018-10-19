package com.zzti.lsy.ninetingapp.entity;

/**
 * @author lsy
 * @create 2018/10/19 20:41
 * @Describe 车辆位置
 */
public class CarLocation {
    private String platenumber;
    private String Location;
    private String lastTime;

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
