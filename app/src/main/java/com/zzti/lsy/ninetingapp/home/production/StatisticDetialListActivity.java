package com.zzti.lsy.ninetingapp.home.production;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.StatisticAdapter;
import com.zzti.lsy.ninetingapp.home.entity.StatisticEntity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 总油耗、总方量、油耗比的统计列表
 */
public class StatisticDetialListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_typeValue)
    TextView tvTypeValue;
    @BindView(R.id.tv_typeUnit)
    TextView tvTypeUnit;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private int type;
    private List<StatisticEntity> statisticEntities;
    private StatisticAdapter statisticAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.acitivity_statistic_detail_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        statisticEntities = new ArrayList<>();
        statisticAdapter = new StatisticAdapter(statisticEntities);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(statisticAdapter);
        statisticAdapter.setType(type);
        for (int i = 0; i < 5; i++) {
            StatisticEntity statisticEntity = new StatisticEntity();
            statisticEntity.setDate("2018-09-0" + i);
            statisticEntity.setValue("50" + i);
            statisticEntities.add(statisticEntity);
        }
        statisticAdapter.notifyDataSetChanged();
    }

    private void initView() {
        ivToolbarMenu.setVisibility(View.VISIBLE);
        ivToolbarMenu.setImageResource(R.mipmap.icon_pie);
        ivToolbarMenu.setOnClickListener(this);
        type = UIUtils.getInt4Intent(this, "TYPE");
        if (type == 1) {//总油耗
            tvToolbarTitle.setText("油耗");
            tvType.setText("油耗");
            tvTypeUnit.setText("升");
        } else if (type == 2) {//生产方量
            tvToolbarTitle.setText("生产方量");
            tvType.setText("总方量");
            tvTypeUnit.setText("平米");
        } else if (type == 3) {//油耗比
            tvToolbarTitle.setText("油耗比");
            tvType.setText("总油耗比");
            tvTypeUnit.setText("%");
        }
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onClick(View view) {

    }
}
