package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.CarStatusEntity;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.DisChargeEntity;
import com.zzti.lsy.ninetingapp.entity.FactoryInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarStatusAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.DisChargedAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.FactoryAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
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

/**
 * @author lsy
 * @create 2018/12/2 21:32
 * @Describe 自有车辆
 */
public class InsideDeviceInputFragment extends BaseFragment implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.tv_emission_standard)
    TextView tvEmissionStandard; //排放标准
    @BindView(R.id.et_carSource)
    EditText etCarSource;//车辆来源
    @BindView(R.id.et_oldLevel)
    EditText etOldLevel;//新旧程度
    @BindView(R.id.et_drivingStatus)
    EditText etDrivingStatus;//行驶证保存情况
    @BindView(R.id.et_vin)
    EditText etVin;//识别码
    @BindView(R.id.et_engine_number)
    EditText etEngineNumber;//发动机号
    @BindView(R.id.tv_dLDate)
    TextView tvDLDate;//行驶证注册日期
    @BindView(R.id.et_registerTime)
    EditText etRegisterTime;//设备所属
    @BindView(R.id.et_qName)
    EditText etQName;//强险保险人
    @BindView(R.id.et_sName)
    EditText etSName;//商险保险人

    @BindView(R.id.tv_yearTime)
    TextView tvYearTime;//年检日期
    @BindView(R.id.et_yearExprie)
    EditText etYearExprie;//年检时限
    @BindView(R.id.tv_qStartTime)
    TextView tvQStartTime;//强制保险生效时间
    @BindView(R.id.tv_qOverTime)
    TextView tvQOverTime;//强制保险到期时间
    @BindView(R.id.et_qCompany)
    EditText etQCompany;//强制保险公司
    @BindView(R.id.et_qAddress)
    EditText etQAddress;//强险保单原件所在地

    @BindView(R.id.tv_sStartTime)
    TextView tvSStartTime;//商业保险生效日期
    @BindView(R.id.tv_sOverTime)
    TextView tvSOverTime;//商业保险到期日期
    @BindView(R.id.et_sCompany)
    EditText etSCompany;//商业保险公司
    @BindView(R.id.et_sAddress)
    EditText etSAddress;//商险保单原件所在地
    @BindView(R.id.et_initQuantity)
    EditText etInitQuantity;//初始化方量
    @BindView(R.id.et_initTravelKm)
    EditText etInitTravelKm;//初始化公里数


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
//    //生产厂家pop
//    private PopupWindow popupWindowFactory;
//    private ListView mListViewFactory;
//    private FactoryAdapter factoryAdapter;
//    private List<FactoryInfoEntity> factoryInfoEntities;

//    //项目部
//    private PopupWindow popupWindowProject;
//    private ListView mListViewProject;
//    private ProjectAdapter projectAdapter;
//    private List<ProjectEntity> projectEntities;

    //车辆状态
