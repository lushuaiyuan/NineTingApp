package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsAdapter;
import com.zzti.lsy.ninetingapp.entity.LifeGoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 日用品列表
 */
public class LifeGoodsListActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<LifeGoodsEntity> lifeGoodEntities;
    private LifeGoodsAdapter lifeGoodsAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        lifeGoodEntities = new ArrayList<>();
        lifeGoodsAdapter = new LifeGoodsAdapter(lifeGoodEntities);
        mRecycleView.setAdapter(lifeGoodsAdapter);
        lifeGoodsAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 5; i++) {
            LifeGoodsEntity lifeGoodsEntity = new LifeGoodsEntity();
            lifeGoodsEntity.setGoodsName("易损牌手套");
            lifeGoodsEntity.setOperatorName("lsy");
            lifeGoodsEntity.setAmount("105");
            lifeGoodsEntity.setPrice("100.00元");
            lifeGoodEntities.add(lifeGoodsEntity);
        }
        lifeGoodsAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("日用品");
        tvToolbarMenu.setVisibility(View.VISIBLE);
        tvToolbarMenu.setText("记录");
        tvToolbarMenu.setOnClickListener(this);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, LifeGoodsOutRecordActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, LifeGoodsDetailActivity.class));
    }
}
