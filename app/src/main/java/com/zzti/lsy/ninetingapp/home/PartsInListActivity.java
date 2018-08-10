package com.zzti.lsy.ninetingapp.home;

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
import com.zzti.lsy.ninetingapp.home.adapter.PartsAdapter;
import com.zzti.lsy.ninetingapp.home.entity.PartsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/9 16:53
 * 配件入库列表
 */
public class PartsInListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PartsEntity> partsEntities;
    private PartsAdapter partsAdapter;

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
        partsEntities = new ArrayList<>();
        partsAdapter = new PartsAdapter(partsEntities);
        partsAdapter.setTag(2);
        mRecycleView.setAdapter(partsAdapter);
        partsAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 5; i++) {
            PartsEntity partsEntity = new PartsEntity();
            partsEntity.setName("米其林");
            partsEntity.setState("在库");
            partsEntity.setModel("配件的型号");
            partsEntity.setNum("1000");
            partsEntity.setPrice("100.00元");
            partsEntities.add(partsEntity);
        }
        partsAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("配件入库");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, PartsInDetailActivity.class));
    }



    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
