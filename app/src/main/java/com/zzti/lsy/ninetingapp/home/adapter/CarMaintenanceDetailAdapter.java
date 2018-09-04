package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarMaintenanceEntity;
import com.zzti.lsy.ninetingapp.utils.StringUtil;

import java.util.List;

/**
 * 维修记录明细
 */
public class CarMaintenanceDetailAdapter extends BaseQuickAdapter<CarMaintenanceEntity, BaseViewHolder> {
    public CarMaintenanceDetailAdapter(List<CarMaintenanceEntity> carMaintenanceEntities) {
        super(R.layout.item_carmaintenance_detail, carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarMaintenanceEntity item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        if (TextUtils.equals(item.getReason(), "1")) {
            helper.setText(R.id.tv_reason, "正常损坏");
        } else if (TextUtils.equals(item.getReason(), "2")) {
            helper.setText(R.id.tv_reason, "人为损坏");
        } else if (TextUtils.equals(item.getReason(), "3")) {
            helper.setText(R.id.tv_reason, "事故损坏");
        }
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
    }
}
