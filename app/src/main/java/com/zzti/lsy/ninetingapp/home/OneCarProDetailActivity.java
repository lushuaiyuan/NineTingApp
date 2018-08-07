package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

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
}
