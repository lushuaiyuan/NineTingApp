package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

/**
 * 录入成功
 */
public class InputSuccessActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_input_success;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("录入成功");
    }
}
