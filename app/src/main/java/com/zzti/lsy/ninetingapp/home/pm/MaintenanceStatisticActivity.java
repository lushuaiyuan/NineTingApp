package com.zzti.lsy.ninetingapp.home.pm;

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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProductEntity;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.entity.RepairEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.production.ProductStatisticsActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
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

/**
 * 维修统计
 */
public class MaintenanceStatisticActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;

    @BindView(R.id.tv_moneyChange)
    TextView tvMoneyChange;//维修费用变动
    @BindView(R.id.tv_amountChange)
    TextView tvAmountChange;//生产量变动
    @BindView(R.id.tv_totalMoneyChange)
    TextView tvTotalMoneyChange;//综合维修费变动
    @BindView(R.id.chart1)
    LineChart mChart1;
    @BindView(R.id.tv_hint1)
    TextView tvHint1;
    @BindView(R.id.chart2)
    LineChart mChart2;
    @BindView(R.id.tv_hint2)
    TextView tvHint2;
    @BindView(R.id.chart3)
    LineChart mChart3;
    @BindView(R.id.tv_hint3)
    TextView tvHint3;

    private List<String> xData1 = new ArrayList<>();
    private List<String> yData1 = new ArrayList<>();
    private List<String> xData2 = new ArrayList<>();
    private List<String> yData2 = new ArrayList<>();
    private List<String> xData3 = new ArrayList<>();
    private List<String> yData3 = new ArrayList<>();
    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ConditionAdapter projectAdapter;
    private List<ConditionEntity> projectEntities;


    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_statistic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initProjectPop();
        intData();
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


    private void intData() {
        showDia();
        getProject();
        getRepaircount();
    }


    private void getRepaircount() {
        xData1.clear();
        yData1.clear();
        xData2.clear();
        yData2.clear();
        xData3.clear();
        yData3.clear();
        if (StringUtil.isNullOrEmpty(projectID))
            projectID = "";
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", projectID);
        params.put("StarTime", tvStartTime.getText().toString() + "-01");
        params.put("OverTime", tvEndTime.getText().toString() + "-" + DateUtil.getDaysOfMonth(tvEndTime.getText().toString()));

        OkHttpManager.postFormBody(Urls.REPAIR_GETREPAIRCOUNT, params, tvStartTime, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    RepairEntity repairEntity = ParseUtils.parseJson(msgInfo.getData().toString(), RepairEntity.class);
                    tvMoneyChange.setText(repairEntity.getAllRepairMoney());
                    tvAmountChange.setText(repairEntity.getAllQuantity());
                    tvTotalMoneyChange.setText(repairEntity.getScale());
                    List<RepairEntity.RepairMoneysBean> repairMoneys = repairEntity.getRepairMoneys();//总维修费
                    List<RepairEntity.QuantityRecordsBean> quantityRecords = repairEntity.getQuantityRecords();//总生产量
                    List<RepairEntity.ScalesBean> scales = repairEntity.getScales();//总综合维修费
                    //总维修费
                    if (repairMoneys != null && repairMoneys.size() > 0) {
                        for (int i = 0; i < repairMoneys.size(); i++) {
                            String time = repairMoneys.get(i).getTime().split("T")[0];
                            String money = repairMoneys.get(i).getMoney();
                            xData1.add(time);
                            yData1.add(money);
                        }
                    }
                    if (xData1.size() == 0) {
                        tvHint1.setVisibility(View.VISIBLE);
                        mChart1.setVisibility(View.GONE);
                    } else {
                        tvHint1.setVisibility(View.GONE);
                        mChart1.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart1, ChartUtils.oneCar, xData1.size(), Color.WHITE);
                        setLineChartDate(mChart1, xData1, yData1, Color.WHITE);
                    }
                    //生产量
                    if (quantityRecords != null && quantityRecords.size() > 0) {
                        for (int i = 0; i < quantityRecords.size(); i++) {
                            String time = quantityRecords.get(i).getTime().split("T")[0];
                            String quantity = quantityRecords.get(i).getQuantity();
                            xData2.add(time);
                            yData2.add(quantity);
                        }
                    }
                    if (xData2.size() == 0) {
                        tvHint2.setVisibility(View.VISIBLE);
                        mChart2.setVisibility(View.GONE);
                    } else {
                        tvHint2.setVisibility(View.GONE);
                        mChart2.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart2, ChartUtils.oneCar, xData2.size(), Color.BLUE);
                        setLineChartDate(mChart2, xData2, yData2, Color.BLUE);
                    }
                    //总综合维修费
                    if (scales != null && scales.size() > 0) {
                        for (int i = 0; i < scales.size(); i++) {
                            String time = scales.get(i).getTime().split("T")[0];
                            String scale = scales.get(i).getScale();
                            xData3.add(time);
                            yData3.add(scale);
                        }
                    }
                    if (xData3.size() == 0) {
                        tvHint3.setVisibility(View.VISIBLE);
                        mChart3.setVisibility(View.GONE);
                    } else {
                        tvHint3.setVisibility(View.GONE);
                        mChart3.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart3, ChartUtils.oneCar, xData3.size(), Color.YELLOW);
                        setLineChartDate(mChart3, xData3, yData3, Color.YELLOW);
                    }

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

    private void setLineChartDate(LineChart mLineChart, final List<String> xData, List<String> yData, int color) {
        List<Entry> mValues = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            Entry entry = new Entry(i, Float.valueOf(yData.get(i)), xData.get(i));
            mValues.add(entry);
        }
        //判断图表中原来是否有数据
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            if (mValues.size() == 0) {
                lineDataSet = new LineDataSet(mValues, "数据");
            } else {
                //获取数据1
                lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            }
            lineDataSet.setValues(mValues);
            //刷新数据
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            lineDataSet = new LineDataSet(mValues, "数据");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            lineDataSet.setHighLightColor(color);//设置点击交点后显示交高亮线的颜色
            lineDataSet.setHighlightEnabled(true);//是否使用点击高亮线
            lineDataSet.setDrawCircles(true);
            lineDataSet.setValueTextColor(color);
            lineDataSet.setLineWidth(1f);//设置线宽
            lineDataSet.setCircleRadius(2f);//设置焦点圆心的大小
            lineDataSet.setHighlightLineWidth(0.5f);//设置点击交点后显示高亮线宽
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            lineDataSet.setValueTextSize(12f);//设置显示值的文字大小
            lineDataSet.setDrawFilled(true);//设置使用 范围背景填充

            lineDataSet.setDrawValues(false);
            //格式化显示数据
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    //对X轴上的值进行Format格式化，转成相应的值
                    int intValue = (int) value;
                    //筛选出自己需要的值，一般都是这样写没问题，并且一定要加上这个判断，不然会出错
                    if (xData.size() > intValue && intValue >= 0) {
                        //这样显示在X轴上值就是 05:30  05:35，不然会是1.0  2.0
                        return String.valueOf(xData.get(intValue));
                    } else {
                        return "";
                    }
                }
            });
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet); // add the datasets
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            mLineChart.setData(data);
            //绘制图表
            mLineChart.invalidate();
        }
    }


    private void initView() {
        setTitle("维修统计");
        tvToolbarMenu.setVisibility(View.VISIBLE);
        tvToolbarMenu.setText("查看报表");
        tvToolbarMenu.setOnClickListener(this);
        tvEndTime.setText(DateUtil.getCurYear() + "-" + (DateUtil.getCurMonth() + 1));
        tvStartTime.setText(DateUtil.getAfterMonth(tvEndTime.getText().toString(), -6));

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
                        ConditionEntity conditionEntity1 = new ConditionEntity("所有项目部", "");
                        projectEntities.add(conditionEntity1);
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


    @OnClick({R.id.tv_project, R.id.tv_startTime, R.id.tv_endTime, R.id.tv_lookOneCar_maintenanceRecord, R.id.btn_search})
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
                showCustomTime(1);
                break;
            case R.id.tv_endTime:
                showCustomTime(2);
                break;
            case R.id.btn_search:
                getRepaircount();
                break;

            case R.id.tv_lookOneCar_maintenanceRecord://查看单车维修统计
                Intent intent = new Intent(this, DeviceListActivity.class);
//                intent.putExtra("TAG", 1);
                intent.putExtra("FLAG", 2);
                startActivity(intent);
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int type) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(MaintenanceStatisticActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (type == 1) {
                    tvStartTime.setText(DateUtil.getDateMonth(date));
                } else {
                    tvEndTime.setText(DateUtil.getDateMonth(date));
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, false, false, false, false})
                .setLabel(" 年", " 月", "", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }


    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, MaintenanceReportActivity.class));
    }

    private String projectID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvProject.setText(projectEntities.get(i).getName());
        projectID = projectEntities.get(i).getId();
        popupWindowProject.dismiss();
    }
}
