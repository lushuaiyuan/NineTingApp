package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

import butterknife.OnClick;

/**
 * author：anxin on 2018/8/7 16:26
 * 单车生产详情
 */
public class OneCarProDetailActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_onecar_pro_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        intiView();
        initData();
    }

    private void initData() {

    }

    private void intiView() {
        setTitle("单车生产详情");
    }

    @OnClick({R.id.rl_oilMass, R.id.rl_proAmount, R.id.rl_ratio, R.id.rl_oilMassToday, R.id.rl_proAmountToday, R.id.rl_ratioToday})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_oilMass:

                break;
            case R.id.rl_proAmount:

                break;
            case R.id.rl_ratio:

                break;
            case R.id.rl_oilMassToday:

                break;
            case R.id.rl_proAmountToday:

                break;
            case R.id.rl_ratioToday:

                break;
        }
    }
}
