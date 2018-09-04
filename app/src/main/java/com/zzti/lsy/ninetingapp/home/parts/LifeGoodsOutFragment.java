package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsAdapter;
import com.zzti.lsy.ninetingapp.entity.LifeGoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 日用品出库
 */
public class LifeGoodsOutFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<LifeGoodsEntity> lifeGoodEntities;
    private LifeGoodsAdapter lifeGoodsAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lifegoods_out;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(mActivity,LifeGoodsOutActivity.class));
    }
}
