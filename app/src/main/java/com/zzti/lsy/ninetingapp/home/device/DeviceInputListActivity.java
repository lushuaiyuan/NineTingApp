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
import com.zzti.lsy.ninetingapp.home.adapter.DeviceInputAdapter;
import com.zzti.lsy.ninetingapp.entity.DeviceEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 设备入库列表
 */
public class DeviceInputListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    private List<DeviceEntity> deviceEntities;
    private DeviceInputAdapter deviceInputAdapter;

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
        deviceInputAdapter = new DeviceInputAdapter(deviceEntities);
        mRecycleView.setAdapter(deviceInputAdapter);
        deviceInputAdapter.setOnItemClickListener(this);

        //TODO
        for (int i = 0; i < 6; i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCarNumber("豫A5555" + i);
            deviceEntity.setProjectAddress("项目部" + i);
            deviceEntity.setInReason("啊时代发生的啊时代发生地方了放假啦地方啊速度发了卡手机到付款爱的色放啦速度快爱的色放借口啦是的爱的浪费空间啦是的啊上看到了卡迪夫" + i);
            deviceEntity.setInDestination("滁州" + i);
            deviceEntities.add(deviceEntity);
        }
        deviceInputAdapter.notifyDataSetChanged();

    }

    private void initView() {
        setTitle("设备入库");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, DeviceInputDetailActivity.class));
    }


    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
