package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备详情
 */
public class CarDetailFragment extends BaseFragment implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_carType)
    TextView tvCarType;//车辆类型
    @BindView(R.id.tv_emission_standard)
    TextView tvEmissionStandard;//排放量
    @BindView(R.id.tv_address)
    TextView tvAddress;//存放地点
    @BindView(R.id.tv_map)
    TextView tvMap;//查看地图
    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer;//生产厂家
    @BindView(R.id.tv_vin)
    TextView tvVin;//识别码
    @BindView(R.id.tv_engine_number)
    TextView tvEngineNumber;//发动机编号
    @BindView(R.id.tv_buyTime)
    TextView tvBuyTime;//购买日期
    @BindView(R.id.tv_buyMoney)
    TextView tvBuyMoney;//购买金额
    @BindView(R.id.tv_drivingNumber)
    TextView tvDrivingNumber;//行驶证号码
    @BindView(R.id.tv_drivingNumber_giveTime)
    TextView tvDrivingNumberGiveTime;//行驶证发放日期
    @BindView(R.id.tv_drivingNumber_validityTime)
    TextView tvDrivingNumberValidityTime;//行驶证有效期

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    private CarInfoEntity carInfoEntity;
    private String beforeProjectID;
    private String beforeProjectName;

    public static CarDetailFragment newInstance() {
        CarDetailFragment fragment = new CarDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            carInfoEntity = (CarInfoEntity) arguments.getSerializable("carInfoEntity");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_detail;
    }

    @Override
    protected void initView() {
        initPop();
        tvCarType.setText(carInfoEntity.getVehicleTypeName());
        tvEmissionStandard.setText(carInfoEntity.getDischargeName());
        tvAddress.setText(carInfoEntity.getProjectName());
        tvManufacturer.setText(carInfoEntity.getFactoryName());
        tvVin.setText(carInfoEntity.getVIN());
        tvEngineNumber.setText(carInfoEntity.getEngineNumber());
        tvBuyTime.setText(carInfoEntity.getPurchaseDate().split("T")[0]);
        tvBuyMoney.setText(carInfoEntity.getPrice());
        tvDrivingNumber.setText(carInfoEntity.getDrivingLicenseNumber());
        tvDrivingNumberGiveTime.setText(carInfoEntity.getDLDate().split("T")[0]);
        tvDrivingNumberValidityTime.setText(carInfoEntity.getDLValidDate().split("T")[0]);
        beforeProjectID = carInfoEntity.getProjectID();
        beforeProjectName = carInfoEntity.getProjectName();
    }

    private void initPop() {
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

    @Override
    protected void initData() {
        showDia();
        getProject();
    }

    /**
     * 获取项目部
     */
    private void getProject() {
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, null, tvAddress, new OkHttpManager.OnResponse<String>() {
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

    @OnClick({R.id.tv_map, R.id.btn_save, R.id.tv_address})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_map:
                startActivity(new Intent(mActivity, MapActivity.class));
                break;
            case R.id.tv_address:
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
                    setBackgroundAlpha(0.5f);
                    popupWindowProject.showAtLocation(tvAddress, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(C.Constant.NODATA);
                }
                break;
            case R.id.btn_save:
                if (StringUtil.isNullOrEmpty(projectID)) {
                    UIUtils.showT("请选择不同的项目部");
                    break;
                }
                MAlertDialog.show(mActivity, "提示", "是否保存数据？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        saveData();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    private void saveData() {
        showDia();
        carInfoEntity.setProjectID(projectID);
        carInfoEntity.setProjectName(tvAddress.getText().toString());
        HashMap<String, String> params = new HashMap<>();
        params.put("carJson", new Gson().toJson(carInfoEntity));
        OkHttpManager.postFormBody(Urls.POST_UPDATCARINFO, params, tvAddress, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    UIUtils.showT("修改成功");
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.B, true));
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    carInfoEntity.setProjectName(beforeProjectName);
                    carInfoEntity.setProjectID(beforeProjectID);
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                carInfoEntity.setProjectName(beforeProjectName);
                carInfoEntity.setProjectID(beforeProjectID);
            }
        });
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private String projectID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvAddress.setText(projectEntities.get(i).getProjectName());
        projectID = projectEntities.get(i).getProjectID();
        popupWindowProject.dismiss();
    }
}
