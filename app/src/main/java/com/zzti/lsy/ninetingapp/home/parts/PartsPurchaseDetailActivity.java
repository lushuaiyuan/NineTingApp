package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsPurchased;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/9 14:56
 * 配件采购工单详情界面
 */
public class PartsPurchaseDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_factory)
    TextView tvFactory;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.tv_timeTitle)
    TextView tvOperatorTitle;//操作时间
    @BindView(R.id.tv_time)
    TextView tvOperatorTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_staffName)
    TextView tvStaffName;
    @BindView(R.id.btn_operator1)
    Button btnOperator1;
    @BindView(R.id.btn_operator2)
    Button btnOperator2;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.et_reason)
    TextView etReason;

    private PartsPurchased partsPurchased;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_purchase_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        partsPurchased = (PartsPurchased) getIntent().getSerializableExtra("partsPurchased");
        tvAmount.setText(partsPurchased.getNumber());
        tvModel.setText(partsPurchased.getPartsModel());
        tvName.setText(partsPurchased.getPartsName());
        tvWay.setText(partsPurchased.getPurchasedChanel());
        tvFactory.setText(partsPurchased.getFactoryName());
        tvPrice.setText(partsPurchased.getPurchasedPrice());
        tvTotalMoney.setText(Integer.parseInt(partsPurchased.getNumber()) * Double.parseDouble(partsPurchased.getPurchasedPrice()) + "");
        tvStaffName.setText(partsPurchased.getStaffName());
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//3配件管理员
            if (partsPurchased.getStatus().equals("5")) {
                tvStatus.setText("待确认入库");
                tvOperatorTitle.setText("申请时间");
                tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                btnOperator1.setText("确认入库");
                btnOperator2.setVisibility(View.GONE);
            } else if (partsPurchased.getStatus().equals("2")) {
                tvStatus.setText("待项目经理审批");
                tvOperatorTitle.setText("申请时间");
                tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                btnOperator1.setText("撤销");
                btnOperator2.setVisibility(View.GONE);
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (partsPurchased.getStatus().equals("1")) {
                    tvStatus.setText("待总经理审批");
                    tvOperatorTitle.setText("项目经理审批时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("4")) {
                    tvStatus.setText("已确认入库");
                    tvOperatorTitle.setText("确认时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                }
            }
        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//2项目经理
            if (partsPurchased.getStatus().equals("2")) {//待项目经理审批
                tvStatus.setText("待项目经理审批");
                tvOperatorTitle.setText("申请时间");
                tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                tvReason.setVisibility(View.VISIBLE);
                etReason.setVisibility(View.VISIBLE);
                btnOperator1.setText("同意");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (partsPurchased.getStatus().equals("1")) {
                    tvStatus.setText("待总经理审批");
                    tvOperatorTitle.setText("项目经理审批时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("4")) {
                    tvStatus.setText("已确认入库");
                    tvOperatorTitle.setText("确认时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("5")) {
                    tvStatus.setText("待确认入库");
                    tvOperatorTitle.setText("申请时间");
                    tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                }
            }
        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//0总经理
            if (partsPurchased.getStatus().equals("1")) {//待总经理审批
                tvStatus.setText("待总经理审批");
                tvOperatorTitle.setText("项目经理审批时间");
                tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                tvReason.setVisibility(View.VISIBLE);
                etReason.setVisibility(View.VISIBLE);
                btnOperator1.setText("同意");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (partsPurchased.getStatus().equals("2")) {//待项目经理审批
                    tvStatus.setText("待项目经理审批");
                    tvOperatorTitle.setText("申请时间");
                    tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("4")) {
                    tvStatus.setText("已确认入库");
                    tvOperatorTitle.setText("确认时间");
                    tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
                } else if (partsPurchased.getStatus().equals("5")) {
                    tvStatus.setText("待确认入库");
                    tvOperatorTitle.setText("申请时间");
                    tvOperatorTime.setText(partsPurchased.getApplyTime().replace("T", " "));
                }
            }
        }

        if (partsPurchased.getStatus().equals("-1")) {
            tvStatus.setText("已拒绝");
            tvOperatorTitle.setText("拒绝时间");
            tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
            tvReason.setVisibility(View.VISIBLE);
            etReason.setVisibility(View.VISIBLE);
            etReason.setText(partsPurchased.getOpinion());
            etReason.setEnabled(false);
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        } else if (partsPurchased.getStatus().equals("3")) {
            tvStatus.setText("已撤销");
            tvOperatorTitle.setText("撤销时间");
            tvOperatorTime.setText(partsPurchased.getPurchasedDate().replace("T", " "));
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        }
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
                } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//配件管理员
                    if (btnOperator1.getText().toString().equals("确认入库")) {
                        content = "是否确认入库？";
                    } else if (btnOperator1.getText().toString().equals("撤销")) {
                        content = "是否撤销当前申请？";
                    }
                }
                MAlertDialog.show(this, "提示", content, false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
                            approvalOrder(1);
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
                            approvalOrder(0);
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//配件管理员
                            if (btnOperator1.getText().toString().equals("确认入库")) {
                                confrimOrder();
                            } else if (btnOperator1.getText().toString().equals("撤销")) {
                                canOrder();
                            }
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
     * 确认入库工单
     */
    private void confrimOrder() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("ppID", partsPurchased.getPpID());
        OkHttpManager.postFormBody(Urls.PARTS_CONFIRMSTORAGE, params, tvAmount, new OkHttpManager.OnResponse<String>() {
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
                    intent.putExtra("status", 4);
                    intent.putExtra("date", msgInfo.getData());
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
     * 审批订单
     *
     * @param status 0 总经理审批  1项目经理审批
     */
    private void approvalOrder(final int status) {
        showDia();
        partsPurchased.setStatus(String.valueOf(status));
        HashMap<String, String> params = new HashMap<>();
        params.put("PartsJson", new Gson().toJson(partsPurchased));
        OkHttpManager.postFormBody(Urls.APPROVE_PARTS, params, tvAmount, new OkHttpManager.OnResponse<String>() {
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
                    intent.putExtra("date", msgInfo.getData());
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
    private void canOrder() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("ppID", partsPurchased.getPpID());
        OkHttpManager.postFormBody(Urls.PARTS_CANCELPARTS, params, tvAmount, new OkHttpManager.OnResponse<String>() {
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
                    intent.putExtra("status", 3);
                    intent.putExtra("date", msgInfo.getData());
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


    private void initView() {
        setTitle("工单详情");
    }


}
