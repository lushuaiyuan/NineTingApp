package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaobaoPurchased;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2018/10/5 09:13
 * @Describe 日用品审批详情
 */
public class LifeGoodsPurchaseDetailActivity extends BaseActivity {
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    @BindView(R.id.tv_operatorTitle)
    TextView tvOperatorTitle;
    @BindView(R.id.tv_operatorTime)
    TextView tvOperatorTime;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.et_reason)
    EditText etReason;
    @BindView(R.id.btn_operator1)
    Button btnOperator1;
    @BindView(R.id.btn_operator2)
    Button btnOperator2;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_purchase_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private LaobaoPurchased laobaoPurchased;

    /**
     * status（3为已撤销 2为默认未审批 1项目经理审批 0总经理审批通过 -1为拒绝）
     */
    private void initData() {
        laobaoPurchased = (LaobaoPurchased) getIntent().getSerializableExtra("laobaoPurchased");
        tvGoodsName.setText(laobaoPurchased.getLbName());
        tvOperator.setText(laobaoPurchased.getStaffName());
        tvAmount.setText(laobaoPurchased.getNumber());
        if (!StringUtil.isNullOrEmpty(laobaoPurchased.getPurchasedMoney())) {
            tvPrice.setText(laobaoPurchased.getPurchasedMoney());
            tvMoney.setText(Integer.parseInt(laobaoPurchased.getNumber()) * Double.parseDouble(laobaoPurchased.getPurchasedMoney()) + "");
        }
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//3配件管理员
            if (laobaoPurchased.getStatus().equals("2")) {
                tvStatus.setText("待审批");
                tvOperatorTitle.setText("申请时间");
                tvOperatorTime.setText(laobaoPurchased.getApplyTime().replace("T", " "));
                btnOperator1.setText("撤销");
                btnOperator2.setVisibility(View.GONE);
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (laobaoPurchased.getStatus().equals("1")) {
                    tvStatus.setText("项目经理已审批");
                    tvOperatorTitle.setText("项目经理审批时间");
                    tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                } else if (laobaoPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                }
            }
        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//2项目经理
            if (laobaoPurchased.getStatus().equals("2")) {//待项目经理审批
                tvStatus.setText("待审批");
                tvOperatorTitle.setText("申请时间");
                tvOperatorTime.setText(laobaoPurchased.getApplyTime().replace("T", " "));
                tvReason.setVisibility(View.VISIBLE);
                etReason.setVisibility(View.VISIBLE);
                btnOperator1.setText("同意");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (laobaoPurchased.getStatus().equals("1")) {
                    tvStatus.setText("项目经理已审批");
                    tvOperatorTitle.setText("项目经理审批时间");
                    tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                } else if (laobaoPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                }
            }
        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//0总经理
            if (laobaoPurchased.getStatus().equals("1")) {//待总经理审批
                tvStatus.setText("项目经理已审批");
                tvOperatorTitle.setText("项目经理审批时间");
                tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                tvReason.setVisibility(View.VISIBLE);
                etReason.setVisibility(View.VISIBLE);
                btnOperator1.setText("同意");
                btnOperator2.setText("拒绝");
            } else {
                btnOperator1.setVisibility(View.GONE);
                btnOperator2.setVisibility(View.GONE);
                if (laobaoPurchased.getStatus().equals("2")) {//待项目经理审批
                    tvStatus.setText("待审批");
                    tvOperatorTitle.setText("申请时间");
                    tvOperatorTime.setText(laobaoPurchased.getApplyTime().replace("T", " "));
                } else if (laobaoPurchased.getStatus().equals("0")) {
                    tvStatus.setText("总经理已审批");
                    tvOperatorTitle.setText("总经理审批时间");
                    tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
                }
            }
        }

        if (laobaoPurchased.getStatus().equals("-1")) {
            tvStatus.setText("已拒绝");
            tvOperatorTitle.setText("拒绝时间");
            tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
            tvReason.setVisibility(View.VISIBLE);
            etReason.setVisibility(View.VISIBLE);
            etReason.setText(laobaoPurchased.getOpinion());
            etReason.setEnabled(false);
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        } else if (laobaoPurchased.getStatus().equals("3")) {
            tvStatus.setText("已撤销");
            tvOperatorTitle.setText("撤销时间");
            tvOperatorTime.setText(laobaoPurchased.getPurchasedDate().replace("T", " "));
            btnOperator1.setVisibility(View.GONE);
            btnOperator2.setVisibility(View.GONE);
        }

    }

    private void initView() {
        setTitle("工单详情");
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
                    content = "是否撤销当前申请？";
                }
                MAlertDialog.show(this, "提示", content, false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//配件管理员
                            canOrder();
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
        showDia();
        laobaoPurchased.setStatus(String.valueOf(status));
        HashMap<String, String> params = new HashMap<>();
        params.put("laobaoJson", new Gson().toJson(laobaoPurchased));
        OkHttpManager.postFormBody(Urls.APPROVE_DAYUSE, params, tvAmount, new OkHttpManager.OnResponse<String>() {
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
    private void canOrder() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("lpID", laobaoPurchased.getLpID());
        OkHttpManager.postFormBody(Urls.PARTS_CANCELLAOBAO, params, tvAmount, new OkHttpManager.OnResponse<String>() {
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
}
