package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.CarStatusEntity;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.DisChargeEntity;
import com.zzti.lsy.ninetingapp.entity.FactoryInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.CarStatusAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.DisChargedAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.FactoryAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceInputActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.tv_emission_standard)
    TextView tvEmissionStandard; //排放标准
    @BindView(R.id.tv_carStatus)
    TextView tvCarStauts;//车辆状态
    @BindView(R.id.tv_factory)
    TextView tvFactory;//生产厂家
    @BindView(R.id.tv_project)
    TextView tvProject;//项目部
    @BindView(R.id.et_vin)
    EditText etVin;//识别码
    @BindView(R.id.et_engine_number)
    EditText etEngineNumber;//发动机号
    @BindView(R.id.tv_buyTime)
    TextView tvBuyTime;//购买时间
    @BindView(R.id.et_buyMoney)
    EditText etBuyMoney;//购买金额
    @BindView(R.id.et_drivingNumber)
    EditText etDrivingNumber;//行驶证号码
    @BindView(R.id.tv_drivingNumber_giveTime)
    TextView tvDrivingNumberGiveTime;//行驶证发放日期
    @BindView(R.id.et_drivingNumber_validityTime)
    EditText etDrivingNumberValidityTime;//行驶证有效期

    //车辆类型pop
    private PopupWindow popupWindowCarType;
    private ListView mListViewCarType;
    private CarTypeAdapter carTypeAdapter;
    private List<CarTypeEntity> carTypeEntities;
    //排放标准pop
    private PopupWindow popupWindowDisCharged;
    private ListView mListViewDisCharged;
    private DisChargedAdapter disChargedAdapter;
    private List<DisChargeEntity> disChargeEntities;
    //生产厂家pop
    private PopupWindow popupWindowFactory;
    private ListView mListViewFactory;
    private FactoryAdapter factoryAdapter;
    private List<FactoryInfoEntity> factoryInfoEntities;

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    //车辆状态
    private PopupWindow popupWindowCarStatus;
    private ListView mListViewCarStatus;
    private CarStatusAdapter carStatusAdapter;
    private List<CarStatusEntity> carStatusEntities;

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initCarTypePop();
        initStandardPop();
        initFactoryPop();
        initProjectPop();
        initCarStatusPop();
    }

    private void initCarTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarType = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarType.setFocusable(true);
        popupWindowCarType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowCarType.setOnDismissListener(this);
        popupWindowCarType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCarType.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewCarType = contentview.findViewById(R.id.pop_list);
        carTypeEntities = new ArrayList<>();
        carTypeAdapter = new CarTypeAdapter(carTypeEntities);
        mListViewCarType.setAdapter(carTypeAdapter);
        mListViewCarType.setOnItemClickListener(this);
        popupWindowCarType.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initStandardPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowDisCharged = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowDisCharged.setFocusable(true);
        popupWindowDisCharged.setOutsideTouchable(true);
        //设置消失监听
        popupWindowDisCharged.setOnDismissListener(this);
        popupWindowDisCharged.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowDisCharged.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewDisCharged = contentview.findViewById(R.id.pop_list);
        disChargeEntities = new ArrayList<>();
        disChargedAdapter = new DisChargedAdapter(disChargeEntities);
        mListViewDisCharged.setAdapter(disChargedAdapter);
        mListViewDisCharged.setOnItemClickListener(this);
        popupWindowDisCharged.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initFactoryPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowFactory = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowFactory.setFocusable(true);
        popupWindowFactory.setOutsideTouchable(true);
        //设置消失监听
        popupWindowFactory.setOnDismissListener(this);
        popupWindowFactory.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowFactory.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewFactory = contentview.findViewById(R.id.pop_list);
        factoryInfoEntities = new ArrayList<>();
        factoryAdapter = new FactoryAdapter(factoryInfoEntities);
        mListViewFactory.setAdapter(factoryAdapter);
        mListViewFactory.setOnItemClickListener(this);
        popupWindowFactory.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initProjectPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
        //设置消失监听
        popupWindowProject.setOnDismissListener(this);
        popupWindowProject.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowProject.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewProject = contentview.findViewById(R.id.pop_list);
        projectEntities = new ArrayList<>();
        projectAdapter = new ProjectAdapter(projectEntities);
        mListViewProject.setAdapter(projectAdapter);
        mListViewProject.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initCarStatusPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarStatus = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarStatus.setFocusable(true);
        popupWindowCarStatus.setOutsideTouchable(true);
        //设置消失监听
        popupWindowCarStatus.setOnDismissListener(this);
        popupWindowCarStatus.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCarStatus.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewCarStatus = contentview.findViewById(R.id.pop_list);
        carStatusEntities = new ArrayList<>();
        carStatusAdapter = new CarStatusAdapter(carStatusEntities);
        mListViewCarStatus.setAdapter(carStatusAdapter);
        mListViewCarStatus.setOnItemClickListener(this);
        popupWindowCarStatus.setAnimationStyle(R.style.anim_bottomPop);
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }

    private void initView() {
        setTitle("设备录入");
        etBuyMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    private int tag;

    @OnClick({R.id.tv_carType, R.id.tv_emission_standard, R.id.tv_carStatus, R.id.tv_factory, R.id.tv_project, R.id.tv_buyTime, R.id.tv_drivingNumber_giveTime, R.id.btn_inputYearInsurance})
    public void viewClick(View view) {
        hideSoftInput(etBuyMoney);
        switch (view.getId()) {
            case R.id.tv_carType://获取车辆类型
                carTypeEntities.clear();
                tag = 1;
                showDia();
                getCarType();
                break;
            case R.id.tv_emission_standard://获取排放标准
                disChargeEntities.clear();
                tag = 2;
                showDia();
                getChargeType();
                break;
            case R.id.tv_carStatus://车辆状态
                tag = 5;
                showCarStatus();
                break;
            case R.id.tv_factory://生产厂家
                factoryInfoEntities.clear();
                tag = 3;
                showDia();
                getCarFactory();
                break;
            case R.id.tv_project://项目部
                projectEntities.clear();
                tag = 4;
                showDia();
                getProject();
                break;
            case R.id.tv_buyTime://选择购买日期
                showCustomTime(1);
                break;
            case R.id.tv_drivingNumber_giveTime://行驶证发放日期
                showCustomTime(2);
                break;
            case R.id.btn_inputYearInsurance:
                setData();
                break;

        }
    }

    //0存放中、1工作中、2维修中
    private void showCarStatus() {
        carStatusEntities.clear();
        CarStatusEntity carStatusEntity1 = new CarStatusEntity("0", "存放中");
        CarStatusEntity carStatusEntity2 = new CarStatusEntity("1", "工作中");
        CarStatusEntity carStatusEntity3 = new CarStatusEntity("2", "维修中");
        carStatusEntities.add(carStatusEntity1);
        carStatusEntities.add(carStatusEntity2);
        carStatusEntities.add(carStatusEntity3);
        carStatusAdapter.notifyDataSetChanged();
        setBackgroundAlpha(0.5f);
        popupWindowCarStatus.showAtLocation(tvCarStauts, Gravity.BOTTOM, 0, 0);
    }


    private void setData() {
        hideSoftInput(etBuyMoney);
        CarInfoEntity carInfoEntity = new CarInfoEntity();
        if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
            UIUtils.showT("请输入车牌号");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvCarType.getText().toString())) {
            UIUtils.showT("请选择车辆类型");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvEmissionStandard.getText().toString())) {
            UIUtils.showT("请选择排放标准");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvCarStauts.getText().toString())) {
            UIUtils.showT("请选择车辆状态");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvFactory.getText().toString())) {
            UIUtils.showT("请选择厂家");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvProject.getText().toString())) {
            UIUtils.showT("请选择项目部");
            return;
        }
        if (StringUtil.isNullOrEmpty(etVin.getText().toString())) {
            UIUtils.showT("请输入识别码");
            return;
        }
        if (StringUtil.isNullOrEmpty(etEngineNumber.getText().toString())) {
            UIUtils.showT("请输入发动机号");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvBuyTime.getText().toString())) {
            UIUtils.showT("请选择购买日期");
            return;
        }
        if (StringUtil.isNullOrEmpty(etBuyMoney.getText().toString())) {
            UIUtils.showT("请输入购买金额");
            return;
        }
        if (StringUtil.isNullOrEmpty(etDrivingNumber.getText().toString())) {
            UIUtils.showT("请输入行驶证号码");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvDrivingNumberGiveTime.getText().toString())) {
            UIUtils.showT("请选择行驶证发放日期");
            return;
        }
        if (StringUtil.isNullOrEmpty(etDrivingNumberValidityTime.getText().toString())) {
            UIUtils.showT("请输入行驶证有效期");
            return;
        }
        carInfoEntity.setPlateNumber(etCarNumber.getText().toString());
        carInfoEntity.setCarTypeID(CarTypeID);
        carInfoEntity.setStatus(carStatus);
        carInfoEntity.setDischargeID(dischargeID);
        carInfoEntity.setFactoryID(FactoryID);
        carInfoEntity.setProjectID(projectID);
        carInfoEntity.setVIN(etVin.getText().toString());
        carInfoEntity.setEngineNumber(etEngineNumber.getText().toString());
        carInfoEntity.setPurchaseDate(tvBuyTime.getText().toString());
        carInfoEntity.setPrice(etBuyMoney.getText().toString());
        carInfoEntity.setDrivingLicenseNumber(etDrivingNumber.getText().toString());
        carInfoEntity.setDLDate(tvDrivingNumberGiveTime.getText().toString());
        carInfoEntity.setDLValidDate(DateUtil.getAfterMonth(tvDrivingNumberGiveTime.getText().toString(), Integer.parseInt(etDrivingNumberValidityTime.getText().toString()) * 12));
        Intent intent = new Intent(this, YearInsuranceActivity.class);
        intent.putExtra("carInfoEntity", carInfoEntity);
        startActivity(intent);

    }

    /**
     * 获取项目部
     */
    private void getProject() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, params, tvProject, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProjectEntity projectEntity = ParseUtils.parseJson(jsonArray.getString(i), ProjectEntity.class);
                            projectEntities.add(projectEntity);
                        }
                        if (projectEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (projectEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = projectAdapter.getView(0, null, mListViewProject);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewProject.getDividerHeight()) * 5;
                                mListViewProject.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewProject.getLayoutParams();
                                params.height = totalHei;
                                mListViewProject.setLayoutParams(params);
                            }
                            popupWindowProject.showAtLocation(tvProject, Gravity.BOTTOM, 0, 0);
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    /**
     * 获取汽车的生产厂家
     */
    private void getCarFactory() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETFACTORY, params, tvFactory, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FactoryInfoEntity factoryInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), FactoryInfoEntity.class);
                            factoryInfoEntities.add(factoryInfoEntity);
                        }
                        if (factoryInfoEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (factoryInfoEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = factoryAdapter.getView(0, null, mListViewFactory);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewFactory.getDividerHeight()) * 5;
                                mListViewFactory.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewFactory.getLayoutParams();
                                params.height = totalHei;
                                mListViewFactory.setLayoutParams(params);
                            }
                            popupWindowFactory.showAtLocation(tvFactory, Gravity.BOTTOM, 0, 0);
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    /**
     * 获取排放标准
     */
    private void getChargeType() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETCHARGETYPE, params, tvEmissionStandard, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DisChargeEntity disChargeEntity = ParseUtils.parseJson(jsonArray.getString(i), DisChargeEntity.class);
                            disChargeEntities.add(disChargeEntity);
                        }
                        if (disChargeEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (disChargeEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = disChargedAdapter.getView(0, null, mListViewDisCharged);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewDisCharged.getDividerHeight()) * 5;
                                mListViewDisCharged.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewDisCharged.getLayoutParams();
                                params.height = totalHei;
                                mListViewDisCharged.setLayoutParams(params);
                            }
                            popupWindowDisCharged.showAtLocation(tvEmissionStandard, Gravity.BOTTOM, 0, 0);
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    /**
     * 获取车辆类型
     */
    private void getCarType() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETCARTYPE, params, tvCarType, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CarTypeEntity carTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), CarTypeEntity.class);
                            carTypeEntities.add(carTypeEntity);
                        }
                        if (carTypeEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (carTypeEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = carTypeAdapter.getView(0, null, mListViewCarType);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewCarType.getDividerHeight()) * 5;
                                mListViewCarType.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewCarType.getLayoutParams();
                                params.height = totalHei;
                                mListViewCarType.setLayoutParams(params);
                            }
                            popupWindowCarType.showAtLocation(tvCarType, Gravity.BOTTOM, 0, 0);
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    private String CarTypeID;
    private String dischargeID;
    private String FactoryID;
    private String projectID;
    private String carStatus;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (tag == 1) {//类型
            tvCarType.setText(carTypeEntities.get(position).getVehicleTypeName());
            CarTypeID = carTypeEntities.get(position).getVehicleTypeID();
            popupWindowCarType.dismiss();
        } else if (tag == 2) {//排放量
            tvEmissionStandard.setText(disChargeEntities.get(position).getDischargeName());
            dischargeID = disChargeEntities.get(position).getDischargeID();
            popupWindowDisCharged.dismiss();
        } else if (tag == 3) {//生产厂家
            tvFactory.setText(factoryInfoEntities.get(position).getFactoryName());
            FactoryID = factoryInfoEntities.get(position).getFactoryID();
            popupWindowFactory.dismiss();
        } else if (tag == 4) {//生产厂家
            tvProject.setText(projectEntities.get(position).getProjectName());
            projectID = projectEntities.get(position).getProjectID();
            popupWindowProject.dismiss();
        } else if (tag == 5) {//车辆状态
            tvCarStauts.setText(carStatusEntities.get(position).getName());
            carStatus = carStatusEntities.get(position).getId();
            popupWindowCarStatus.dismiss();
        }
    }


    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int tag) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(DeviceInputActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (tag == 1) {
                    tvBuyTime.setText(DateUtil.getDate(date));
                } else if (tag == 2) {
                    tvDrivingNumberGiveTime.setText(DateUtil.getDate(date));
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
    }
}
