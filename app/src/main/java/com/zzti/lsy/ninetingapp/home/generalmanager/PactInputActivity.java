package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.os.Bundle;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

/**
 * author：anxin on 2018/10/12 20:06
 * 合同录入界面
 */
public class PactInputActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_pact_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        setTitle("录入合同");
    }

    private void initData() {

    }
}
