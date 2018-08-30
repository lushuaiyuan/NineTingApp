package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.adapter.BXAdapter;
import com.zzti.lsy.ninetingapp.home.entity.NsBxEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 保险
 */
public class BxFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<NsBxEntity> homeHintEntities;
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
        bxAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 5; i++) {
            NsBxEntity nsBxEntity = new NsBxEntity();
            nsBxEntity.setCarNumber("豫A5555" + i);
            nsBxEntity.setEndDate("2019-01-01");
            nsBxEntity.setEndDay("10");
            nsBxEntity.setBuyDate("2018-01-01");
            nsBxEntity.setValidityTime("1年");
            homeHintEntities.add(nsBxEntity);
        }
        bxAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DeviceDetailActivity.class);
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
