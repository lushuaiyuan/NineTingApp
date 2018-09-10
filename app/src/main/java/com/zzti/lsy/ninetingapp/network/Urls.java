package com.zzti.lsy.ninetingapp.network;

/**
 * author：anxin on 2018/8/3 10:21
 */
public class Urls {
    private final static String BASE_URL = "http://111.230.139.65:8086";
    public final static String POST_LOGIN_URL = BASE_URL + "/Login/Login.asmx/LoginCheck";//登录
    public final static String POST_LOGIN_OUT = BASE_URL + "/Login/Login.asmx/LoginOut";//登出
    public final static String POST_GETSTAFF = BASE_URL + "/Login/Login.asmx/GetStaff";//获取用户对应员工信息
    public final static String POST_ADDCAR = BASE_URL + "/Car/Car.asmx/AddCar";//录入车辆
    public final static String POST_GETCARLIST = BASE_URL + "/Car/Car.asmx/GetCarList";//获取车辆信息列表
    public final static String POST_GETCARTYPE = BASE_URL + "/Car/Car.asmx/GetCarType";//获取车辆类型信息
    public final static String POST_GETCHARGETYPE = BASE_URL + "/Car/Car.asmx/GetChargeType";//获取车辆排放类型
    public final static String POST_GETPROJECT = BASE_URL + "/Car/Car.asmx/GetProject";//获取项目部
    public final static String POST_GETFACTORY = BASE_URL + "/Car/Car.asmx/GetFactory";//获取生产厂家
    public final static String POST_GETINSURANCETYPE = BASE_URL + "/Car/Car.asmx/GetInsuranceType";//获取保险类型


}
