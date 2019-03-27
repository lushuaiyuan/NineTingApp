package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.utils.SpUtils;

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
        etAlarmValue.setText(laoBao.getAlarmValue());
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
                //TODO

                break;
            case R.id.btn_alert://修改告警值
                //TODO

                break;

        }
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
