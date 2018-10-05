package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.LaobaoPurchased;

import java.util.List;

/**
 * 日用品入库工单适配器
 */
public class LifeGoodsPurcheaseListAdapter extends BaseQuickAdapter<LaobaoPurchased, BaseViewHolder> {
    public LifeGoodsPurcheaseListAdapter(List<LaobaoPurchased> laobaoPurchaseds) {
        super(R.layout.item_lifegoods, laobaoPurchaseds);
    }

    @Override
    protected void convert(BaseViewHolder helper, LaobaoPurchased item) {
        helper.setText(R.id.tv_goodsName, item.getLbName())
                .setText(R.id.tv_amount, item.getNumber())
                .setText(R.id.tv_price, item.getPurchasedMoney());
        helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
        //（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1为拒绝）
        if (item.getStatus().equals("-1")) {
            helper.setText(R.id.tv_status, "已拒绝");
        } else if (item.getStatus().equals("0")) {
            helper.setText(R.id.tv_status, "总经理已审批");
        } else if (item.getStatus().equals("1")) {
            helper.setText(R.id.tv_status, "项目经理已审批");
        } else if (item.getStatus().equals("2")) {
            helper.setText(R.id.tv_status, "待审批");
        } else if (item.getStatus().equals("3")) {
            helper.setText(R.id.tv_status, "已撤销");
        }

    }

}
