package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
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



    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //维修申请
                startActivity(new Intent(mActivity, RepairRequestActivity.class));
                break;
            case R.id.rl_menu2://维修记录
                startActivity(new Intent(mActivity, RepairRecordActivity.class));
                break;
            case R.id.rl_menu3://维修统计
                //TODO
                UIUtils.showT("维修统计");
                break;
        }
    }

    public static Fragment newInstance() {
        RepairManageFragment fragment = new RepairManageFragment();
        return fragment;
    }
}
