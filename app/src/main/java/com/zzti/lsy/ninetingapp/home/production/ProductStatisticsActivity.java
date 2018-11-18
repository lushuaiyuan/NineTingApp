package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProductEntity;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsOutDetailActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.DynamicLineChartManager;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2018/11/18 16:27
 * @Describe 生产统计
 */
public class ProductStatisticsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_productAmount)
    TextView tvProductAmount;
    @BindView(R.id.tv_oilMassAmount)
    TextView tvOilMassAmount;
    @BindView(R.id.tv_oilMassRatio)
    TextView tvOilMassRatio;
    @BindView(R.id.chart1)
    LineChart mChart1;
    @BindView(R.id.chart2)
    LineChart mChart2;

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ConditionAdapter projectAdapter;
    private List<ConditionEntity> projectEntities;


    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Float> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> color = new ArrayList<>();//折线颜色集合

    @Override
    public int getContentViewId() {
        return R.layout.activity_product_static;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initProjectPop();
        initData();
    }

    private void initProjectPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
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
        projectAdapter = new ConditionAdapter(projectEntities);
        mListViewProject.setAdapter(projectAdapter);
        mListViewProject.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_upPop);
    }

    private void initData() {
        //折线名字
        names.add("生产量");
        names.add("加油量");
        //折线颜色
        color.add(R.color.color_ff80b4);
        color.add(R.color.color_6bcfd7);

        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names, color);
        dynamicLineChartManager2 = new DynamicLineChartManager(mChart2, "油耗率", R.color.color_6bcfd6);

        showDia();
        getProject();
        getData();
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
                            ConditionEntity conditionEntity = new ConditionEntity();
                            conditionEntity.setId(projectEntity.getProjectID());
                            conditionEntity.setName(projectEntity.getProjectName());
                            projectEntities.add(conditionEntity);
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

    private void initView() {
        setTitle("生产统计");
        tvEndTime.setText(DateUtil.getCurrentDate());
        tvStartTime.setText(DateUtil.getAfterMonth(DateUtil.getCurrentDate(), -1));
    }

    @OnClick({R.id.tv_project, R.id.tv_startTime, R.id.tv_endTime, R.id.btn_search, R.id.btn_oneCar})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_project:
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
                    popupWindowProject.showAsDropDown(tvProject, 0, 0, Gravity.BOTTOM);
                } else {
                    UIUtils.showT("暂无数据");
                }
                break;
            case R.id.tv_startTime:
                type = 1;
                showCustomTime();
                break;
            case R.id.tv_endTime:
                type = 2;
                showCustomTime();
                break;
            case R.id.btn_search:
                getData();
                break;
            case R.id.btn_oneCar:
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("FLAG", 4);
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        if (StringUtil.isNullOrEmpty(projectID))
            projectID = "";
        params.put("projectID", projectID);
        params.put("StarTime", tvStartTime.getText().toString());
        params.put("OverTime", tvEndTime.getText().toString());
        OkHttpManager.postFormBody(Urls.RECORD_GETPROJECTRECORD, params, tvOilMassAmount, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    ProductEntity productEntity = ParseUtils.parseJson(msgInfo.getData(), ProductEntity.class);
                    tvProductAmount.setText(productEntity.getQuantity());
                    tvOilMassAmount.setText(productEntity.getWear());
                    tvOilMassRatio.setText(productEntity.getZratio());
                    showChart(productEntity);

                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    /**
     * @param productEntity
     */
    private void showChart(ProductEntity productEntity) {
        List<ProductEntity.QuantityRecordsBean> quantityRecords = productEntity.getQuantityRecords();//生产量
        List<ProductEntity.WearRecordsBean> wearRecords = productEntity.getWearRecords();//加油量
        List<ProductEntity.ZratioRecordsBean> zratioRecords = productEntity.getZratioRecords();//油耗比

        //生产量
        for (int i = 0; i < quantityRecords.size(); i++) {
            float quantity = (float) quantityRecords.get(i).getQuantity();
            list.add(quantity);
        }
        List<Float> zratios = new ArrayList<>();
        for (int i = 0; i < zratioRecords.size(); i++) {
            float zratio = (float) zratioRecords.get(i).getZratio();
            zratios.add(zratio);
        }
        dynamicLineChartManager1.setYAxis(100, 0, 10);//生产量和加油量
        dynamicLineChartManager2.setYAxis(Collections.max(zratios), 0, 10);//油耗比

        dynamicLineChartManager1.addEntry(list);
        dynamicLineChartManager2.addEntry(zratioRecords.size());

    }

    private int type = 1;

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(ProductStatisticsActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (type == 1) {
                    tvStartTime.setText(DateUtil.getDate(date));
                } else {
                    tvEndTime.setText(DateUtil.getDate(date));
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

    private String projectID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvProject.setText(projectEntities.get(i).getName());
        projectID = projectEntities.get(i).getId();
        popupWindowProject.dismiss();
    }
}
