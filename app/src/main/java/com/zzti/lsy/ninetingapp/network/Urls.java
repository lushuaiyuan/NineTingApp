package com.zzti.lsy.ninetingapp.network;

/**
 * author：anxin on 2018/8/3 10:21
 */
public class Urls {
    private final static String BASE_URL = "http://111.230.139.65:8086";
    //        private final static String BASE_URL = "http://192.168.0.110:8086";
    public final static String POST_LOGIN_URL = BASE_URL + "/Login/Login.asmx/LoginCheck";//登录
    public final static String POST_LOGIN_OUT = BASE_URL + "/Login/Login.asmx/LoginOut";//登出
    public final static String POST_GETSTAFF = BASE_URL + "/Login/Login.asmx/GetStaff";//获取用户对应员工信息
    public final static String POST_GETPROJECT = BASE_URL + "/Login/Login.asmx/GetProject";//获取项目部

    //设备管理员
    public final static String POST_ADDINCAR = BASE_URL + "/Car/Car.asmx/AddCar";//录入车辆
    public final static String POST_ADDOUTCAR = BASE_URL + "/Car/Car.asmx/AddCar";//录入车辆
    public final static String POST_GETCARLIST = BASE_URL + "/Car/Car.asmx/GetCarList";//获取车辆信息列表
    public final static String POST_GETCARTYPE = BASE_URL + "/Car/Car.asmx/GetCarType";//获取车辆类型信息
    public final static String POST_GETCHARGETYPE = BASE_URL + "/Car/Car.asmx/GetChargeType";//获取车辆排放类型
    public final static String POST_GETFACTORY = BASE_URL + "/Car/Car.asmx/GetFactory";//获取生产厂家
    public final static String POST_GETINSURANCETYPE = BASE_URL + "/Car/Car.asmx/GetInsuranceType";//获取保险类型
    public final static String POST_UPDATCARINFO = BASE_URL + "/Car/Car.asmx/UpdateCar";//更新车辆信息
    public final static String POST_GETCAREXPIRE = BASE_URL + "/Car/Car.asmx/GetCarExpire";//年审到期时间
    public final static String POST_GETCARLOCATION = BASE_URL + "/Car/Car.asmx/GetCarLocation";//获取车辆位置信息

    //机械师
    public final static String POST_ADDREPAIR = BASE_URL + "/Repair/Repair.asmx/AddRepair";//录入维修申请
    public final static String POST_CANCELREPAIR = BASE_URL + "/Repair/Repair.asmx/CancelRepair";//撤销维修工单
    public final static String POST_GETREPAIRLIST = BASE_URL + "/Repair/Repair.asmx/GetRepairList";//获取维修工单列表
    public final static String POST_GETREPAIRPARTS = BASE_URL + "/Repair/Repair.asmx/GetRepairParts";//获取维修工单所需配件列表
    public final static String POST_GETREPAIRTYPE = BASE_URL + "/Repair/Repair.asmx/GetRepairType";//获取维修类型
    public final static String POST_GETREPAIRCAUSE = BASE_URL + "/Repair/Repair.asmx/GetRepairCause";//获取维修原因
    public final static String REPAIR_GETREPAIRCOUNT = BASE_URL + "/Repair/Repair.asmx/GetRepairCount";//获取维修统计

    //配件管理员
    public final static String PARTS_GETPARTS = BASE_URL + "/Parts/Parts.asmx/GetParts";//获取配件列表
    public final static String PARTS_GETPARTSFACTORY = BASE_URL + "/Parts/Parts.asmx/GetFactory";//获取配件生产厂间
    public final static String PARTS_PARTSSTORAGE = BASE_URL + "/Parts/Parts.asmx/PartsStorage";//配件入库
    public final static String PARTS_PARTSOUT = BASE_URL + "/Parts/Parts.asmx/PartsOut";//配件出库
    public final static String PARTS_GETLAOBAO = BASE_URL + "/Parts/Parts.asmx/GetLaoBao";//获取劳保品详情列表
    public final static String PARTS_LAOSTORAGE = BASE_URL + "/Parts/Parts.asmx/LaoStorage";//劳保品采购
    public final static String PARTS_LAOBAOOUT = BASE_URL + "/Parts/Parts.asmx/LaoBaoOut";//劳保品出库
    public final static String PARTS_GETSTAFFLIST = BASE_URL + "/Parts/Parts.asmx/GetStaff";//获取员工地址
    public final static String PARTS_GETPARTSOUT = BASE_URL + "/Parts/Parts.asmx/GetPartsOut";//获取配件出库工单列表
    public final static String PARTS_CANCELPARTS = BASE_URL + "/Parts/Parts.asmx/CancelParts";//撤销配件入库工单
    public final static String PARTS_GETLAOBAOOUT = BASE_URL + "/Parts/Parts.asmx/GetLaoBaoOut";//获取劳保品出库工单列表
    public final static String PARTS_CANCELLAOBAO = BASE_URL + "/Parts/Parts.asmx/CancelLaobao";//撤销劳保品入库工单
    public final static String PARTS_CONFIRMSTORAGE = BASE_URL + "/Parts/Parts.asmx/ConfirmStorage";//确认入库工单


    //项目经理
    public final static String APPROVE_GETPARTSIN = BASE_URL + "/Approve/Approve.asmx/GetPartsIn";//获取配件入库工单列表
    public final static String APPROVE_GETLAOBAO = BASE_URL + "/Approve/Approve.asmx/GetLaoBao";//获取劳保品入库工单列表
    public final static String APPROVE_REPAIR = BASE_URL + "/Approve/Approve.asmx/ApproveRepair";//维修工单的审批
    public final static String APPROVE_PARTS = BASE_URL + "/Approve/Approve.asmx/ApproveParts";//配件入库工单的审批
    public final static String APPROVE_DAYUSE = BASE_URL + "/Approve/Approve.asmx/ApproveDayUse";//日用品入库工单的审批

    //生产员
    public static final String RECORD_ADDRECORD = BASE_URL + "/Record/Record.asmx/AddRecord";//录入生产信息
    public static final String RECORD_GETRECORDLIST = BASE_URL + "/Record/Record.asmx/GetRecordList";//获取统计报表
    public static final String RECORD_GETCARRECORD = BASE_URL + "/Record/Record.asmx/GetCarRecord";//获取单车生产统计信息
    public static final String RECORD_GETPROJECTRECORD = BASE_URL + "/Record/Record.asmx/GetProjectRecord";//

    //总经理
    public static final String ADMIN_GETPACTLIST = BASE_URL + "/Admin/Admin.asmx/GetPactList";//获取合同列表
    public static final String ADMIN_UPDATEPACT = BASE_URL + "/Admin/Admin.asmx/UpdatePact";//更新合同信息
    public static final String ADMIN_ADDPACT = BASE_URL + "/Admin/Admin.asmx/AddPact";//添加合同信息
    public static final String ADMIN_GETCARCOUNT = BASE_URL + "/Admin/Admin.asmx/GetCarCount";//设备管理
}
