package com.zzti.lsy.ninetingapp.home.adapter;

import android.opengl.Visibility;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
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
        //TODO
//                .setText(R.id.tv_alarmValue, item.getAlarmValue());
//        if (Integer.parseInt(item.getAlarmValue()) > Integer.parseInt(item.getLaobaoNumber())) {//库存数量小于告警值
//            helper.setVisible(R.id.tv_status, true);
//            helper.setTextColor(R.id.tv_status, App.get().getResources().getColor(R.color.color_red));
//            helper.setText(R.id.tv_status, "库存不足");
//        } else {
//            helper.setVisible(R.id.tv_status, false);
//        }

    }

}
