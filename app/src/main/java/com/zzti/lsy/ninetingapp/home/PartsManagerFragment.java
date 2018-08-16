package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.OnClick;

/**
 * author：anxin on 2018/8/8 14:14
 * 配件管理员
 */
public class PartsManagerFragment extends BaseFragment {

    public static PartsManagerFragment newInstance() {
        PartsManagerFragment fragment = new PartsManagerFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_partsmanaer;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //配件列表
                startActivity(new Intent(mActivity, PartsListActivity.class));
                break;
            case R.id.rl_menu2://配件录入
                startActivity(new Intent(mActivity, PartsInputActivity.class));
                break;
            case R.id.rl_menu3://配件入库
                startActivity(new Intent(mActivity, PartsInListActivity.class));
                break;
            case R.id.rl_menu4://配件入库
                startActivity(new Intent(mActivity, PartsOutListActivity.class));
                break;
            case R.id.rl_menu5://日用品列表
//                startActivity(new Intent(mActivity, LifeGoodsListActivity.class));
                startActivity(new Intent(mActivity, MaintenanceRecordActivity.class));
                break;
            case R.id.rl_menu6://日用品采购出库
//                startActivity(new Intent(mActivity, LifeGoodsOutInActivity.class));
                startActivity(new Intent(mActivity, MaintenanceRequestActivity.class));
                break;
        }
    }
}
