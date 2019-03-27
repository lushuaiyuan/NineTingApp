package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.pm.MaintenanceStatisticActivity;
import com.zzti.lsy.ninetingapp.home.production.ProductStatisticsActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.OnClick;

/**
 * 机械师  operator 6
 */
public class RepairManageFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_maintenance_manage;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {

    }



    @OnClick({R.id.rl_menu1, R.id.rl_menu2})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //维修统计
                startActivity(new Intent(mActivity, MaintenanceStatisticActivity.class));
                break;
            case R.id.rl_menu2://生产统计
                startActivity(new Intent(mActivity, ProductStatisticsActivity.class));
                break;
//            case R.id.rl_menu3://
//
//                break;
        }
    }

    public static Fragment newInstance() {
        RepairManageFragment fragment = new RepairManageFragment();
        return fragment;
    }
}
