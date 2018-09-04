package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.ProEntity;

import java.util.List;

/**
 * author：anxin on 2018/8/7 16:16
 * 生产表格适配器
 */
public class ProAdapter extends BaseQuickAdapter<ProEntity, BaseViewHolder> {
    public ProAdapter(List<ProEntity> proEntities) {
        super(R.layout.item_pro, proEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber());
        helper.setText(R.id.tv_name, item.getProjectAddress());
        helper.setText(R.id.tv_proAmount, item.getProAmount());
        helper.setText(R.id.tv_oilMass, item.getOilMass());
    }
}
