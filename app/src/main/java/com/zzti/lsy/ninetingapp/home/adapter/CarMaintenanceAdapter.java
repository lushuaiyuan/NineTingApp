package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarMaintenanceEntity;
import com.zzti.lsy.ninetingapp.utils.StringUtil;

import java.util.List;

public class CarMaintenanceAdapter extends BaseQuickAdapter<CarMaintenanceEntity, BaseViewHolder> {
    public CarMaintenanceAdapter(List<CarMaintenanceEntity> carMaintenanceEntities) {
        super(R.layout.item_carmaintenance, carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarMaintenanceEntity item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        if (helper.getAdapterPosition() >= 1) {
            helper.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_delete).setVisibility(View.GONE);
        }
        if (TextUtils.equals(item.getReason(), "1")) {
            ((RadioButton) helper.getView(R.id.radio_button_normal)).setChecked(true);
        } else if (TextUtils.equals(item.getReason(), "2")) {
            ((RadioButton) helper.getView(R.id.radio_button_personnel)).setChecked(true);
        } else if (TextUtils.equals(item.getReason(), "3")) {
            ((RadioButton) helper.getView(R.id.radio_button_accident)).setChecked(true);
        }
        if (StringUtil.isNullOrEmpty(item.getPartsName())) {
            helper.setText(R.id.tv_partsName, "");
        } else {
            helper.setText(R.id.tv_partsName, item.getPartsName());
        }
        helper.addOnClickListener(R.id.ib_sub).addOnClickListener(R.id.ib_add).addOnClickListener(R.id.tv_delete).addOnClickListener(R.id.ll_partsName);
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
