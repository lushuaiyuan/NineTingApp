package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;

import java.util.List;

/**
 * 设备出库列表适配器
 */
public class DeviceOutputAdapter extends BaseQuickAdapter<CarInfoEntity, BaseViewHolder> {
    public DeviceOutputAdapter(List<CarInfoEntity> deviceEntities) {
        super(R.layout.item_device_output, deviceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoEntity item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber());
//        helper.setText(R.id.tv_projectAddress, item.getProjectAddress());
//        helper.setText(R.id.tv_address, item.getAddress());
//        helper.setText(R.id.tv_carType, item.getCarType());
    }
}
