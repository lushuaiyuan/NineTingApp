package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;

import java.util.List;

/**
 * 年审适配器
 */
public class NSAdapter extends BaseQuickAdapter<NsBxEntity, BaseViewHolder> {
    public NSAdapter(List<NsBxEntity> homeHintEntities) {
        super(R.layout.item_ns_list, homeHintEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, NsBxEntity item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber())
                .setText(R.id.tv_endDate, item.getExpireDate().split("T")[0])
                .setText(R.id.tv_projectAddress, item.getProjectName())
                .setText(R.id.tv_carType, item.getVehicleTypeName())
                .setText(R.id.tv_bx_validityDay, String.valueOf(DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), item.getExpireDate().split("T")[0])));
    }
}
