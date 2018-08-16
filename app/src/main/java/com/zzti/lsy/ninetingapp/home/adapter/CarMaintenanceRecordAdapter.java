package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.CarMaintenanceEntity;

import java.util.List;

public class CarMaintenanceRecordAdapter extends BaseQuickAdapter<CarMaintenanceEntity, BaseViewHolder> {
    public CarMaintenanceRecordAdapter(List<CarMaintenanceEntity> carMaintenanceEntities) {
        super(R.layout.item_carmaintenance_record,carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarMaintenanceEntity item) {

    }
}
