package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/10/12 11:26
 * 项目部详情界面
 */
public class ProjectDetailActivity extends BaseActivity {
    @BindView(R.id.tv_projectName)
    TextView tvProjectName;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_projectLeader)
    TextView tvProjectLeader;
    @BindView(R.id.tv_projectPeriod)
    TextView tvProjectPeriod;
    @BindView(R.id.tv_contractMoney)
    TextView tvContractMoney;
    @BindView(R.id.tv_projectStartTime)
    TextView tvProjectStartTime;

    @Override
    public int getContentViewId() {
        return R.layout.activity_project_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private String projectID;

    private void initData() {
        ProjectEntity projectEntity = (ProjectEntity) getIntent().getSerializableExtra("projectEntity");
        projectID = projectEntity.getProjectID();
        tvProjectName.setText(projectEntity.getProjectName());
        tvProjectAddress.setText(projectEntity.getProjectAddress());
        tvProjectLeader.setText(projectEntity.getMangerName());
        tvContractMoney.setText(projectEntity.getCartainName());
        tvProjectPeriod.setText(projectEntity.getStatisName());
        tvProjectStartTime.setText(projectEntity.getProjectStartTime().split("T")[0]);
    }

    private void initView() {
        setTitle("项目详情");
    }

    @OnClick(R.id.btn_staffList)
    public void viewClick() {
        Intent intent = new Intent(this, StaffListActivity.class);
        intent.putExtra("projectName", tvProjectName.getText().toString());
        intent.putExtra("projectID", projectID);
        startActivity(intent);
    }
}
