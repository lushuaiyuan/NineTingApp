package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/9 14:56
 * 配件详情界面
 */
public class PartsDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_factory)
    TextView tvFactory;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.et_alarmValue)
    EditText etAlarmValue;

    private PartsInfoEntity partsInfoEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        partsInfoEntity = (PartsInfoEntity) getIntent().getSerializableExtra("PartsInfo");
        etAmount.setText(partsInfoEntity.getPartsNumber());
        etAmount.setSelection(etAmount.getText().toString().length());
        etAlarmValue.setText(partsInfoEntity.getAlarmNumber());
        tvFactory.setText(partsInfoEntity.getFactoryName());
        tvModel.setText(partsInfoEntity.getPartsModel());
        tvName.setText(partsInfoEntity.getPartsName());
        tvPrice.setText(partsInfoEntity.getPurchasedPrice());
        tvTotalMoney.setText(Integer.parseInt(partsInfoEntity.getPartsNumber()) * Double.parseDouble(partsInfoEntity.getPurchasedPrice()) + "");
    }

    @OnClick({R.id.btn_in, R.id.btn_out, R.id.btn_outRecord,R.id.btn_submit,R.id.btn_alert})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_in://入库
                Intent intent1 = new Intent(this, PartsInputActivity.class);
                intent1.putExtra("TAG", 2);//代表入库
                intent1.putExtra("PartsInfo", partsInfoEntity);
                startActivity(intent1);

                break;
            case R.id.btn_out://出库
                Intent intent2 = new Intent(this, PartsOutDetailActivity.class);
                intent2.putExtra("partsID", partsInfoEntity.getPartsID());
                intent2.putExtra("partsName", partsInfoEntity.getPartsName());
                intent2.putExtra("partsModel", partsInfoEntity.getPartsModel());
                intent2.putExtra("partsNumber", partsInfoEntity.getPartsNumber());
                startActivity(intent2);

                break;
            case R.id.btn_outRecord://出库记录
                Intent intent3 = new Intent(this, PartsOutRecordActivity.class);
                intent3.putExtra("PartsID", partsInfoEntity.getPartsID());
                startActivity(intent3);
                break;
            case R.id.btn_submit:
                MAlertDialog.show(this, "温馨提示", "是否提交库存数量？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        alertNumber();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
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
        partsInfoEntity.setPartsNumber(etAmount.getText().toString());
        HashMap<String, String> params = new HashMap<>();
        List<PartsInfoEntity> partsInfoEntities = new ArrayList<>();
        partsInfoEntities.add(partsInfoEntity);
        params.put("jsonList", new Gson().toJson(partsInfoEntities));
        params.put("type", "0");
        OkHttpManager.postFormBody(Urls.PARTS_UPDATENUMBER, params, tvFactory, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
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
        partsInfoEntity.setAlarmNumber(etAlarmValue.getText().toString());
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "0");
        params.put("json", new Gson().toJson(partsInfoEntity));
        OkHttpManager.postFormBody(Urls.PARTS_UPDATEALARM, params, tvFactory, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    EventBus.getDefault().post(new EventMessage(C.EventCode.H, null));
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
        setTitle("配件详情");
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