//    private PopupWindow popupWindowCarStatus;
//    private ListView mListViewCarStatus;
//    private CarStatusAdapter carStatusAdapter;
//    private List<CarStatusEntity> carStatusEntities;


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

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            mActivity.finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_inside_device_input;
    }

    @Override
    protected void initView() {
        initCarTypePop();
        initStandardPop();
        projectID = SpUtils.getInstance().getString(SpUtils.PROJECTID, "");
    }

    @Override
    protected void initData() {

    }

    private int tag;

    @OnClick({R.id.tv_carType, R.id.tv_emission_standard, R.id.tv_dLDate, R.id.tv_yearTime, R.id.tv_qStartTime, R.id.tv_qOverTime, R.id.tv_sStartTime, R.id.tv_sOverTime, R.id.btn_inputYearInsurance})
    public void viewClick(View view) {
        hideSoftInput(etCarNumber);
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
            case R.id.tv_dLDate://行驶证注册日期
                showCustomTime(1);
                break;
//            case R.id.tv_registerTime://登记日期
//                showCustomTime(2);
//                break;
            case R.id.tv_yearTime://年检日期
                showCustomTime(3);
                break;
            case R.id.tv_qStartTime://强制保险生效时间
                showCustomTime(4);
                break;
            case R.id.tv_qOverTime://强制保险到期时间
                showCustomTime(5);
                break;
            case R.id.tv_sStartTime://商业保险生效日期
                showCustomTime(6);
                break;
            case R.id.tv_sOverTime://商业保险到期日期
                showCustomTime(7);
                break;
            case R.id.btn_inputYearInsurance:
                setData();
                break;

        }
    }


    private void setData() {
        hideSoftInput(etCarNumber);
        CarInfoEntity carInfoEntity = new CarInfoEntity();
        if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
            UIUtils.showT("车牌号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvCarType.getText().toString())) {
            UIUtils.showT("车辆类型不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvEmissionStandard.getText().toString())) {
            UIUtils.showT("排放标准不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etCarSource.getText().toString())) {
            UIUtils.showT("车辆来源不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etOldLevel.getText().toString())) {
            UIUtils.showT("新旧程度不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etDrivingStatus.getText().toString())) {
            UIUtils.showT("行驶证保存情况不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etVin.getText().toString())) {
            UIUtils.showT("车辆识别码不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etEngineNumber.getText().toString())) {
            UIUtils.showT("发动机号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvDLDate.getText().toString())) {
            UIUtils.showT("行驶证注册日期不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etRegisterTime.getText().toString())) {
            UIUtils.showT("设备所属不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etQName.getText().toString())) {
            UIUtils.showT("强险保险人不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etSName.getText().toString())) {
            UIUtils.showT("商险保险人不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvYearTime.getText().toString())) {
            UIUtils.showT("年检日期不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etYearExprie.getText().toString())) {
            UIUtils.showT("年检时限不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvQStartTime.getText().toString())) {
            UIUtils.showT("强制保险生效时间不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etQCompany.getText().toString())) {
            UIUtils.showT("强制保险公司不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etQAddress.getText().toString())) {
            UIUtils.showT("强险保单原件所在地不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvSStartTime.getText().toString())) {
            UIUtils.showT("商业保险生效日期不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvSOverTime.getText().toString())) {
            UIUtils.showT("商业保险到期日期不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etSCompany.getText().toString())) {
            UIUtils.showT("商业保险公司不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etSAddress.getText().toString())) {
            UIUtils.showT("商险保单原件所在地");
            return;
        }
        carInfoEntity.setPlateNumber(etCarNumber.getText().toString());
        carInfoEntity.setCarTypeID(CarTypeID);
        carInfoEntity.setDischargeID(dischargeID);
        carInfoEntity.setCarSource(etCarSource.getText().toString());
        carInfoEntity.setOldLevel(etOldLevel.getText().toString());
        carInfoEntity.setDrivingStatus(etDrivingStatus.getText().toString());
        carInfoEntity.setVIN(etVin.getText().toString());
        carInfoEntity.setEngineNumber(etEngineNumber.getText().toString());
        carInfoEntity.setDLDate(tvDLDate.getText().toString());
        carInfoEntity.setDeviceUse(etRegisterTime.getText().toString());
        carInfoEntity.setqName(etQName.getText().toString());
        carInfoEntity.setsName(etSName.getText().toString());
        carInfoEntity.setProjectID(projectID);
        carInfoEntity.setYearTime(tvYearTime.getText().toString());
        carInfoEntity.setYearExprie(etYearExprie.getText().toString());
        carInfoEntity.setqStartTime(tvQStartTime.getText().toString());
        carInfoEntity.setqOverTime(tvQOverTime.getText().toString());
        carInfoEntity.setqCompany(etQCompany.getText().toString());
        carInfoEntity.setqAddress(etQAddress.getText().toString());
        carInfoEntity.setsStartTime(tvSStartTime.getText().toString());
        carInfoEntity.setsOverTime(tvSOverTime.getText().toString());
        carInfoEntity.setsCompany(etSCompany.getText().toString());
        carInfoEntity.setsAddress(etSAddress.getText().toString());
        carInfoEntity.setDischargeID(dischargeID);

//        Intent intent = new Intent(mActivity, YearInsuranceActivity.class);
//        intent.putExtra("carInfoEntity", carInfoEntity);
//        intent.putExtra("initQuantity", etInitQuantity.getText().toString());
//        intent.putExtra("initTravelKm", etInitTravelKm.getText().toString());
//        startActivity(intent);
        HashMap<String, String> params = new HashMap<>();
        params.put("carJson", new Gson().toJson(carInfoEntity));
        params.put("initQuantity", etInitQuantity.getText().toString());
        params.put("initTravelKm", etInitTravelKm.getText().toString());
        OkHttpManager.postFormBody(Urls.POST_ADDINCAR, params, tvCarType, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(mActivity, SuccessActivity.class);
                    intent.putExtra("TAG", 6);
                    startActivity(intent);
                    mActivity.finish();
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
        OkHttpManager.postFormBody(Urls.POST_GETCHARGETYPE, null, tvEmissionStandard, new OkHttpManager.OnResponse<String>() {
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
        OkHttpManager.postFormBody(Urls.POST_GETCARTYPE, null, tvCarType, new OkHttpManager.OnResponse<String>() {
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
    private String projectID;

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
        }
    }


    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int tag) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (tag == 1) {
                    tvDLDate.setText(DateUtil.getDate(date));
                } else if (tag == 3) {
                    tvYearTime.setText(DateUtil.getDate(date));
                } else if (tag == 4) {
                    tvQStartTime.setText(DateUtil.getDate(date));
                } else if (tag == 5) {
                    tvQOverTime.setText(DateUtil.getDate(date));
                } else if (tag == 6) {
                    tvSStartTime.setText(DateUtil.getDate(date));
                } else if (tag == 7) {
                    tvSOverTime.setText(DateUtil.getDate(date));
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
