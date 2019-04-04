package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.UpdateApply;

import java.util.List;

/**
 * @author lsy
 * @create 2019/4/3 20:56
 * @Describe 审批适配器
 */
public class AuthorizationAdapter extends BaseQuickAdapter<UpdateApply, BaseViewHolder> {
    public AuthorizationAdapter(List<UpdateApply> updateApplies) {
        super(R.layout.item_authorization,updateApplies);
    }

    @Override
    protected void convert(BaseViewHolder helper, UpdateApply item) {
        helper.setText(R.id.tv_projectName, item.getProjectName())
                .setText(R.id.tv_applyUser, item.getApplyUser())
                .setText(R.id.tv_applyTime, item.getApplyTime().replace("T", " "))
                .setText(R.id.tv_updateInfo, item.getUpdateInfo());
        if (item.getStatus().equals("0")) {//已审核
            helper.setText(R.id.tv_status, "已审核");
            helper.setTextColor(R.id.tv_status, App.get().getResources().getColor(R.color.color_6bcfd6));
        } else {//待审核
            helper.setText(R.id.tv_status, "待审核");
            helper.setTextColor(R.id.tv_status, App.get().getResources().getColor(R.color.color_red));
        }
    }
}
