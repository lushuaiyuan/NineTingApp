package com.zzti.lsy.ninetingapp.mine;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

/**
 * author：anxin on 2018/8/3 16:39
 */
public class MineFragment extends BaseFragment {
    public static MineFragment newInstance() {
        MineFragment taskFragment = new MineFragment();
        return taskFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("我的");
    }

    @Override
    protected void initData() {

    }
}
