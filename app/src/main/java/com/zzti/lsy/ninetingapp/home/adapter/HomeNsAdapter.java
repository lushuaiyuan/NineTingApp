package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.NsBxEntity;

import java.util.List;

/**
 * 首页年审适配器
 */
public class HomeNsAdapter extends BaseQuickAdapter<NsBxEntity, BaseViewHolder> {
    public HomeNsAdapter(List<NsBxEntity> homeHintEntities) {
        super(R.layout.item_home_hint, homeHintEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, NsBxEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_endDay, item.getEndDay())
                .setText(R.id.tv_endDate, item.getEndDate());
    }
}
