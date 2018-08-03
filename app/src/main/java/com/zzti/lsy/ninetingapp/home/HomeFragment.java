package com.zzti.lsy.ninetingapp.home;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

/**
 * author：anxin on 2018/8/3 16:39
 * 首页
 */
public class HomeFragment extends BaseFragment {
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {

    }

}
