package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarStatusEntity;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.CarStatusAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.DeviceListAdapter;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.home.pm.ContrastActivity;
import com.zzti.lsy.ninetingapp.home.pm.OneCarMaintenanceStatisticActivity;
import com.zzti.lsy.ninetingapp.home.production.FormListActivity;
import com.zzti.lsy.ninetingapp.home.production.OneCarProDetailActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备列表
 */
public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.rl_searchCarNumber)
    RelativeLayout rlSearchCarNumber;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.rl_project)
    RelativeLayout rlProject;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_carStatus)
    RelativeLayout rlCarStatus;
    @BindView(R.id.tv_carStatus)
    TextView tvCarStatus;
    @BindView(R.id.rl_carType)
    RelativeLayout rlCarType;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.btn_contrast)
    Button btnContrast;

    //车辆状态
    private PopupWindow popupWindowCarStatus;
    private ListView mListViewCarStatus;
    private CarStatusAdapter carStatusAdapter;
    private List<CarStatusEntity> carStatusEntities;
    //车辆类型pop
    private PopupWindow popupWindowCarType;
    private ListView mListViewCarType;
    private CarTypeAdapter carTypeAdapter;
    private List<CarTypeEntity> carTypeEntities;
    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    private DeviceListAdapter deviceListAdapter;
    private List<CarInfoEntity> carInfoEntities;
    private int flag = -1; //-1默认进入到详情界面 1代表获取车牌号 2代表进入单车统计界面 3代表选中 4代表进入单车生产统计
    private String carNumber;//对比的基准车牌号
    private int type;//查询条件
    private String wherestr = "";//查询条件
    private String status;//状态id
    private String CarTypeID;//类型id
    private String projectID;//项目部id
    private int pageIndex = 1;//页码

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initCarStatusPop();
        initCarTypePop();
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理角色
            initProjectPop();
        }
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initProjectPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, UIUtils.getWidth(this) - DensityUtils.dp2px(32), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
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

    private void initCarTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarType = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarType.setFocusable(true);
        popupWindowCarType.setOutsideTouchable(true);
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
        carTypeAdapter.setTag(1);
        mListViewCarType.setAdapter(carTypeAdapter);
        mListViewCarType.setOnItemClickListener(this);
        popupWindowCarType.setAnimationStyle(R.style.anim_upPop);
    }

    private void initCarStatusPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarStatus = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarStatus.setFocusable(true);
        popupWindowCarStatus.setOutsideTouchable(true);
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
        carStatusAdapter.setTag(1);
        mListViewCarStatus.setOnItemClickListener(this);
        popupWindowCarStatus.setAnimationStyle(R.style.anim_upPop);
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        carInfoEntities = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(carInfoEntities);
        mRecycleView.setAdapter(deviceListAdapter);
        deviceListAdapter.setOnItemClickListener(this);
        deviceListAdapter.setFlag(flag);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                wherestr = "";
                pageIndex = 1;
                etSearch.setText("");
                tvCarStatus.setText("车辆状态");
                status = "";
                tvCarType.setText("车辆类型");
                CarTypeID = "";
                tvProject.setText("项目部");
                projectID = "";
                carInfoEntities.clear();
                getCarList();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getCarList();
            }
        });
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getCarList();
            getCarType();
            if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理角色
                getProject();
            }
        }
    }


    /**
     * 获取车辆列表
     */
    private void getCarList() {
        HashMap<String, String> params = new HashMap<>();
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        params.put("pageIndex", String.valueOf(pageIndex));
        OkHttpManager.postFormBody(Urls.POST_GETCARLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(smartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CarInfoEntity carInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), CarInfoEntity.class);
                            if (flag == 3) {
                                if (carInfoEntity.getPlateNumber().equals(carNumber)) {
                                    carInfoEntity.setCheck(true);
                                } else {
                                    carInfoEntity.setCheck(false);
                                }
                            }
                            carInfoEntities.add(carInfoEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                deviceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }

    private void initView() {
        setTitle("设备列表");
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理才显示项目部的查询条件
            rlProject.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        } else {
            rlProject.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
//        int tag = UIUtils.getInt4Intent(this, "TAG");
//        if (tag == 1) {
//            tvToolbarMenu.setVisibility(View.GONE);
//            rlSearchCarNumber.setVisibility(View.GONE);
//            llCondition.setVisibility(View.GONE);
//        } else {
        tvToolbarMenu.setVisibility(View.VISIBLE);
        tvToolbarMenu.setText("表格");
//            rlSearchCarNumber.setVisibility(View.VISIBLE);
//            llCondition.setVisibility(View.VISIBLE);
//        }
        flag = UIUtils.getInt4Intent(this, "FLAG");
        carNumber = UIUtils.getStr4Intent(this, "carNumber");
        carSelect.add(carNumber);
        if (flag == 3) {
            btnContrast.setVisibility(View.VISIBLE);
        } else {
            btnContrast.setVisibility(View.GONE);
        }
    }

    private ArrayList<String> carSelect = new ArrayList<>();

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (flag == -1) {
            Intent intent = new Intent(this, DeviceDetailActivity.class);
            intent.putExtra("TAG", 0);
            intent.putExtra("carNumber", carInfoEntities.get(position).getPlateNumber());
            startActivity(intent);
        } else if (flag == 1) {
            Intent intent = new Intent();
            intent.putExtra("carNumber", carInfoEntities.get(position).getPlateNumber());
            setResult(2, intent);
            finish();
        } else if (flag == 2) {
            Intent intent = new Intent(this, OneCarMaintenanceStatisticActivity.class);
            intent.putExtra("carInfoEntity", carInfoEntities.get(position));
            startActivity(intent);
        } else if (flag == 3) {
            if (!carInfoEntities.get(position).getPlateNumber().equals(carNumber)) {
                if (carInfoEntities.get(position).isCheck()) {
                    carInfoEntities.get(position).setCheck(false);
                    deviceListAdapter.notifyDataSetChanged();
                    carSelect.remove(carInfoEntities.get(position).getPlateNumber());
                } else {
                    if (carSelect.size() < 3) {
                        carInfoEntities.get(position).setCheck(true);
                        deviceListAdapter.notifyDataSetChanged();
                        carSelect.add(carInfoEntities.get(position).getPlateNumber());
                    } else {
                        UIUtils.showT("每次最多只能对比三个");
                    }
                }
            }
        } else if (flag == 4) {
            Intent intent = new Intent(this, OneCarProDetailActivity.class);
            intent.putExtra("plateNumber", carInfoEntities.get(position).getPlateNumber());
            intent.putExtra("projectName", carInfoEntities.get(position).getProjectName());
            startActivity(intent);
        }
    }

    @OnClick({R.id.iv_search, R.id.rl_project, R.id.rl_carStatus, R.id.rl_carType, R.id.tv_toolbarMenu, R.id.btn_contrast})
    public void viewClick(View view) {
        hideSoftInput(etSearch);
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("请输入车牌号");
                    break;
                }
                if (!UIUtils.validateCarNum(etSearch.getText().toString())) {
                    UIUtils.showT("车牌号格式不正确");
                    break;
                }
                wherestr = "";
                if (!StringUtil.isNullOrEmpty(CarTypeID)) {
                    wherestr += " and CarTypeID=" + CarTypeID;
                }
                if (!StringUtil.isNullOrEmpty(projectID)) {
                    wherestr += " and projectID=" + projectID;
                }
                if (!StringUtil.isNullOrEmpty(status)) {
                    wherestr += " and status=" + status;
                }
                wherestr += " and plateNumber=" + "\"" + etSearch.getText().toString() + "\"";
                showDia();
                carInfoEntities.clear();
                getCarList();
                break;
            case R.id.rl_project://选择项目部
                type = 3;
                if (projectEntities.size() > 0) {
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
                    popupWindowProject.showAtLocation(rlProject, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT("暂无数据");
                }
                break;
            case R.id.rl_carStatus:
                type = 1;
                showCarStatus();
                break;
            case R.id.rl_carType:
                type = 2;
                if (carTypeEntities.size() > 0) {
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
                    popupWindowCarType.showAsDropDown(rlCarType, 0, 0, Gravity.LEFT);
                } else {
                    UIUtils.showT("暂无数据");
                }
                break;
            case R.id.tv_toolbarMenu:
                if (flag == 4) {
                    startActivity(new Intent(this, FormListActivity.class));
                } else {
                    startActivity(new Intent(this, DeviceFormActivity.class));
                }
                break;
            case R.id.btn_contrast:
                if (carSelect.size() == 1) {
                    UIUtils.showT("最少选择两条数据进行对比");
                } else {
                    Intent intent = new Intent(this, ContrastActivity.class);
                    intent.putStringArrayListExtra("carSelect", carSelect);
                    startActivity(intent);
                }
                break;
        }
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
                    carTypeEntities.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CarTypeEntity carTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), CarTypeEntity.class);
                            carTypeEntities.add(carTypeEntity);
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
                    projectEntities.clear();
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
        popupWindowCarStatus.showAsDropDown(rlCarStatus, 0, 0, Gravity.LEFT);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        if (type == 1) {
            tvCarStatus.setText(carStatusEntities.get(i).getName());
            status = carStatusEntities.get(i).getId();
            wherestr += " and status=" + status;
            if (!StringUtil.isNullOrEmpty(CarTypeID)) {
                wherestr += " and CarTypeID=" + CarTypeID;
            }
            if (!StringUtil.isNullOrEmpty(projectID)) {
                wherestr += " and projectID=" + projectID;
            }
            popupWindowCarStatus.dismiss();
        } else if (type == 2) {
            tvCarType.setText(carTypeEntities.get(i).getVehicleTypeName());
            CarTypeID = carTypeEntities.get(i).getVehicleTypeID();
            wherestr += " and CarTypeID=" + carTypeEntities.get(i).getVehicleTypeID();
            if (!StringUtil.isNullOrEmpty(status)) {
                wherestr += " and status=" + status;
            }
            if (!StringUtil.isNullOrEmpty(projectID)) {
                wherestr += " and projectID=" + projectID;
            }
            popupWindowCarType.dismiss();
        } else if (type == 3) {
            tvProject.setText(projectEntities.get(i).getProjectName());
            projectID = projectEntities.get(i).getProjectID();
            wherestr += " and projectID=" + projectEntities.get(i).getProjectID();
            if (!StringUtil.isNullOrEmpty(status)) {
                wherestr += " and status=" + status;
            }
            if (!StringUtil.isNullOrEmpty(CarTypeID)) {
                wherestr += " and CarTypeID=" + CarTypeID;
            }
            popupWindowProject.dismiss();
        }
        if (!StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            wherestr += " and plateNumber=" + "\"" + etSearch.getText().toString() + "\"";
        }
        showDia();
        carInfoEntities.clear();
        getCarList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endRefresh(smartRefreshLayout);
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.B) {
            wherestr = "";
            pageIndex = 1;
            etSearch.setText("");
            tvCarStatus.setText("车辆状态");
            status = "";
            tvCarType.setText("车辆类型");
            CarTypeID = "";
            tvProject.setText("项目部");
            projectID = "";
            carInfoEntities.clear();
            showDia();
            getCarList();
        }
    }
}
