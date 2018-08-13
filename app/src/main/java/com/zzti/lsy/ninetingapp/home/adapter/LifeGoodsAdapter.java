package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.LifeGoodsEntity;

import java.util.List;

/**
 * 日用品列表
 */
public class LifeGoodsAdapter extends BaseQuickAdapter<LifeGoodsEntity, BaseViewHolder> {
    public LifeGoodsAdapter(List<LifeGoodsEntity> lifeGoodEntities) {
        super(R.layout.item_lifegoods, lifeGoodEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, LifeGoodsEntity item) {
        helper.setText(R.id.tv_goodsName, item.getGoodsName())
                .setText(R.id.tv_operator, item.getOperatorName())
                .setText(R.id.tv_amount, item.getAmount())
                .setText(R.id.tv_price, item.getPrice());
    }

}
