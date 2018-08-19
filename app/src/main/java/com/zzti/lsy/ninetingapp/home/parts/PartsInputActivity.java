package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/8 15:27
 * 配件录入
 */
public class PartsInputActivity extends BaseActivity {
    @BindView(R.id.et_partsName)
    EditText etPartsName;
    @BindView(R.id.et_model)
    EditText etModel;
    @BindView(R.id.et_factory)
    EditText etFactory;
    @BindView(R.id.et_way)
    EditText etWay;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initData() {
        //TODO
        tvOperator.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        tvTime.setText(DateUtil.getCurrentDate());
    }

    private void initView() {
        setTitle("配件录入");
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @OnClick(R.id.btn_submit)
    public void viewClick() {
        if (StringUtil.isNullOrEmpty(etPartsName.getText().toString())) {
            UIUtils.showT("名称不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etModel.getText().toString())) {
            UIUtils.showT("型号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etFactory.getText().toString())) {
            UIUtils.showT("生产厂家不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etWay.getText().toString())) {
            UIUtils.showT("进货渠道不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
            UIUtils.showT("单价不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
            UIUtils.showT("数量不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
            UIUtils.showT("总金额不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvOperator.getText().toString())) {
            UIUtils.showT("经手人不能为空");
            return;
        }
        hideSoftInput(etAmount);
        //TODO
        if (UIUtils.isNetworkConnected()) {
            showDia();
            submitInputData();
        }
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 2);
        startActivity(intent);
    }


    private void submitInputData() {
        cancelDia();
        etPartsName.getText().clear();
        etModel.getText().clear();
        etPrice.getText().clear();
        etAmount.getText().clear();
        tvTotalMoney.setText("");
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
