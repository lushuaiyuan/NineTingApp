package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.NsBxEntity;

import java.util.List;

/**
 * 保险适配器
 */
public class BXAdapter extends BaseQuickAdapter<NsBxEntity, BaseViewHolder> {
    public BXAdapter(List<NsBxEntity> homeHintEntities) {
        super(R.layout.item_bx_list, homeHintEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, NsBxEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_endDate, item.getEndDate())
                .setText(R.id.tv_projectAddress, item.getProjectAddress())
                .setText(R.id.tv_buyDate, item.getBuyDate())
                .setText(R.id.tv_bx_validityTime, item.getValidityTime());
    }
}
