package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairCoutEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修统计
 */
public class MaintenanceStatisticActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tv_repairAmount)
    TextView tvRepairAmount;
    @BindView(R.id.tv_repairMoney)
    TextView tvRepairMoney;
    @BindView(R.id.chart)
    LineChart mLineChart;
    private List<RepairCoutEntity.RepairStatisticInfo> repairStatisticInfos;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_statistic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        intData();
    }

    private void intData() {
        showDia();
        getRepaircount();
    }

    private void getRepaircount() {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", "");
        OkHttpManager.postFormBody(Urls.REPAIR_GETREPAIRCOUNT, params, tvRepairAmount, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                boolean hasData = false;
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    tvRepairAmount.setText(msgInfo.getMsg());
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData().toString());
                        float totalMoney = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            hasData = true;
                            RepairCoutEntity.RepairStatisticInfo repairStatisticInfo = ParseUtils.parseJson(jsonArray.getString(i), RepairCoutEntity.RepairStatisticInfo.class);
                            repairStatisticInfos.set(Integer.parseInt(repairStatisticInfo.getTime()) - 1, repairStatisticInfo);
                            totalMoney += Float.parseFloat(repairStatisticInfo.getMoney());
                        }
                        tvRepairMoney.setText(String.valueOf(totalMoney));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ChartUtils.initChart(mLineChart, ChartUtils.allCar, 12);
                    setLineChartDate(hasData);
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

    private void setLineChartDate(boolean hasData) {
        if (!hasData) return;
        List<Entry> mValues = new ArrayList<>(12);
        for (int i = 0; i < repairStatisticInfos.size(); i++) {
            Entry entry = new Entry(Integer.parseInt(repairStatisticInfos.get(i).getTime()), Float.parseFloat(repairStatisticInfos.get(i).getMoney()));
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
            lineDataSet = new LineDataSet(mValues, "测试数据1");
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
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            lineDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
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
        tvTitle.setText("维修统计");
        repairStatisticInfos = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            repairStatisticInfos.add(new RepairCoutEntity.RepairStatisticInfo(String.valueOf(i), "0"));
        }
    }


    @OnClick({R.id.iv_toolbarBack, R.id.tv_lookOneCar_maintenanceRecord, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
            case R.id.tv_lookOneCar_maintenanceRecord://查看单车维修统计
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("TAG", 1);
                intent.putExtra("FLAG", 2);
                startActivity(intent);
                break;
            case R.id.tv_toolbarMenu://查看报表
                startActivity(new Intent(this, MaintenanceReportActivity.class));
                break;
        }
    }
}
