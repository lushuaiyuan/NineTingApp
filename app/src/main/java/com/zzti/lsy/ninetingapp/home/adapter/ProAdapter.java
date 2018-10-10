package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;

import java.util.List;

/**
 * author：anxin on 2018/8/7 16:16
 * 生产表格适配器
 */
public class ProAdapter extends BaseQuickAdapter<StatisticalList, BaseViewHolder> {
    public ProAdapter(List<StatisticalList> statisticalLists) {
        super(R.layout.item_pro, statisticalLists);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatisticalList item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber());
        helper.setText(R.id.tv_name, item.getProjectID());
        helper.setText(R.id.tv_proAmount, item.getSquareQuantity());
        helper.setText(R.id.tv_oilMass, item.getQilWear());
    }
}
