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
import com.zzti.lsy.ninetingapp.home.adapter.CarListAdapter;
import com.zzti.lsy.ninetingapp.home.entity.CarEntity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CarListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private CarListAdapter carListAdapter;
    private List<CarEntity> carListData;
    private int flag = -1; //-1默认进入到详情界面 1代表获取车牌号

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
        flag = UIUtils.getInt4Intent(this, "FLAG");
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        carListData = new ArrayList<>();
        carListAdapter = new CarListAdapter(carListData);
        mRecycleView.setAdapter(carListAdapter);
        carListAdapter.setOnItemClickListener(this);
        //TODO
        for (int i = 0; i < 6; i++) {
            CarEntity carEntity = new CarEntity();
            carEntity.setCarNumber("豫A5555" + i);
            carEntity.setCarType("宇通00" + i);
            carEntity.setCarVin("AFWE2323ASDFA" + i);
            carListData.add(carEntity);
        }
        carListAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("车辆列表");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (flag == -1) {
            startActivity(new Intent(this, CarDetailActivity.class));
        } else if (flag == 1) {
            Intent intent = new Intent();
            intent.putExtra("carNumber", carListData.get(position).getCarNumber());
            setResult(2, intent);
            finish();
        }
    }


}
