package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品出库
 */
public class LifeGoodsOutActivity extends BaseActivity {
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.et_outAmount)
    EditText etOutAmount;
    @BindView(R.id.et_recipient)
    EditText etRecipient;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_out;
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
        tvOutTime.setText(DateUtil.getCurrentDate());
    }

    private void initView() {
        setTitle("日用品出库");
    }

    @OnClick(R.id.btn_out)
    public void viewClick() {
        if (StringUtil.isNullOrEmpty(tvOutTime.getText().toString())) {
            UIUtils.showT("出库时间不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etOutAmount.getText().toString())) {
            UIUtils.showT("出库数量不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etRecipient.getText().toString())) {
            UIUtils.showT("领用人不能为空");
            return;
        }
        goodsLifeOut();
    }

    private void goodsLifeOut() {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 5);
        startActivity(intent);
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
