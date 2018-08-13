package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

/**
 * 日用品出库
 */
public class LifeGoodsOutActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_out;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("日用品出库");
    }
}
