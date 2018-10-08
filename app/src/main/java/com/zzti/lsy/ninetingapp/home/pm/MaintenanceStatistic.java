package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修统计
 */
public class MaintenanceStatistic extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tv_monthMaintenance_RecordAmount)
    TextView tvMonthMaintenanceRecordAmount;
    @BindView(R.id.tv_monthState)
    TextView tvMonthState;
    @BindView(R.id.tv_quarterMaintenance_RecordAmount)
    TextView tvQuarterMaintenanceRecordAmount;
    @BindView(R.id.tv_quarterState)
    TextView tvQuarterState;
    @BindView(R.id.chart)
    LineChart chart;

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
        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(), ChartUtils.monthValue);
    }

    private List<Entry> getData() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < DateUtil.getCurrentDay() - 1; i++) {
            Entry entry = new Entry();
            entry.setX(i);
            entry.setY(15 + i);
            entries.add(entry);
        }
        return entries;
    }

    private void initView() {
        tvTitle.setText("维修统计");
    }


    @OnClick({R.id.iv_toolbarBack, R.id.tv_lookOneCar_maintenanceRecord, R.id.tv_toolbarMenu, R.id.ll_monthMaintenance_Record, R.id.ll_quarterMaintenance_Record})
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
            case R.id.ll_monthMaintenance_Record://查看本月维修记录
                UIUtils.showT("查看本月维修记录");
                break;
            case R.id.ll_quarterMaintenance_Record://查看本季度维修记录
                UIUtils.showT("查看本季度维修记录");
                break;

        }
    }
}
