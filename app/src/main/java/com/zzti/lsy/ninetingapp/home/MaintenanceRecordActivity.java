package com.zzti.lsy.ninetingapp.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarMaintenanceAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.CarMaintenanceRecordAdapter;
import com.zzti.lsy.ninetingapp.home.entity.CarMaintenanceEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修记录
 */
public class MaintenanceRecordActivity extends BaseActivity {
    @BindView(R.id.tv_handleState)
    TextView tvHandleState;
    @BindView(R.id.tv_carState)
    TextView tvCarState;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<CarMaintenanceEntity> carMaintenanceEntities;
    private CarMaintenanceRecordAdapter carMaintenanceRecordAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_record;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        carMaintenanceEntities = new ArrayList<>();
        carMaintenanceRecordAdapter = new CarMaintenanceRecordAdapter(carMaintenanceEntities);
        mRecycleView.setAdapter(carMaintenanceRecordAdapter);
    }

    private void initView() {
        setTitle("维修记录");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }


    @OnClick({R.id.rl_handleState, R.id.rl_carState})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_handleState://选择车辆

                break;
            case R.id.rl_carState://项目地址

                break;
        }
    }


}
