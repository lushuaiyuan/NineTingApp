package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.HomeHintEntity;

import java.util.List;

public class HomeHintAdapter extends BaseQuickAdapter<HomeHintEntity,BaseViewHolder> {
    public HomeHintAdapter(List<HomeHintEntity> homeHintEntities) {
        super(R.layout.item_home_hint,homeHintEntities);
    }
    @Override
    protected void convert(BaseViewHolder helper, HomeHintEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_endDay, item.getEndDay())
                .setText(R.id.tv_endDate, item.getEndDate());
    }
}
