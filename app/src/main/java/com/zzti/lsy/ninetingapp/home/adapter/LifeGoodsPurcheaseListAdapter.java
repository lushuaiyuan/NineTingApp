package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.LaobaoPurchased;

import java.util.List;

/**
 * 日用品入库工单适配器
 */
public class LifeGoodsPurcheaseListAdapter extends BaseQuickAdapter<LaobaoPurchased, BaseViewHolder> {
    public LifeGoodsPurcheaseListAdapter(List<LaobaoPurchased> laobaoPurchaseds) {
        super(R.layout.item_lifegoods_purchase, laobaoPurchaseds);
    }

    @Override
    protected void convert(BaseViewHolder helper, LaobaoPurchased item) {
        helper.setText(R.id.tv_goodsName, item.getLbName())
                .setText(R.id.tv_amount, item.getNumber())
                .setText(R.id.tv_price, item.getPurchasedMoney())
                .setText(R.id.tv_address, item.getShipaddr())
                .setText(R.id.tv_inTime, item.getPurchasedDate().replace("T", " "));

        //（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1为拒绝）
//        if (item.getStatus().equals("0")) {
//            helper.setText(R.id.tv_status, "总经理已审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_ff80b4));
//        } else if (item.getStatus().equals("1")) {
//            helper.setText(R.id.tv_status, "待总经理审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
//        } else if (item.getStatus().equals("2")) {
//            helper.setText(R.id.tv_status, "待项目经理审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
//        } else if (item.getStatus().equals("3")) {
//            helper.setText(R.id.tv_status, "已撤销");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_bae886));
//        } else if (item.getStatus().equals("-1")) {
//            helper.setText(R.id.tv_status, "已拒绝");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_red));
//        }
    }

}
