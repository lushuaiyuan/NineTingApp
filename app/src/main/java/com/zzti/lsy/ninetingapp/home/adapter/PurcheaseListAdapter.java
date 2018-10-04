package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaoBao;

import java.util.List;

/**
 * 日用品采购列表
 */
public class PurcheaseListAdapter extends BaseQuickAdapter<LaoBao, BaseViewHolder> {
    public PurcheaseListAdapter(List<LaoBao> lifeGoodEntities) {
        super(R.layout.item_lifegoods, lifeGoodEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, LaoBao item) {
        helper.setText(R.id.tv_goodsName, item.getLbName())
                .setText(R.id.tv_amount, item.getLaobaoNumber())
                .setText(R.id.tv_price, item.getPrice());
    }

}
