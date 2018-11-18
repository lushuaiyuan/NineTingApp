package com.zzti.lsy.ninetingapp.entity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/11/18 20:02
 * @Describe
 */
public class ProductEntity {

    /**
     * quantity : 210.0
     * wear : 110.00400000000002
     * zratio : 0.52
     * projectName : 徐州项目
     * WearRecords : [{"time":"2018-10-15T00:00:00","wear":17},{"time":"2018-10-13T00:00:00","wear":5},{"time":"2018-10-16T00:00:00","wear":12},{"time":"2018-10-13T00:00:00","wear":10},{"time":"2018-10-14T00:00:00","wear":3},{"time":"2018-10-15T00:00:00","wear":20},{"time":"2018-10-15T00:00:00","wear":5},{"time":"2018-10-17T00:00:00","wear":20},{"time":"2018-10-17T00:00:00","wear":10},{"time":"2018-10-23T00:00:00","wear":0.001},{"time":"2018-10-23T00:00:00","wear":0.001},{"time":"2018-10-23T00:00:00","wear":2.001},{"time":"2018-10-24T00:00:00","wear":2},{"time":"2018-11-01T00:00:00","wear":1.001},{"time":"2018-10-14T00:00:00","wear":2},{"time":"2018-10-22T00:00:00","wear":1}]
     * QuantityRecords : [{"time":"2018-10-15T00:00:00","quantity":7},{"time":"2018-10-13T00:00:00","quantity":5},{"time":"2018-10-16T00:00:00","quantity":4},{"time":"2018-10-13T00:00:00","quantity":12},{"time":"2018-10-14T00:00:00","quantity":7},{"time":"2018-10-15T00:00:00","quantity":50},{"time":"2018-10-15T00:00:00","quantity":10},{"time":"2018-10-17T00:00:00","quantity":50},{"time":"2018-10-17T00:00:00","quantity":11},{"time":"2018-10-23T00:00:00","quantity":12},{"time":"2018-10-23T00:00:00","quantity":0},{"time":"2018-10-23T00:00:00","quantity":0},{"time":"2018-10-24T00:00:00","quantity":1},{"time":"2018-11-01T00:00:00","quantity":20},{"time":"2018-10-14T00:00:00","quantity":20},{"time":"2018-10-22T00:00:00","quantity":1}]
     * ZratioRecords : [{"time":"2018-10-15T00:00:00","zratio":2.43},{"time":"2018-10-13T00:00:00","zratio":1},{"time":"2018-10-16T00:00:00","zratio":3},{"time":"2018-10-13T00:00:00","zratio":0.83},{"time":"2018-10-14T00:00:00","zratio":0.43},{"time":"2018-10-15T00:00:00","zratio":0.4},{"time":"2018-10-15T00:00:00","zratio":0.5},{"time":"2018-10-17T00:00:00","zratio":0.4},{"time":"2018-10-17T00:00:00","zratio":0.91},{"time":"2018-10-23T00:00:00","zratio":0},{"time":"2018-10-23T00:00:00","zratio":"Infinity"},{"time":"2018-10-23T00:00:00","zratio":"Infinity"},{"time":"2018-10-24T00:00:00","zratio":2},{"time":"2018-11-01T00:00:00","zratio":0.05},{"time":"2018-10-14T00:00:00","zratio":0.1},{"time":"2018-10-22T00:00:00","zratio":1}]
     */

    private String quantity;
    private String wear;
    private String zratio;
    private String projectName;
    private List<WearRecordsBean> WearRecords;
    private List<QuantityRecordsBean> QuantityRecords;
    private List<ZratioRecordsBean> ZratioRecords;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWear() {
        return wear;
    }

    public void setWear(String wear) {
        this.wear = wear;
    }

    public String getZratio() {
        return zratio;
    }

    public void setZratio(String zratio) {
        this.zratio = zratio;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<WearRecordsBean> getWearRecords() {
        return WearRecords;
    }

    public void setWearRecords(List<WearRecordsBean> WearRecords) {
        this.WearRecords = WearRecords;
    }

    public List<QuantityRecordsBean> getQuantityRecords() {
        return QuantityRecords;
    }

    public void setQuantityRecords(List<QuantityRecordsBean> QuantityRecords) {
        this.QuantityRecords = QuantityRecords;
    }

    public List<ZratioRecordsBean> getZratioRecords() {
        return ZratioRecords;
    }

    public void setZratioRecords(List<ZratioRecordsBean> ZratioRecords) {
        this.ZratioRecords = ZratioRecords;
    }

    public static class WearRecordsBean {
        /**
         * time : 2018-10-15T00:00:00
         * wear : 17.0
         */

        private String time;
        private double wear;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getWear() {
            return wear;
        }

        public void setWear(double wear) {
            this.wear = wear;
        }
    }

    public static class QuantityRecordsBean {
        /**
         * time : 2018-10-15T00:00:00
         * quantity : 7.0
         */

        private String time;
        private double quantity;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }
    }

    public static class ZratioRecordsBean {
        /**
         * time : 2018-10-15T00:00:00
         * zratio : 2.43
         */

        private String time;
        private double zratio;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getZratio() {
            return zratio;
        }

        public void setZratio(double zratio) {
            this.zratio = zratio;
        }
    }
}
