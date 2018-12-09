package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.RentCarEntity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/12/9 16:55
 * @Describe 外租车的适配器
 */
public class RentCarAdapter extends BaseQuickAdapter<RentCarEntity, BaseViewHolder> {
    public RentCarAdapter(List<RentCarEntity> rentCarEntities) {
        super(R.layout.item_rentcar, rentCarEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, RentCarEntity item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber())
                .setText(R.id.tv_carOwner, item.getOwnerName())
                .setText(R.id.tv_rentStartTime, item.getStartTime().split("T")[0])
                .setText(R.id.tv_rentEndTime, item.getOverTime().split("T")[0]);
    }
}
