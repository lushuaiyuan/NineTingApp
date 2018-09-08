package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.DeviceEntity;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<DeviceEntity, BaseViewHolder> {
    public DeviceListAdapter(List<DeviceEntity> carEntities) {
        super(R.layout.item_device, carEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber());
        if (TextUtils.equals(item.getCarState(), "存放中")) {
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
            helper.setText(R.id.tv_state, "存放中");
        } else if (TextUtils.equals(item.getCarState(), "维修中")) {
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
            helper.setText(R.id.tv_state, "维修中");
        } else if (TextUtils.equals(item.getCarState(), "工作中")) {
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(App.get().getResources().getColor(R.color.color_ffb947));
            helper.setText(R.id.tv_state, "工作中");
        }
        helper.setText(R.id.tv_projectAddress, item.getProjectAddress());
        helper.setText(R.id.tv_address, item.getAddress());
        helper.setText(R.id.tv_carType, item.getCarType());
        if (flag == 3) {
            helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);
            if (item.isDefault()) {
                ((ImageView) helper.getView(R.id.iv_check)).setImageResource(R.drawable.iv_check_select);
                return;
            } else {
                ((ImageView) helper.getView(R.id.iv_check)).setImageResource(R.drawable.iv_check_normal);
            }
            if (item.isCheck()) {
                ((ImageView) helper.getView(R.id.iv_check)).setImageResource(R.drawable.iv_check_select);
            } else {
                ((ImageView) helper.getView(R.id.iv_check)).setImageResource(R.drawable.iv_check_normal);
            }
        } else {
            helper.getView(R.id.iv_check).setVisibility(View.GONE);
        }
    }

    private int flag;

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
