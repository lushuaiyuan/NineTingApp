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
 * 单车维修统计
 */
public class OneCarMaintenanceStatisticActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_constructionAddress)
    TextView tvConstructionAddress;
    @BindView(R.id.tv_serviceType)
    TextView tvServiceType;
    @BindView(R.id.chart)
    LineChart mChart;
    private String carNumber;

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
        ChartUtils.initChart(mChart);
        ChartUtils.notifyDataSetChanged(mChart, getData(), ChartUtils.monthValue);
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
        tvTitle.setText("统计");
        carNumber = UIUtils.getStr4Intent(this, "carNumber");
        tvCarNumber.setText(carNumber);
    }

    @OnClick({R.id.iv_toolbarBack, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
            case R.id.tv_toolbarMenu:
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("carNumber", carNumber);
                intent.putExtra("TAG", 1);
                intent.putExtra("FLAG", 3);
                startActivity(intent);
                break;
        }
    }
}
