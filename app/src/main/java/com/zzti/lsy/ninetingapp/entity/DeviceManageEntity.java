package com.zzti.lsy.ninetingapp.entity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/11/15 21:31
 * @Describe
 */
public class DeviceManageEntity {
    private String projectName;//项目部名称

    private List<DeviceDetial> deviceDetials;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<DeviceDetial> getDeviceDetials() {
        return deviceDetials;
    }

    public void setDeviceDetials(List<DeviceDetial> deviceDetials) {
        this.deviceDetials = deviceDetials;
    }


    public class DeviceDetial {
        private String carType; //车辆类型
        private String amount; //总数量
        private String repairAmount; //维修数量
        private String normalAmount; //正常数量

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRepairAmount() {
            return repairAmount;
        }

        public void setRepairAmount(String repairAmount) {
            this.repairAmount = repairAmount;
        }

        public String getNormalAmount() {
            return normalAmount;
        }

        public void setNormalAmount(String normalAmount) {
            this.normalAmount = normalAmount;
        }
    }
}
