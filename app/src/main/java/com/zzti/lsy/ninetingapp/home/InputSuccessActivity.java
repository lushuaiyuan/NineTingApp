package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.btn_input, R.id.btn_form})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input:
                finish();
                break;
            case R.id.btn_form:
                EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                startActivity(new Intent(this, FormListActivity.class));
                finish();
                break;
        }
    }


}
