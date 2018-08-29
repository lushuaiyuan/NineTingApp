package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.adapter.HomeHintAdapter;
import com.zzti.lsy.ninetingapp.home.entity.HomeHintEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页管理界面
 */
public class DeviceManageFragment extends BaseFragment {
    @BindView(R.id.recycleView_ns)
    RecyclerView mRecycleViewNs;
    @BindView(R.id.recycleView_bx)
    RecyclerView mRecycleViewBx;
    private List<HomeHintEntity> homeHintEntitiesNs;
    private List<HomeHintEntity> homeHintEntitiesBx;
    private HomeHintAdapter homeHintAdapter;

    public static DeviceManageFragment newInstance() {
        DeviceManageFragment fragment = new DeviceManageFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_manage;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {
        homeHintEntitiesBx = new ArrayList<>();
        homeHintEntitiesNs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HomeHintEntity homeHintEntity = new HomeHintEntity();
            homeHintEntity.setCarNumber("豫A5555" + i);
            homeHintEntity.setEndDate("2018.09.01");
            homeHintEntity.setEndDay("12");
            homeHintEntitiesNs.add(homeHintEntity);
            homeHintEntitiesBx.add(homeHintEntity);
        }

        LinearLayoutManager linearLayoutManagerNs = new LinearLayoutManager(getContext());
        linearLayoutManagerNs.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewNs.setLayoutManager(linearLayoutManagerNs);
        homeHintAdapter = new HomeHintAdapter(homeHintEntitiesNs);
        mRecycleViewNs.setAdapter(homeHintAdapter);

        LinearLayoutManager linearLayoutManagerBx = new LinearLayoutManager(getContext());
        linearLayoutManagerBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewBx.setLayoutManager(linearLayoutManagerBx);
        homeHintAdapter = new HomeHintAdapter(homeHintEntitiesBx);
        mRecycleViewBx.setAdapter(homeHintAdapter);

    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //设备列表
                startActivity(new Intent(mActivity, DeviceListActivity.class));
                break;
            case R.id.rl_menu2://设备录入
                startActivity(new Intent(mActivity, DeviceInputActivity.class));
                break;
            case R.id.rl_menu3://设备入库
                startActivity(new Intent(mActivity, DeviceInputListActivity.class));
                break;
            case R.id.rl_menu4://设备出库
                startActivity(new Intent(mActivity, DeviceOutputListActivity.class));
                break;
            case R.id.rl_menu5://年审、保险
                startActivity(new Intent(mActivity, BxNsActivity.class));
                break;
            case R.id.rl_menu6://查看表格
                startActivity(new Intent(mActivity, DeviceFormActivity.class));
                break;
        }
    }
}
