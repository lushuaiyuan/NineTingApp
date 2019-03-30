package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.event.PartsPurchaseMsg;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品详情
 */
public class LifeGoodsDetailActivity extends BaseActivity {
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.et_alarmValue)
    EditText etAlarmValue;


    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private LaoBao laoBao;

    private void initData() {
        laoBao = (LaoBao) getIntent().getSerializableExtra("LaoBao");
        tvGoodsName.setText(laoBao.getLbName());
        tvPrice.setText(laoBao.getPrice());
        etAmount.setText(laoBao.getLaobaoNumber());
        etAmount.setSelection(etAmount.getText().length());
        etAlarmValue.setText(laoBao.getAlarmNumber());
        etAlarmValue.setSelection(etAlarmValue.getText().length());
        tvMoney.setText(Integer.parseInt(laoBao.getLaobaoNumber()) * Double.parseDouble(laoBao.getPrice()) + "");
    }

    private void initView() {
        setTitle("日用品详情");
    }

    @OnClick({R.id.btn_out, R.id.btn_outRecord, R.id.btn_submit, R.id.btn_alert})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out://日用品出库
                Intent intent = new Intent(this, LifeGoodsOutActivity.class);
                intent.putExtra("lbID", laoBao.getLbID());
                intent.putExtra("lbName", laoBao.getLbName());
                intent.putExtra("lbNumber", laoBao.getLaobaoNumber());
                startActivity(intent);
                break;
            case R.id.btn_outRecord://出库记录
                Intent intent2 = new Intent(this, LifeGoodsOutRecordActivity.class);
                intent2.putExtra("lbID", laoBao.getLbID());
                startActivity(intent2);
                break;
            case R.id.btn_submit://提交库存
                MAlertDialog.show(this, "温馨提示", "是否提交库存数量？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        alertNumber();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                },true);
                break;
            case R.id.btn_alert://修改告警值
                MAlertDialog.show(this, "温馨提示", "是否修改库存告警值？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        alertAlarmNumber();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    /**
     * 修改库存值
     */
    private void alertNumber() {
        showDia();
        laoBao.setLaobaoNumber(etAmount.getText().toString());
        HashMap<String, String> params = new HashMap<>();
        params.put("jsonList", new Gson().toJson(laoBao));
        params.put("type", "1");
        OkHttpManager.postFormBody(Urls.PARTS_UPDATENUMBER, params, tvGoodsName, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                showDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
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
     * 修改告警值
     */
    private void alertAlarmNumber() {
        showDia();
        laoBao.setAlarmNumber(etAlarmValue.getText().toString());
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "1");
        params.put("json", new Gson().toJson(laoBao));
        OkHttpManager.postFormBody(Urls.PARTS_UPDATEALARM, params, tvGoodsName, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    EventBus.getDefault().post(new EventMessage(C.EventCode.G, null));
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
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
