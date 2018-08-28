package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

import butterknife.BindView;

/**
 * 设备出库成功详情
 */
public class DeviceOutputDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.tv_outDestination)
    TextView tvOutDestination;
    @BindView(R.id.tv_outReason)
    TextView tvOutReason;

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_output_detail;
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
