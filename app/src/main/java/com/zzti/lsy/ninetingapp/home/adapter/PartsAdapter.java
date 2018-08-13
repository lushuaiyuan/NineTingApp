package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.PartsEntity;

import java.util.List;

/**
 * author：anxin on 2018/8/9 10:29
 */
public class PartsAdapter extends BaseQuickAdapter<PartsEntity, BaseViewHolder> {
    public PartsAdapter(List<PartsEntity> partsEntities) {
        super(R.layout.item_parts, partsEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, PartsEntity item) {
        helper.setText(R.id.tv_partsName, item.getName());
        helper.setText(R.id.tv_state, item.getState());
        helper.setText(R.id.tv_model, item.getModel());
        helper.setText(R.id.tv_num, item.getNum());
        helper.setText(R.id.tv_price, item.getPrice());
        if (tag == 1) {//配件出库列表
            helper.getView(R.id.tv_operator).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_operator, "在库");
        } else if (tag == 2) { //配件入库列表
            helper.getView(R.id.tv_operator).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_operator, "入库");
        } else {
            helper.getView(R.id.tv_operator).setVisibility(View.GONE);
        }
    }

    private int tag;

    public void setTag(int tag) {
        this.tag = tag;
    }
}
