package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

import butterknife.OnClick;

/**
 * author：anxin on 2018/8/8 14:14
 * 配件管理员 operator 3
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
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.rl_menu7})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //配件列表
                startActivity(new Intent(mActivity, PartsListActivity.class));
                break;
            case R.id.rl_menu2://配件录入
                Intent intent1 = new Intent(mActivity, PartsInputActivity.class);
                intent1.putExtra("TAG", 1);
                startActivity(intent1);
                break;
            case R.id.rl_menu3://配件入库
                Intent intent2 = new Intent(mActivity, PartsListActivity.class);
                intent2.putExtra("TAG", 3);
                startActivity(intent2);
                break;
            case R.id.rl_menu4://配件出库
                Intent intent3 = new Intent(mActivity, PartsListActivity.class);
                intent3.putExtra("TAG", 4);
                startActivity(intent3);
                break;
            case R.id.rl_menu5://日用品列表
                startActivity(new Intent(mActivity, LifeGoodsListActivity.class));
                break;
            case R.id.rl_menu6://日用品录入
                startActivity(new Intent(mActivity, LifeGoodsOutInActivity.class));
                break;
            case R.id.rl_menu7://日用品出库
                startActivity(new Intent(mActivity, LifeGoodsOutInActivity.class));
                break;
        }
    }
}
