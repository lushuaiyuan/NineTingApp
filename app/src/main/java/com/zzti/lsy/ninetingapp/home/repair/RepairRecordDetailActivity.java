package com.zzti.lsy.ninetingapp.home.repair;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 维修记录详情
 */
public class RepairRecordDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_constructionAddress)
    TextView tvConstructionAddress;
    @BindView(R.id.tv_serviceType)
    TextView tvServiceType;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.recycleView_detail)
    RecyclerView recycleViewDetail;
    @BindView(R.id.tv_maintenanceTime)
    TextView tvMaintenanceTime;//计划维修时间
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;
    @BindView(R.id.tv_content)
    TextView tvContent;//维修内容
    @BindView(R.id.tv_remark)
    TextView tvRemark;//维修原因
    @BindView(R.id.btn_operator)
    Button btnOperator;//操作按钮

    //照片
    private PhotoAdapter photoAdapter;
    private List<String> pics;

    //维修明细
    private RequiredPartsAdapter requiredPartsAdapter;
    private List<RequiredParts> requiredPartsList;


    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_record_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        requiredPartsList = new ArrayList<>();
        recycleViewDetail.setLayoutManager(new LinearLayoutManager(this));
        requiredPartsAdapter = new RequiredPartsAdapter(requiredPartsList);
        requiredPartsAdapter.setType(2);
        recycleViewDetail.setAdapter(requiredPartsAdapter);
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getData();
        }
    }

    private void getData() {
        cancelDia();

    }


    private void initView() {
        setTitle("维修申请");
        //解决卡顿问题
        recycleViewDetail.setHasFixedSize(true);
        recycleViewDetail.setNestedScrollingEnabled(false);
        RepairinfoEntity repairinfoEntity = (RepairinfoEntity) getIntent().getSerializableExtra("RepairinfoEntity");

        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        String[] devicePics = repairinfoEntity.getDevPicture().split("|");
        pics= Arrays.asList(devicePics);
        if (pics.size() == 4) {
            pics.remove(3);
        }
        photoAdapter = new PhotoAdapter(pics);
        recyclerViewPhoto.setAdapter(photoAdapter);
        setData(repairinfoEntity);


    }

    private void setData(RepairinfoEntity repairinfoEntity) {
        tvCarNumber.setText(repairinfoEntity.getPlateNumber());
        tvProjectAddress.setText("暂无数据");
        tvConstructionAddress.setText("adfa");
        tvServiceType.setText(repairinfoEntity.getTypeName());
        if (repairinfoEntity.getStatus().equals("0")) {
            tvState.setText("总经理已审批");
        } else if (repairinfoEntity.getStatus().equals("1")) {
            tvState.setText("项目经理已审批");
        } else if (repairinfoEntity.getStatus().equals("2")) {
            tvState.setText("待审批");
        } else if (repairinfoEntity.getStatus().equals("3")) {
            tvState.setText("已撤销");
            btnOperator.setVisibility(View.GONE);
        }
        tvMaintenanceTime.setText(repairinfoEntity.getRepairBeginTime().split("T")[0]);
        tvReason.setText(repairinfoEntity.getCauseName());
        tvContent.setText(repairinfoEntity.getRepairContent());
        tvRemark.setText(repairinfoEntity.getRemark());
    }
}

