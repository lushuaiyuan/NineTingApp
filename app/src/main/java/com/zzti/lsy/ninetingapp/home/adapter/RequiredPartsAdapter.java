package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.utils.StringUtil;

import java.util.List;

/**
 * 维修记录明细
 */
public class RequiredPartsAdapter extends BaseQuickAdapter<RequiredParts, BaseViewHolder> {
    public RequiredPartsAdapter(List<RequiredParts> carMaintenanceEntities) {
        super(R.layout.item_carmaintenance_detail, carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, RequiredParts item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        if (StringUtil.isNullOrEmpty(item.getPartsName())) {
            helper.setText(R.id.tv_partsName, "");
        } else {
            helper.setText(R.id.tv_partsName, item.getPartsName());
        }
        if (!StringUtil.isNullOrEmpty(item.getPartsAmount())) {
            helper.setText(R.id.tv_partsAmount, item.getPartsAmount());
        }
        if (StringUtil.isNullOrEmpty(item.getMoney())) {
            helper.setText(R.id.tv_totalMoney, "0元");
        } else {
            helper.setText(R.id.tv_totalMoney, item.getMoney() + "元");
        }
        if (type == 1) {//录入的时候部分按钮可以操作
            if (helper.getAdapterPosition() >= 1) {
                helper.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            }
            helper.addOnClickListener(R.id.ib_sub).addOnClickListener(R.id.ib_add).addOnClickListener(R.id.tv_delete).addOnClickListener(R.id.ll_partsName);
        } else {
            helper.getView(R.id.tv_delete).setVisibility(View.GONE);
        }


    }

    private int type; //1代表录入  2代表记录

    public void setType(int type) {
        this.type = type;
    }
}