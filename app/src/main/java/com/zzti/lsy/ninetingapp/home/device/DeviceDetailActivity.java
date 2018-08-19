package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

/**
 * author：anxin on 2018/8/7 14:28
 * 车辆详情
 */
public class DeviceDetailActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_car_deatil;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("车辆详情");
    }
}
