package com.zzti.lsy.ninetingapp.home.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;

import java.util.List;

/**
 * 维修记录的适配器
 */
public class RepairRecordAdapter extends BaseQuickAdapter<RepairinfoEntity, BaseViewHolder> {
    public RepairRecordAdapter(List<RepairinfoEntity> repairinfoEntities) {
        super(R.layout.item_repair_record, repairinfoEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, RepairinfoEntity item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber())
                .setText(R.id.tv_repairAllMoney, item.getRepairAllMoney())
                .setText(R.id.tv_repairApplyTime, item.getRepairApplyTime().split("T")[0])
                .setText(R.id.tv_repairFactory, item.getRepairFactory());
//        if (item.getStatus().equals("0")) {
//            helper.setText(R.id.tv_status, "总经理已审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
//        } else if (item.getStatus().equals("1")) {
//            helper.setText(R.id.tv_status, "待总经理审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
//        } else if (item.getStatus().equals("2")) {
//            helper.setText(R.id.tv_status, "待项目经理审批");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
//        } else if (item.getStatus().equals("3")) {
//            helper.setText(R.id.tv_status, "已撤销");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_bae886));
//        } else if (item.getStatus().equals("-1")) {
//            helper.setText(R.id.tv_status, "已拒绝");
//            ((TextView) helper.getView(R.id.tv_status)).setTextColor(App.get().getResources().getColor(R.color.color_red));
//        }
    }

}
