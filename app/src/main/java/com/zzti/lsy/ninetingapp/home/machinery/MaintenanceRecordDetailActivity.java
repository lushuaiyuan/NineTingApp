package com.zzti.lsy.ninetingapp.home.machinery;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarMaintenanceDetailAdapter;
import com.zzti.lsy.ninetingapp.entity.CarMaintenanceEntity;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 维修记录详情
 */
public class MaintenanceRecordDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_constructionAddress)
    TextView tvConstructionAddress;
    @BindView(R.id.tv_serviceType)
    TextView tvServiceType;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.recycleView_detail)
    RecyclerView recycleViewDetail;
    @BindView(R.id.tv_servicePersonnel)
    TextView tvServicePersonnel;//保修人
    @BindView(R.id.tv_servicePersonnelTel)
    TextView tvServicePersonnelTel;//保修人电话
    @BindView(R.id.tv_maintenanceTime)
    TextView tvMaintenanceTime;//计划维修时间
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;
    @BindView(R.id.tv_reason)
    TextView tvReason;//维修原因
    @BindView(R.id.tv_content)
    TextView tvContent;//维修内容
    @BindView(R.id.tv_remark)
    TextView tvRemark;//维修原因

    //照片
    private PhotoAdapter photoAdapter;
    private List<String> pics;

    //维修明细
    private CarMaintenanceDetailAdapter carMaintenanceDetailAdapter;
    private List<CarMaintenanceEntity> carMaintenanceEntities;

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
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        pics = new ArrayList<>();
        photoAdapter = new PhotoAdapter(pics);
        recyclerViewPhoto.setAdapter(photoAdapter);
        carMaintenanceEntities = new ArrayList<>();
        //TODO 明细的
        for (int i = 0; i < 4; i++) {
            CarMaintenanceEntity carMaintenanceEntity = new CarMaintenanceEntity();
            carMaintenanceEntity.setReason(String.valueOf(i + 1));
            carMaintenanceEntity.setPartsAmount(String.valueOf(i + 1));
            carMaintenanceEntity.setPartsName("配件" + i);
            carMaintenanceEntity.setMoney(String.valueOf(500 + i));
            carMaintenanceEntities.add(carMaintenanceEntity);
        }
        recycleViewDetail.setLayoutManager(new LinearLayoutManager(this));

        carMaintenanceDetailAdapter = new CarMaintenanceDetailAdapter(carMaintenanceEntities);
        recycleViewDetail.setAdapter(carMaintenanceDetailAdapter);

        if (UIUtils.isNetworkConnected()) {
            showDia();
            getData();
        }
    }

    private void getData() {
        cancelDia();
        tvCarNumber.setText("adfa");
        tvProjectAddress.setText("adfa");
        tvConstructionAddress.setText("adfa");
        tvServiceType.setText("adfa");
        tvState.setText("维修中");
        tvServicePersonnel.setText("adfa");
        tvServicePersonnelTel.setText("18337113754");
        tvMaintenanceTime.setText("2018-08-18 09:20");
        tvReason.setText("啊是发了两份啊大家发了健康啊都放假了看见对方啊速度发捡垃圾爱的附加费啦圣诞节啊速度发了空间");
        tvContent.setText("啊是发了两份啊大家发了健康啊都放假了看见对方啊速度发捡垃圾爱的附加费啦圣诞节啊速度发了空间");
        tvRemark.setText("啊是发了两份啊大家发了健康啊都放假了看见对方啊速度发捡垃圾爱的附加费啦圣诞节啊速度发了空间");


    }


    private void initView() {
        setTitle("维修申请");
        //解决卡顿问题
        recycleViewDetail.setHasFixedSize(true);
        recycleViewDetail.setNestedScrollingEnabled(false);
    }
}

