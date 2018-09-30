package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.StaffEntity;

import java.util.List;

/**
 * author：anxin on 2018/9/30 16:32
 * 员工列表的适配器
 */
public class StaffAdapter extends BaseQuickAdapter<StaffEntity, BaseViewHolder> {
    public StaffAdapter(List<StaffEntity> staffEntities) {
        super(R.layout.item_staff, staffEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, StaffEntity item) {
        helper.setText(R.id.tv_staffName, item.getStaffName())
                .setText(R.id.tv_jobName, item.getJobName())
                .setText(R.id.tv_phone, item.getStaffPhoneNumber());
    }
}
