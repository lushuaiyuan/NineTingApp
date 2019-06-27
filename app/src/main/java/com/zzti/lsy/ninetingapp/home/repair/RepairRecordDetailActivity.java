package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.PhotoActivity;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
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
    TextView tvCarNumber;//车牌号
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;//项目部
    @BindView(R.id.tv_receiptNo)
    TextView tvRecepitNo;//维修单号
    @BindView(R.id.tv_repairParts)
    TextView tvRepairParts;//维修配件费用
    @BindView(R.id.tv_repairMoney)
    TextView tvRepairMoney;//维修费用
    @BindView(R.id.tv_repairAllMoney)
    TextView tvRepairAllMoney;//维修总费用
    @BindView(R.id.tv_repairFactory)
    TextView tvRepairFactory;//维修厂商
    @BindView(R.id.tv_repairApplyTime)
    TextView tvRepairApplyTime;//维修申请时间
    @BindView(R.id.tv_staffName)
    TextView tvStaffName;//维修申请人
    //    @BindView(R.id.tv_state)
//    TextView tvState;//状态
    @BindView(R.id.recycleView_detail)
    RecyclerView recycleViewDetail;
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;
    @BindView(R.id.tv_remark)
    TextView tvRemark;//维修原因
    @BindView(R.id.btn_operator1)
    Button btnOperator1;//操作按钮
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
                            requiredParts.setNumber(requiredParts.getRpNumber());
                            requiredParts.setRpNumber("0");
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
    }

    private void setData(RepairinfoEntity repairinfoEntity) {
        tvCarNumber.setText(repairinfoEntity.getPlateNumber());
        tvProjectAddress.setText(repairinfoEntity.getProjectName());
        tvRecepitNo.setText(repairinfoEntity.getReceiptNo());
        tvRepairParts.setText(repairinfoEntity.getRepairParts());
        tvRepairMoney.setText(repairinfoEntity.getRepairMoney());
        tvRepairAllMoney.setText(repairinfoEntity.getRepairAllMoney());
        tvRepairFactory.setText(repairinfoEntity.getRepairFactory());
        tvRepairApplyTime.setText(repairinfoEntity.getRepairApplyTime().split("T")[0]);
        tvStaffName.setText(repairinfoEntity.getStaffName());
        tvRemark.setText(repairinfoEntity.getRemark());
    }

    @OnClick({R.id.btn_operator1})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_operator1:
                for (int i = 0; i < requiredPartsList.size(); i++) {
                    if (StringUtil.isNullOrEmpty(requiredPartsList.get(i).getRpNumber()) || "0".equals(requiredPartsList.get(i).getRpNumber())) {
                        UIUtils.showT(requiredPartsList.get(i).getPartsName() + "的退库数量不能为空");
                        return;
                    }
                    if (requiredPartsList.get(i).getNumber().equals("0")) {
                        UIUtils.showT(requiredPartsList.get(i).getPartsName() +"的库存为空，不能退库");
                        return;
                    }
                }
                MAlertDialog.show(this, "提示", "是否确认退库？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        cancel();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    /**
     * 退库操作
     */
    private void cancel() {
        HashMap<String, String> params = new HashMap<>();
        params.put("partsJson", new Gson().toJson(requiredPartsList));
        OkHttpManager.postFormBody(Urls.REPAIR_UPDATEREPAIRPARTS, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    UIUtils.showT("修改成功");
                    finish();
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
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

