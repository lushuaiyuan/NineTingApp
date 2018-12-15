package com.zzti.lsy.ninetingapp.entity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/12/15 17:28
 * @Describe
 */
public class RepairEntity {

    /**
     * allQuantity : 233.0
     * allRepairMoney : 3941.0
     * scale : 16.91
     * projectName : 所有
     * RepairMoneys : [{"time":12,"money":128},{"time":12,"money":100},{"time":12,"money":120},{"time":12,"money":6},{"time":12,"money":6},{"time":12,"money":2},{"time":12,"money":100},{"time":12,"money":5},{"time":12,"money":7},{"time":12,"money":1},{"time":12,"money":1},{"time":12,"money":2333},{"time":12,"money":232},{"time":12,"money":345},{"time":12,"money":332},{"time":12,"money":100},{"time":12,"money":123}]
     * QuantityRecords : [{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0},{"time":12,"quantity":0}]
     * scales : [{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"},{"time":12,"scale":"Infinity"}]
     */

    private String allQuantity;
    private String allRepairMoney;
    private String scale;
    private String projectName;
    private List<RepairMoneysBean> RepairMoneys;
    private List<QuantityRecordsBean> QuantityRecords;
    private List<ScalesBean> scales;

    public String getAllQuantity() {
        return allQuantity;
    }

    public void setAllQuantity(String allQuantity) {
        this.allQuantity = allQuantity;
    }

    public String getAllRepairMoney() {
        return allRepairMoney;
    }

    public void setAllRepairMoney(String allRepairMoney) {
        this.allRepairMoney = allRepairMoney;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<RepairMoneysBean> getRepairMoneys() {
        return RepairMoneys;
    }

    public void setRepairMoneys(List<RepairMoneysBean> RepairMoneys) {
        this.RepairMoneys = RepairMoneys;
    }

    public List<QuantityRecordsBean> getQuantityRecords() {
        return QuantityRecords;
    }

    public void setQuantityRecords(List<QuantityRecordsBean> QuantityRecords) {
        this.QuantityRecords = QuantityRecords;
    }

    public List<ScalesBean> getScales() {
        return scales;
    }

    public void setScales(List<ScalesBean> scales) {
        this.scales = scales;
    }

    public static class RepairMoneysBean {
        /**
         * time : 12
         * money : 128.0
         */

        private int time;
        private String money;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

    public static class QuantityRecordsBean {
        /**
         * time : 12
         * quantity : 0.0
         */

        private int time;
        private String quantity;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }

    public static class ScalesBean {
        /**
         * time : 12
         * scale : Infinity
         */

        private int time;
        private String scale;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }
    }
}
