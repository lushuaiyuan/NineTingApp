package com.zzti.lsy.ninetingapp.task;

import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

/**
 * author：anxin on 2018/8/3 16:30
 * 监控
 */
public class MonitorFragment extends BaseFragment {
    public static MonitorFragment newInstance() {
        MonitorFragment monitorFragment = new MonitorFragment();
        return monitorFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("监控");
        ivToolbarBack.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }
}
