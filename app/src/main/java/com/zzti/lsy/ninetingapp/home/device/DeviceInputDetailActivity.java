package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备入库详情
 */
public class DeviceInputDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_inTime)
    TextView tvInTime;
    @BindView(R.id.tv_inNum)
    TextView tvInNum;
    @BindView(R.id.tv_inDestination)
    TextView tvInDestination;
    @BindView(R.id.tv_inReason)
    TextView tvInReason;


    @Override
    public int getContentViewId() {
        return R.layout.activity_device_input_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("入库详情");
    }

    @OnClick(R.id.btn_input)
    public void viewClick() {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 7);
        startActivity(intent);
        finish();
    }

}
