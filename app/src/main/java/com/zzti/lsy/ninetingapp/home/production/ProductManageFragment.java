package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;

import butterknife.OnClick;

/**
 * author：anxin on 2018/8/3 16:39
 * 生产管理员 operator 1
 */
public class ProductManageFragment extends BaseFragment {


    public static ProductManageFragment newInstance() {
        ProductManageFragment fragment = new ProductManageFragment();
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

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1:
                Intent intent = new Intent(mActivity, ProductInputActivity.class);
                intent.putExtra("TAG", 0);
                startActivity(intent);
                break;
            case R.id.rl_menu2:
                startActivity(new Intent(mActivity, ProductStatisticsActivity.class));
                break;
            case R.id.rl_menu3:
                startActivity(new Intent(mActivity, ProductRecordActivity.class));
                break;
        }
    }

}
