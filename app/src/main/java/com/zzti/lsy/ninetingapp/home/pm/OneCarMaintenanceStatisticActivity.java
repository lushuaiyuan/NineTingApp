package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单车维修统计
 */
public class OneCarMaintenanceStatisticActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
//    @BindView(R.id.tv_state)
//    TextView tvState;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
//    @BindView(R.id.tv_constructionAddress)
//    TextView tvConstructionAddress;
    @BindView(R.id.tv_serviceType)
    TextView tvServiceType;
    @BindView(R.id.chart)
    LineChart mLineChart;

    @Override
    public int getContentViewId() {
        return R.layout.activity_onecar_maintenancestatistic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        intiData();
    }

    private void intiData() {
        showDia();
        getRepaircount();


    }

    private void getRepaircount() {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", tvCarNumber.getText().toString());
        OkHttpManager.postFormBody(Urls.REPAIR_ONECARREPAIR, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
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
                        JSONArray data = new JSONArray(msgInfo.getData().toString());
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            xData.add(jsonObject.optString("time").substring(5, jsonObject.optString("time").length()));
                            yData.add(jsonObject.optString("money"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ChartUtils.initChart(mLineChart, ChartUtils.oneCar, xData.size(), Color.WHITE);
                    ChartUtils.setLineChartDate(mLineChart, xData, yData, Color.WHITE);
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

    private List<String> xData = new ArrayList<>();
    private List<String> yData = new ArrayList<>();

    private void setLineChartDate() {
        if (yData.size() == 0) return;
        List<Entry> mValues = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            Entry entry = new Entry(i, Float.valueOf(yData.get(i)), xData.get(i));
            mValues.add(entry);
        }
        //判断图表中原来是否有数据
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(mValues);
            //刷新数据
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            lineDataSet = new LineDataSet(mValues, "数据");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setColor(Color.WHITE);
            lineDataSet.setCircleColor(Color.WHITE);
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


    private CarInfoEntity carInfoEntity;

    private void initView() {
        tvTitle.setText("统计");
        carInfoEntity = (CarInfoEntity) getIntent().getSerializableExtra("carInfoEntity");
        tvCarNumber.setText(carInfoEntity.getPlateNumber());
//        if (TextUtils.equals(carInfoEntity.getStatus(), "0")) {
//            tvState.setTextColor(getResources().getColor(R.color.color_6bcfd6));
//            tvState.setText("存放中");
//        } else if (TextUtils.equals(carInfoEntity.getStatus(), "1")) {
//            tvState.setTextColor(getResources().getColor(R.color.color_ffb947));
//            tvState.setText("工作中");
//        } else if (TextUtils.equals(carInfoEntity.getStatus(), "2")) {
//            tvState.setTextColor(getResources().getColor(R.color.color_fe81b3));
//            tvState.setText("维修中");
//        }
        tvProjectAddress.setText(carInfoEntity.getProjectName());
        tvServiceType.setText(carInfoEntity.getVehicleTypeName());
    }

    @OnClick({R.id.iv_toolbarBack, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
            case R.id.tv_toolbarMenu:
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("carNumber", carInfoEntity.getPlateNumber());
//                intent.putExtra("TAG", 1);
                intent.putExtra("FLAG", 3);
                startActivity(intent);
                break;
        }
    }
}
