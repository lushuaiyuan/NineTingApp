package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.LaobaoDelivery;

import java.util.List;

/**
 * 表格查看适配器
 * 日用品出库记录适配器
 */
public class LifeGoodsOutRecordAdapter extends BaseQuickAdapter<LaobaoDelivery, BaseViewHolder> {
    public LifeGoodsOutRecordAdapter(List<LaobaoDelivery> laobaoDeliveries) {
        super(R.layout.item_lifegoods_outrecord, laobaoDeliveries);
    }

    @Override
    protected void convert(BaseViewHolder helper, LaobaoDelivery item) {
        helper.setText(R.id.tv_outTime, item.getLdDate().split("T")[0])
                .setText(R.id.tv_operator, item.getUseName())
                .setText(R.id.tv_amount, item.getLdNumber())
                .setText(R.id.tv_recipient, item.getStaffName());
    }
}
