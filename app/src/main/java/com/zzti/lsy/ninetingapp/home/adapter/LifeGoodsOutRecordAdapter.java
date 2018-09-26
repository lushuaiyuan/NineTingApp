package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LifeGoodsEntity;

import java.util.List;

/**
 * 表格查看适配器
 */
public class LifeGoodsOutRecordAdapter extends BaseQuickAdapter<LifeGoodsEntity, BaseViewHolder> {
    public LifeGoodsOutRecordAdapter(List<LifeGoodsEntity> lifeGoodsEntities) {
        super(R.layout.item_lifegoods_outrecord, lifeGoodsEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, LifeGoodsEntity item) {
        helper.setText(R.id.tv_outTime, item.getOutTime())
                .setText(R.id.tv_operator, item.getOperatorName())
                .setText(R.id.tv_amount, item.getLaobaoNumber())
                .setText(R.id.tv_recipient, item.getRecipient());
    }
}
