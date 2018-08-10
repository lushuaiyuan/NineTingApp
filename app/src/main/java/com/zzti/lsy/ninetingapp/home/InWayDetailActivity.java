package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

/**
 * author：anxin on 2018/8/9 15:59
 * 在途的详情
 */
public class InWayDetailActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_out_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("出库详情");
    }
}
