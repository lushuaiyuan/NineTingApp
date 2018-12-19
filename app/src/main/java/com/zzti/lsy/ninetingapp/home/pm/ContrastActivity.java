package com.zzti.lsy.ninetingapp.home.pm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bin.david.form.annotation.SmartTable;
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
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
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
 * 对比
 */
public class ContrastActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitleView;
    @BindView(R.id.tv_carNumber1)
    TextView tvCarNumber1;
    @BindView(R.id.chart1)
    LineChart mLineChart1;
    @BindView(R.id.tv_carNumber2)
    TextView tvCarNumber2;
    @BindView(R.id.chart2)
    LineChart mLineChart2;
    @BindView(R.id.tv_carNumber3)
    TextView tvCarNumber3;
    @BindView(R.id.chart3)
    LineChart mLineChart3;

    @Override
    public int getContentViewId() {
        return R.layout.activity_contrast;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        ArrayList<String> carSelect = getIntent().getStringArrayListExtra("carSelect");
        if (carSelect.size() == 2) {
            tvCarNumber1.setText(carSelect.get(0));
            tvCarNumber2.setText(carSelect.get(1));
            showDia();
            getRepaircount(tvCarNumber1.getText().toString());
            showDia();
            getRepaircount(tvCarNumber2.getText().toString());
            tvCarNumber3.setVisibility(View.GONE);
            mLineChart3.setVisibility(View.GONE);
        } else if (carSelect.size() == 3) {
            tvCarNumber1.setText(carSelect.get(0));
            tvCarNumber2.setText(carSelect.get(1));
            tvCarNumber3.setText(carSelect.get(2));
            showDia();
            getRepaircount(tvCarNumber1.getText().toString());
            showDia();
            getRepaircount(tvCarNumber2.getText().toString());
            showDia();
            getRepaircount(tvCarNumber3.getText().toString());
        }

    }

    private List<String> xData1 = new ArrayList<>();
    private List<String> yData1 = new ArrayList<>();

    private List<String> xData2 = new ArrayList<>();
    private List<String> yData2 = new ArrayList<>();

    private List<String> xData3 = new ArrayList<>();
    private List<String> yData3 = new ArrayList<>();

    private void getRepaircount(final String plateNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", plateNumber);
        OkHttpManager.postFormBody(Urls.REPAIR_ONECARREPAIR, params, tvCarNumber1, new OkHttpManager.OnResponse<String>() {
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
                            if (plateNumber.equals(tvCarNumber1.getText().toString())) {
                                xData1.add(jsonObject.optString("time").substring(5, jsonObject.optString("time").length()));
                                yData1.add(jsonObject.optString("money"));
                                ChartUtils.initChart(mLineChart1, ChartUtils.oneCar, xData1.size(), Color.WHITE);
                                setLineChartDate(mLineChart1, xData1, yData1, Color.WHITE);
                            } else if (plateNumber.equals(tvCarNumber2.getText().toString())) {
                                xData2.add(jsonObject.optString("time").substring(5, jsonObject.optString("time").length()));
                                yData2.add(jsonObject.optString("money"));
                                ChartUtils.initChart(mLineChart2, ChartUtils.oneCar, xData2.size(), Color.BLUE);
                                setLineChartDate(mLineChart2, xData2, yData2, Color.BLUE);
                            } else if (plateNumber.equals(tvCarNumber3.getText().toString())) {
                                xData3.add(jsonObject.optString("time").substring(5, jsonObject.optString("time").length()));
                                yData3.add(jsonObject.optString("money"));
                                ChartUtils.initChart(mLineChart3, ChartUtils.oneCar, xData3.size(), Color.YELLOW);
                                setLineChartDate(mLineChart3, xData3, yData3, Color.YELLOW);
                            }
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

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }


    private void setLineChartDate(LineChart mLineChart, final List<String> xData, List<String> yData, int color) {
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


    private void initView() {
        tvTitleView.setText("统计对比");
    }

    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
