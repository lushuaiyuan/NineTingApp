package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.DeviceOutputAdapter;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 设备出库库列表
 */
public class DeviceOutputListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    private List<CarInfoEntity> deviceEntities;
    private DeviceOutputAdapter deviceOutputAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        deviceEntities = new ArrayList<>();
        deviceOutputAdapter = new DeviceOutputAdapter(deviceEntities);
        mRecycleView.setAdapter(deviceOutputAdapter);
        deviceOutputAdapter.setOnItemClickListener(this);

        //TODO
        for (int i = 0; i < 6; i++) {
            CarInfoEntity carInfoEntity = new CarInfoEntity();
            carInfoEntity.setPlateNumber("豫A5555" + i);
//            carInfoEntity.setProjectAddress("项目部" + i);
//            carInfoEntity.setAddress("滁州" + i);
//            carInfoEntity.setCarType("罐车" + i);
            deviceEntities.add(carInfoEntity);
        }
        deviceOutputAdapter.notifyDataSetChanged();

    }

    private void initView() {
        setTitle("设备出库");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, DeviceOutputActivity.class));
    }


    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
