package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;

import butterknife.OnClick;

/**
 * 保险年审时间
 */
public class YearInsuranceActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_year_insurance;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("设备录入");
    }

    @OnClick(R.id.btn_submit)
    public void viewClick(View view) {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 6);
        startActivity(intent);
    }
}
