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
        super(R.layout.item_required_parts, carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, RequiredParts item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        if (StringUtil.isNullOrEmpty(item.getPartsName())) {
            helper.setText(R.id.tv_partsName, "");
        } else {
            if (type == 1) {//录入
                helper.setText(R.id.tv_partsName, item.getPartsName());
            } else {
                helper.setText(R.id.tv_partsName, item.getPartsModel() + "——" + item.getPartsName());
            }
        }
        if (!StringUtil.isNullOrEmpty(item.getRpNumber())) {
            helper.setText(R.id.tv_partsAmount, item.getRpNumber());
        }
        if (type == 1) {//录入的时候部分按钮可以操作
            if (helper.getAdapterPosition() >= 1) {
                helper.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            }
            helper.getView(R.id.ib_sub).setVisibility(View.VISIBLE);
            helper.getView(R.id.ib_add).setVisibility(View.VISIBLE);
            helper.getView(R.id.imageView).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.ib_sub).addOnClickListener(R.id.ib_add).addOnClickListener(R.id.tv_delete).addOnClickListener(R.id.ll_partsName);
        } else {
            helper.getView(R.id.imageView).setVisibility(View.GONE);
            helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            helper.getView(R.id.ib_sub).setVisibility(View.GONE);
            helper.getView(R.id.ib_add).setVisibility(View.GONE);
        }


    }

    private int type; //1代表录入  2代表记录

    public void setType(int type) {
        this.type = type;
    }
}
