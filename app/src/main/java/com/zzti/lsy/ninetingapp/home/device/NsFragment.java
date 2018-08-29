package com.zzti.lsy.ninetingapp.home.device;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.adapter.BXAdapter;
import com.zzti.lsy.ninetingapp.home.entity.HomeHintEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 年审
 */
public class NsFragment extends BaseFragment {
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<HomeHintEntity> homeHintEntities;
    private BXAdapter bxAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        homeHintEntities = new ArrayList<>();
        bxAdapter = new BXAdapter(homeHintEntities);
        mRecycleView.setAdapter(bxAdapter);
//        bxAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 5; i++) {
            HomeHintEntity homeHintEntity = new HomeHintEntity();
            homeHintEntity.setCarNumber("豫A5555" + i);
            homeHintEntity.setEndDate("2019-01-01");
            homeHintEntity.setEndDay("10");
            homeHintEntity.setBuyDate("2018-01-01");
            homeHintEntity.setValidityTime("1年");
            homeHintEntities.add(homeHintEntity);
        }
        bxAdapter.notifyDataSetChanged();
    }
}
