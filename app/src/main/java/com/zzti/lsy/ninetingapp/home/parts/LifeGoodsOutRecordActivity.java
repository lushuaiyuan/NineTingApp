package com.zzti.lsy.ninetingapp.home.parts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsOutRecordAdapter;
import com.zzti.lsy.ninetingapp.entity.LifeGoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 日用品出库记录
 */
public class LifeGoodsOutRecordActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<LifeGoodsEntity> lifeGoodEntities;
    private LifeGoodsOutRecordAdapter lifeGoodsOutRecordAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_outrecord;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        lifeGoodEntities = new ArrayList<>();
        lifeGoodsOutRecordAdapter = new LifeGoodsOutRecordAdapter(lifeGoodEntities);
        mRecycleView.setAdapter(lifeGoodsOutRecordAdapter);
        //TODO
        for (int i = 0; i < 5; i++) {
            LifeGoodsEntity lifeGoodsEntity = new LifeGoodsEntity();
            lifeGoodsEntity.setOutTime("2018-08-14");
            lifeGoodsEntity.setLbName("易损牌手套");
            lifeGoodsEntity.setOperatorName("lsy");
            lifeGoodsEntity.setLaobaoNumber("105");
            lifeGoodsEntity.setRecipient("卜辽捷");
            lifeGoodEntities.add(lifeGoodsEntity);
        }
        lifeGoodsOutRecordAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("表格查看");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }
}
