package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.CarEntity;

import java.util.List;

public class CarListAdapter extends BaseQuickAdapter<CarEntity, BaseViewHolder> {
    public CarListAdapter(List<CarEntity> carEntities) {
        super(R.layout.item_carlist, carEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber());
        helper.setText(R.id.tv_carType, item.getCarType());
        helper.setText(R.id.tv_carVin, item.getCarVin());
    }
}
