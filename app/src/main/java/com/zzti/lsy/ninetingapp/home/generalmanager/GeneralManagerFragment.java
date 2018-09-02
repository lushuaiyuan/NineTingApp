package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.support.v4.app.Fragment;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

/**
 * 总经理 operator 5
 */
public class GeneralManagerFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_general_manager;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {

    }

    public static Fragment newInstance() {
        GeneralManagerFragment fragment = new GeneralManagerFragment();
        return fragment;
    }
}
