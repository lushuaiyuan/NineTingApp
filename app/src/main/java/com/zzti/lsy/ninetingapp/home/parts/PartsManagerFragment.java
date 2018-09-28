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

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.rl_menu7, R.id.rl_menu8})
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
                Intent intent7 = new Intent(mActivity, LifeGoodsListActivity.class);
                intent7.putExtra("TAG", 1);
                startActivity(intent7);
                break;
            case R.id.rl_menu6://日用品录入
                Intent intent4 = new Intent(mActivity, LifeGoodsInActivity.class);
                intent4.putExtra("TAG", 1);
                startActivity(intent4);
                break;
            case R.id.rl_menu7://日用品入库
                Intent intent5 = new Intent(mActivity, LifeGoodsListActivity.class);
                intent5.putExtra("TAG", 2);
                startActivity(intent5);
                break;
            case R.id.rl_menu8://日用品出库
                Intent intent6 = new Intent(mActivity, LifeGoodsListActivity.class);
                intent6.putExtra("TAG", 3);
                startActivity(intent6);
                break;
        }
    }
}
