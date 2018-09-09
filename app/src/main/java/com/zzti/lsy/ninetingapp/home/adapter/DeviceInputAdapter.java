package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;

import java.util.List;

/**
 * 设备入库列表适配器
 */
public class DeviceInputAdapter extends BaseQuickAdapter<CarInfoEntity, BaseViewHolder> {
    public DeviceInputAdapter(List<CarInfoEntity> deviceEntities) {
        super(R.layout.item_device_input, deviceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoEntity item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber());
//        helper.setText(R.id.tv_projectAddress, item.getProjectAddress());
//        helper.setText(R.id.tv_inDestination, item.getInDestination());
//        helper.setText(R.id.tv_inReason, item.getInReason());
    }
}
