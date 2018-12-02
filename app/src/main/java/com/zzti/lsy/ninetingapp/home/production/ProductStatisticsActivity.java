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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.DynamicLineChartManager;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
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
    @BindView(R.id.tv_hint1)
    TextView tv_hint1;
    @BindView(R.id.chart2)
    LineChart mChart2;
    @BindView(R.id.tv_hint2)
    TextView tv_hint2;
    @BindView(R.id.chart3)
    LineChart mChart3;
    @BindView(R.id.tv_hint3)
    TextView tv_hint3;

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ConditionAdapter projectAdapter;
    private List<ConditionEntity> projectEntities;

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
        xData1.clear();
        xData2.clear();
        xData3.clear();
        yData1.clear();
        yData2.clear();
        yData3.clear();
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
                    List<ProductEntity.QuantityRecordsBean> quantityRecords = productEntity.getQuantityRecords();//生产量
                    List<ProductEntity.WearRecordsBean> wearRecords = productEntity.getWearRecords();//加油量
                    List<ProductEntity.ZratioRecordsBean> zratioRecords = productEntity.getZratioRecords();//油耗比
                    //生产量
                    if (quantityRecords != null && quantityRecords.size() > 0) {
                        for (int i = 0; i < quantityRecords.size(); i++) {
                            String format = quantityRecords.get(i).getTime().split("T")[0];
                            String quantity = quantityRecords.get(i).getQuantity();
                            xData1.add(format);
                            yData1.add(quantity);
                        }
                    }
                    if (xData1.size() == 0) {
                        tv_hint1.setVisibility(View.VISIBLE);
                        mChart1.setVisibility(View.GONE);
                    } else {
                        tv_hint1.setVisibility(View.GONE);
                        mChart1.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart1, ChartUtils.oneCar, xData1.size());
                        setLineChartDate(mChart1, xData1, yData1);
                    }
                    //加油量
                    if (wearRecords != null && wearRecords.size() > 0) {
                        for (int i = 0; i < wearRecords.size(); i++) {
                            String wear = wearRecords.get(i).getWear();
                            String format = wearRecords.get(i).getTime().split("T")[0];
                            xData2.add(format);
                            yData2.add(wear);
                        }
                    }
                    if (xData2.size() == 0) {
                        tv_hint2.setVisibility(View.VISIBLE);
                        mChart2.setVisibility(View.GONE);
                    } else {
                        tv_hint2.setVisibility(View.GONE);
                        mChart2.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart2, ChartUtils.oneCar, xData2.size());
                        setLineChartDate(mChart2, xData2, yData2);
                    }
                    //油耗比率
                    if (zratioRecords != null && zratioRecords.size() > 0) {
                        for (int i = 0; i < zratioRecords.size(); i++) {
                            String format = zratioRecords.get(i).getTime().split("T")[0];
                            String zratio = zratioRecords.get(i).getZratio();
                            xData3.add(format);
                            yData3.add(zratio);
                        }
                    }
                    if (xData1.size() == 0) {
                        tv_hint3.setVisibility(View.VISIBLE);
                        mChart3.setVisibility(View.GONE);
                    } else {
                        tv_hint3.setVisibility(View.GONE);
                        mChart3.setVisibility(View.VISIBLE);
                        ChartUtils.initChart(mChart3, ChartUtils.oneCar, xData3.size());
                        setLineChartDate(mChart3, xData3, yData3);
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


    private List<String> xData1 = new ArrayList<>();
    private List<String> yData1 = new ArrayList<>();

    private List<String> xData2 = new ArrayList<>();
    private List<String> yData2 = new ArrayList<>();

    private List<String> xData3 = new ArrayList<>();
    private List<String> yData3 = new ArrayList<>();

    private void setLineChartDate(LineChart mLineChart, final List<String> xData, List<String> yData) {
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
            lineDataSet.setColor(Color.WHITE);
            lineDataSet.setCircleColor(Color.parseColor("#AAFFFFFF"));
            lineDataSet.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
            lineDataSet.setHighlightEnabled(true);//是否使用点击高亮线
            lineDataSet.setDrawCircles(true);
            lineDataSet.setValueTextColor(Color.WHITE);
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
                        return xData.get(intValue);
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
