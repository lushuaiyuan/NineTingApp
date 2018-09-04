package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ProAdapter;
import com.zzti.lsy.ninetingapp.entity.ProEntity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/7 15:59
 * 生产表格
 */
public class FormListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<ProEntity> proEntities;
    private ProAdapter proAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_form_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        proEntities = new ArrayList<>();
        proAdapter = new ProAdapter(proEntities);
        mRecycleView.setAdapter(proAdapter);
        proAdapter.setOnItemClickListener(this);

        //TODO
        for (int i = 0; i < 6; i++) {
            ProEntity proEntity = new ProEntity();
            proEntity.setCarNumber("豫A5555" + i);
            proEntity.setProjectAddress("项目" + i);
            proEntity.setProAmount("1" + i);
            proEntity.setOilMass("2" + i);
            proEntities.add(proEntity);
        }
        proAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle(DateUtil.getCurrentDate());
        ivToolbarMenu.setVisibility(View.VISIBLE);
        ivToolbarMenu.setOnClickListener(this);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, OneCarProDetailActivity.class));
    }

    @Override
    public void onClick(View view) {
        showCustomTime();
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(FormListActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                setTitle(DateUtil.getDate(date));
                //TODO  查询数据

            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }
}
