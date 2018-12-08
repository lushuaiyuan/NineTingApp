package com.zzti.lsy.ninetingapp.entity;

/**
 * @author lsy
 * @create 2018/12/8 18:05
 * @Describe 获取综合油耗报警以及生产量
 */
public class RecordCountEntity {
    /// <summary>
    /// 年度生产量
    /// </summary>
    public String yearQuantity;
    // 月生产量
    public String monthQuantity;

    /// 当日生产量
    public String dayQuantity;

    public Alarm Alarms;

    public String getYearQuantity() {
        return yearQuantity;
    }

    public void setYearQuantity(String yearQuantity) {
        this.yearQuantity = yearQuantity;
    }

    public String getMonthQuantity() {
        return monthQuantity;
    }

    public void setMonthQuantity(String monthQuantity) {
        this.monthQuantity = monthQuantity;
    }

    public String getDayQuantity() {
        return dayQuantity;
    }

    public void setDayQuantity(String dayQuantity) {
        this.dayQuantity = dayQuantity;
    }

    public Alarm getAlarms() {
        return Alarms;
    }

    public void setAlarms(Alarm alarms) {
        Alarms = alarms;
    }

    public class Alarm {
        /// 综合油耗
        public String allWear;
        /// 项目部名称/车牌号
        public String alarmitem;

        public String getAllWear() {
            return allWear;
        }

        public void setAllWear(String allWear) {
            this.allWear = allWear;
        }

        public String getAlarmitem() {
            return alarmitem;
        }

        public void setAlarmitem(String alarmitem) {
            this.alarmitem = alarmitem;
        }
    }

}
