package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaoBao;

import java.util.List;

/**
 * 日用品列表
 */
public class LifeGoodsAdapter extends BaseQuickAdapter<LaoBao, BaseViewHolder> {
    public LifeGoodsAdapter(List<LaoBao> lifeGoodEntities) {
        super(R.layout.item_lifegoods, lifeGoodEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, LaoBao item) {
        helper.setText(R.id.tv_goodsName, item.getLbName())
                .setText(R.id.tv_amount, item.getLaobaoNumber())
                .setText(R.id.tv_price, item.getPrice());
    }

}
