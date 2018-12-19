package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;

import java.util.List;

/**
 * 首页保养适配器
 */
public class HomeByAdapter extends BaseQuickAdapter<NsBxEntity, BaseViewHolder> {
    public HomeByAdapter(List<NsBxEntity> homeHintEntities) {
        super(R.layout.item_home_hint, homeHintEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, NsBxEntity item) {
        helper.getView(R.id.tv_hint).setVisibility(View.GONE);
        helper.getView(R.id.tv_unit).setVisibility(View.GONE);
        helper.setText(R.id.tv_carNumber, item.getPlateNumber())
                .setText(R.id.tv_endDay, item.getAlarm())
                .setText(R.id.tv_endDate, item.getVehicleTypeName());
    }
}
