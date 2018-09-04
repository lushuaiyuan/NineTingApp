package com.zzti.lsy.ninetingapp.home.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarMaintenanceEntity;

import java.util.List;

public class CarMaintenanceRecordAdapter extends BaseQuickAdapter<CarMaintenanceEntity, BaseViewHolder> {
    public CarMaintenanceRecordAdapter(List<CarMaintenanceEntity> carMaintenanceEntities) {
        super(R.layout.item_carmaintenance_record, carMaintenanceEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarMaintenanceEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_partsName, item.getPartsName())
                .setText(R.id.tv_content, item.getReason())
                .setText(R.id.tv_plantTime, item.getPlantTime())
                .setText(R.id.tv_state, item.getState());
        if (item.getState().endsWith("待处理")) {
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
        } else if (item.getState().endsWith("已通过")) {
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
        }
    }
}
