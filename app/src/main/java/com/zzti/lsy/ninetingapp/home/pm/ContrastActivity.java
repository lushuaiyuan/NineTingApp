package com.zzti.lsy.ninetingapp.home.pm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bin.david.form.annotation.SmartTable;
import com.github.mikephil.charting.charts.LineChart;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

import java.util.ArrayList;

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
    LineChart mChart1;
    @BindView(R.id.tv_carNumber2)
    TextView tvCarNumber2;
    @BindView(R.id.chart2)
    LineChart mChart2;
    @BindView(R.id.tv_carNumber3)
    TextView tvCarNumber3;
    @BindView(R.id.chart3)
    LineChart mChart3;

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
            tvCarNumber3.setVisibility(View.GONE);
            mChart3.setVisibility(View.GONE);
        } else if (carSelect.size() == 3) {
            tvCarNumber1.setText(carSelect.get(0));
            tvCarNumber2.setText(carSelect.get(1));
            tvCarNumber3.setText(carSelect.get(2));
        }
    }

    private void initView() {
        tvTitleView.setText("统计");
    }

    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
