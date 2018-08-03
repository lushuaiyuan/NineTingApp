package com.zzti.lsy.ninetingapp.task;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

/**
 * author：anxin on 2018/8/3 16:30
 */
public class TaskFragment extends BaseFragment {
    public static TaskFragment newInstance() {
        TaskFragment taskFragment = new TaskFragment();
        return taskFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("消息");
    }

    @Override
    protected void initData() {

    }
}
