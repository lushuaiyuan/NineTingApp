package com.zzti.lsy.ninetingapp.home.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.StatisticEntity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;

import java.util.List;

/**
 * 统计适配器
 */
public class StatisticAdapter extends BaseQuickAdapter<StatisticEntity, BaseViewHolder> {
    private int type;

    public StatisticAdapter(List<StatisticEntity> statisticEntities) {
        super(R.layout.item_statistic, statisticEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatisticEntity item) {
        if (DateUtil.IsToday(item.getDate())) {
            helper.setText(R.id.tv_time, "今天");
        }else{
            helper.setText(R.id.tv_time, item.getDate().split("-")[1] + "-"+item.getDate().split("-")[2]);
        }
        helper.setText(R.id.tv_typeValue, item.getValue());
        if (type == 1) {
            helper.setText(R.id.tv_typeUnit, "升");
        } else if (type == 2) {
            helper.setText(R.id.tv_typeUnit, "平米");
        } else if (type == 3) {
            helper.setText(R.id.tv_typeUnit, "%");
        }
    }

    public void setType(int type) {
        this.type = type;
    }
}
