package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;

import java.util.List;

/**
 * author：anxin on 2018/8/9 10:29
 * 维修配件的适配器
 */
public class PartsListAdapter extends BaseQuickAdapter<PartsInfoEntity, BaseViewHolder> {
    public PartsListAdapter(List<PartsInfoEntity> partsEntities) {
        super(R.layout.item_parts, partsEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, PartsInfoEntity item) {
        helper.setText(R.id.tv_partsName, item.getPartsName());
//        helper.setText(R.id.tv_state, item.getState());
        helper.setText(R.id.tv_model, item.getPartsModel());
        helper.setText(R.id.tv_num, item.getPartsNumber());
        helper.setText(R.id.tv_price, item.getPurchasedPrice());
        if (tag == 1) {//代表维修申请进来
            helper.getView(R.id.tv_operator).setVisibility(View.GONE);
        } else if (tag == 2) { //代表点击配件列表菜单进来
            helper.getView(R.id.tv_operator).setVisibility(View.GONE);
        } else if (tag == 3) {//3配件入库
            helper.getView(R.id.tv_operator).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_operator, "入库");
        } else if (tag == 4) {//4配件出库
            helper.getView(R.id.tv_operator).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_operator, "出库");
        }
    }

    private int tag;//1代表维修申请进来（获取配件名称）  2代表点击配件列表菜单进来  3配件入库  4配件出库

    public void setTag(int tag) {
        this.tag = tag;
    }
}
