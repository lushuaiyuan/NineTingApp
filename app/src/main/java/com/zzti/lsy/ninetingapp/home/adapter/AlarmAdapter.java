package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.AlarmItemEntity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/12/19 13:26
 * @Describe
 */
public class AlarmAdapter extends BaseQuickAdapter<AlarmItemEntity, BaseViewHolder> {
    public AlarmAdapter(List<AlarmItemEntity> alarmItemEntities) {
        super(R.layout.item_alarm, alarmItemEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlarmItemEntity item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent());
    }

}
