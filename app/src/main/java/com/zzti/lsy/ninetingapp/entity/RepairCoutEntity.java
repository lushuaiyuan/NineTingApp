package com.zzti.lsy.ninetingapp.entity;

import com.github.mikephil.charting.data.BaseEntry;

import java.util.List;

/**
 * @author lsy
 * @create 2018/10/9 22:41
 * @Describe 维修统计实体类
 */
public class RepairCoutEntity extends BaseEntry {

    private List<RepairStatisticInfo> data;

    @Override
    public List<RepairStatisticInfo> getData() {
        return data;
    }

    public void setData(List<RepairStatisticInfo> data) {
        this.data = data;
    }

    public static class RepairStatisticInfo {
        private String money;
        private String time;

        public RepairStatisticInfo(String time, String money) {
            this.money = money;
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
