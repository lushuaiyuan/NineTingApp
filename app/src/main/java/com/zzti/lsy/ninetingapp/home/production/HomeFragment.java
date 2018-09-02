package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.machinery.MaintenanceRecordActivity;
import com.zzti.lsy.ninetingapp.home.machinery.MaintenanceRequestActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.OnClick;

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

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1:
                startActivity(new Intent(mActivity, ProductInputActivity.class));
                break;
            case R.id.rl_menu2:
                break;
            case R.id.rl_menu3:
                startActivity(new Intent(mActivity, MaintenanceRequestActivity.class));
                break;
            case R.id.rl_menu4:
                startActivity(new Intent(mActivity, MaintenanceRecordActivity.class));
                break;
            case R.id.rl_menu5:
                UIUtils.showT("菜单5");
                break;
        }
    }

}
