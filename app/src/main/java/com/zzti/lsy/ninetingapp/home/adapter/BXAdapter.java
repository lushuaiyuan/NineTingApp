package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.HomeHintEntity;

import java.util.List;

/**
 * 保险适配器
 */
public class BXAdapter extends BaseQuickAdapter<HomeHintEntity, BaseViewHolder> {
    public BXAdapter(List<HomeHintEntity> homeHintEntities) {
        super(R.layout.item_bx_list, homeHintEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeHintEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_endDate, item.getEndDate())
                .setText(R.id.tv_projectAddress, item.getProjectAddress())
                .setText(R.id.tv_buyDate, item.getBuyDate())
                .setText(R.id.tv_bx_validityTime, item.getValidityTime());
    }
}
