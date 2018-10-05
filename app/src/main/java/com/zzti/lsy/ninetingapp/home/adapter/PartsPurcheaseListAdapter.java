package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.entity.PartsPurchased;

import java.util.List;

/**
 * 配件入库工单适配器
 */
public class PartsPurcheaseListAdapter extends BaseQuickAdapter<PartsPurchased, BaseViewHolder> {
    public PartsPurcheaseListAdapter(List<PartsPurchased> partsPurchaseds) {
        super(R.layout.item_parts, partsPurchaseds);
    }

    @Override
    protected void convert(BaseViewHolder helper, PartsPurchased item) {
        helper.setText(R.id.tv_partsName, item.getPartsName())
                .setText(R.id.tv_model, item.getPartsModel())
                .setText(R.id.tv_num, item.getNumber())
                .setText(R.id.tv_price, item.getPurchasedPrice());
        helper.getView(R.id.tv_operator).setVisibility(View.VISIBLE);
        //（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1为拒绝）
        if (item.getStatus().equals("-1")) {
            helper.setText(R.id.tv_operator, "已拒绝");
        } else if (item.getStatus().equals("0")) {
            helper.setText(R.id.tv_operator, "总经理已审批");
        } else if (item.getStatus().equals("1")) {
            helper.setText(R.id.tv_operator, "项目经理已审批");
        } else if (item.getStatus().equals("2")) {
            helper.setText(R.id.tv_operator, "待审批");
        } else if (item.getStatus().equals("3")) {
            helper.setText(R.id.tv_operator, "已撤销");
        }
    }

}
