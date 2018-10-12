package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;

import java.util.List;

/**
 * author：anxin on 2018/10/12 10:54
 * 项目部详情列表
 */
public class ProjectDetailAdapter extends BaseQuickAdapter<ProjectEntity, BaseViewHolder> {
    public ProjectDetailAdapter(List<ProjectEntity> projectDetails) {
        super(R.layout.item_projectdetail_list, projectDetails);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectEntity item) {
        helper.setText(R.id.tv_projectName, item.getProjectName())
                .setText(R.id.tv_projectAddress, item.getProjectAddress())
                .setText(R.id.tv_projectLeader, item.getMangerName());
    }
}
