package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;

import butterknife.OnClick;

/**
 * 入库详情
 */
public class PartsInDetailActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_partsin_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initView() {
        setTitle("入库详情");
    }

    @OnClick(R.id.btn_in)
    public void viewClick(View view) {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 3);
        startActivity(intent);
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
