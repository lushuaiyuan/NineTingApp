package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.PartsListAdapter;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/8 16:18
 * 配件列表
 */
public class PartsListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.radio_group_condition)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_all)
    RadioButton mRadioButtonAll;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PartsInfoEntity> partsEntities;
    private PartsListAdapter partsListAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        partsEntities = new ArrayList<>();
        partsListAdapter = new PartsListAdapter(partsEntities);
        mRecycleView.setAdapter(partsListAdapter);
        partsListAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 5; i++) {
            PartsInfoEntity partsInfoEntity = new PartsInfoEntity();
            partsInfoEntity.setName("米其林");
            partsInfoEntity.setState("在库");
            partsInfoEntity.setModel("配件的型号");
            partsInfoEntity.setNum("1000");
            partsInfoEntity.setPrice("100.00元");
            partsEntities.add(partsInfoEntity);
        }
        partsListAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("配件列表");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_all: //全部

                        break;
                    case R.id.radio_button_inWareHouse://在库

                        break;
                    case R.id.radio_button_maintain://维修

                        break;
                    case R.id.radio_button_onRoad://在途

                        break;
                }
            }
        });
        // 保证第一次会回调OnCheckedChangeListener
        mRadioButtonAll.setChecked(true);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //TODO
        PartsInfoEntity partsInfoEntity = partsEntities.get(position);
        startActivity(new Intent(this, PartsDetailActivity.class));
    }
}
