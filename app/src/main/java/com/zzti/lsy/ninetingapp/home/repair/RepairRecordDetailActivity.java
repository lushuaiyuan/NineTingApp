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
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tv_maintenanceStartTime)
    TextView tvMaintenanceStartTime;//计划维修开始时间
    @BindView(R.id.tv_maintenanceEndTime)
    TextView tvMaintenanceEndTime;//计划维修结束时间
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
        requiredPartsAdapter.setType(2);//不能修改信息
        recycleViewDetail.setAdapter(requiredPartsAdapter);

        if (UIUtils.isNetworkConnected()) {
            showDia();
            getData();
        }
    }

    private void getData() {
        cancelDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("repairId", repairID);
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRPARTS, params, recycleViewDetail, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RequiredParts requiredParts = ParseUtils.parseJson(jsonArray.getString(i), RequiredParts.class);
                            requiredPartsList.add(requiredParts);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                requiredPartsAdapter.notifyDataSetChanged();
            }
        });
    }

    private String repairID;

    private void initView() {
        setTitle("维修申请");
        //解决卡顿问题
        recycleViewDetail.setHasFixedSize(true);
        recycleViewDetail.setNestedScrollingEnabled(false);
        RepairinfoEntity repairinfoEntity = (RepairinfoEntity) getIntent().getSerializableExtra("RepairinfoEntity");
        repairID = repairinfoEntity.getRepairID();
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        String[] devicePics = repairinfoEntity.getDevPicture().split("\\|");
        pics = Arrays.asList(devicePics);
        if (pics.size() == 4) {
            pics.remove(3);
        }
        photoAdapter = new PhotoAdapter(pics);
        recyclerViewPhoto.setAdapter(photoAdapter);
        setData(repairinfoEntity);

        if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理

        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 0) {//总经理

        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 1) {//机械师
            if (tvState.getText().toString().equals("待审批")) {
                btnOperator.setText("撤销");
            }
        }
    }

    private void setData(RepairinfoEntity repairinfoEntity) {
        tvCarNumber.setText(repairinfoEntity.getPlateNumber());
        tvProjectAddress.setText(repairinfoEntity.getProjectName());
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
        tvMaintenanceStartTime.setText(repairinfoEntity.getRepairBeginTime().split("T")[0]);
        tvMaintenanceEndTime.setText(repairinfoEntity.getRepairOverTime().split("T")[0]);
        tvReason.setText(repairinfoEntity.getCauseName());
        tvContent.setText(repairinfoEntity.getRepairContent());
        tvRemark.setText(repairinfoEntity.getRemark());
    }

    @OnClick(R.id.btn_operator)
    public void viewClick(View view) {
        MAlertDialog.show(this, "提示", "是否撤销维修申请", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick(String msg) {
                showDia();
                revocationData();
            }

            @Override
            public void onCancelClick() {

            }
        }, true);
    }

    /**
     * 撤销数据
     */
    private void revocationData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("repairID", repairID);
        OkHttpManager.postFormBody(Urls.POST_CANCELREPAIR, params, recycleViewDetail, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    setResult(2);
                    UIUtils.showT("撤销成功");
                    finish();
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }
}

