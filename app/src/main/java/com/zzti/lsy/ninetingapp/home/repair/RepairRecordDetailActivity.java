package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.PhotoActivity;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
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
public class RepairRecordDetailActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
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
    @BindView(R.id.btn_operator1)
    Button btnOperator1;//操作按钮
    @BindView(R.id.btn_operator2)
    Button btnOperator2;//操作按钮
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
        params.put("repairId", repairinfoEntity.getRepairID());
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

    private RepairinfoEntity repairinfoEntity;

    private void initView() {
        setTitle("维修详情");
        //解决卡顿问题
        recycleViewDetail.setHasFixedSize(true);
        recycleViewDetail.setNestedScrollingEnabled(false);
        repairinfoEntity = (RepairinfoEntity) getIntent().getSerializableExtra("RepairinfoEntity");
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        String[] devicePics = repairinfoEntity.getDevPicture().split("\\|");
        pics = Arrays.asList(devicePics);
        if (pics.size() == 4) {
            pics.remove(3);
        }
        photoAdapter = new PhotoAdapter(pics);
        photoAdapter.setOnItemClickListener(this);
        recyclerViewPhoto.setAdapter(photoAdapter);
        setData(repairinfoEntity);

        //（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1拒绝）
        if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
            if (repairinfoEntity.getStatus().equals("2")) {
                tvState.setText("待项目经理审批");
                btnOperator1.setVisibility(View.VISIBLE);
                btnOperator2.setVisibility(View.VISIBLE);
                btnOperator1.setText("通过");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (repairinfoEntity.getStatus().equals("1")) {
                    tvState.setText("待总经理审批");
                }
            }
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
            if (repairinfoEntity.getStatus().equals("1")) {
                tvState.setText("待总经理审批");
                btnOperator2.setVisibility(View.VISIBLE);
                btnOperator1.setVisibility(View.VISIBLE);
                btnOperator1.setText("通过");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator2.setVisibility(View.GONE);
                btnOperator1.setVisibility(View.GONE);
                if (repairinfoEntity.getStatus().equals("2")) {
                    tvState.setText("待项目经理审批");
                }
            }
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 1) {//机械师
            if (repairinfoEntity.getStatus().equals("2")) {
                tvState.setText("待项目经理审批");
                btnOperator1.setVisibility(View.VISIBLE);
                btnOperator2.setVisibility(View.GONE);
                btnOperator1.setText("撤销");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (repairinfoEntity.getStatus().equals("1")) {
                    tvState.setText("待总经理审批");
                }
            }
        }

        if (repairinfoEntity.getStatus().equals("-1")) {
            tvState.setText("已拒绝");
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        } else if (repairinfoEntity.getStatus().equals("3")) {
            tvState.setText("已撤销");
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        } else if (repairinfoEntity.getStatus().equals("0")) {
            tvState.setText("总经理已审批");
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        }
    }

    private void setData(RepairinfoEntity repairinfoEntity) {
        tvCarNumber.setText(repairinfoEntity.getPlateNumber());
        tvProjectAddress.setText(repairinfoEntity.getProjectName());
        tvConstructionAddress.setText("adfa");
        tvServiceType.setText(repairinfoEntity.getTypeName());
        tvMaintenanceStartTime.setText(repairinfoEntity.getRepairBeginTime().split("T")[0]);
        tvMaintenanceEndTime.setText(repairinfoEntity.getRepairOverTime().split("T")[0]);
        tvReason.setText(repairinfoEntity.getCauseName());
        tvContent.setText(repairinfoEntity.getRepairContent());
        tvRemark.setText(repairinfoEntity.getRemark());
    }

    @OnClick({R.id.btn_operator1, R.id.btn_operator2})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_operator1:
                String content = "";
                if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
                    content = "是否同意当前申请？";
                } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
                    content = "是否同意当前申请？";
                } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 1) {//机械师
                    content = "是否撤销当前申请？";
                }
                MAlertDialog.show(this, "提示", content, false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        showDia();
                        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 1) {//机械师
                            cancelOrder();
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
                            approvalOrder(0);
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
                            approvalOrder(1);
                        }
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
            case R.id.btn_operator2:
                MAlertDialog.show(this, "提示", "是否拒绝当前申请？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        approvalOrder(-1);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    /**
     * 审批订单
     *
     * @param status 0 总经理审批  1项目经理审批
     */
    private void approvalOrder(final int status) {
        repairinfoEntity.setStatus(String.valueOf(status));
        HashMap<String, String> params = new HashMap<>();
        params.put("repairJson", new Gson().toJson(repairinfoEntity));
        OkHttpManager.postFormBody(Urls.APPROVE_REPAIR, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent();
                    intent.putExtra("status", status);
                    setResult(2, intent);
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


    /**
     * 撤销订单
     */
    private void cancelOrder() {
        HashMap<String, String> params = new HashMap<>();
        params.put("repairID", repairinfoEntity.getRepairID());
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!StringUtil.isNullOrEmpty(pics.get(position))) {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("url", pics.get(position));
            startActivity(intent);
        }
    }
}

