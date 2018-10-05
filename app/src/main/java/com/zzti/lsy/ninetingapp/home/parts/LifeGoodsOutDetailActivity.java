package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品出库详情
 */
public class LifeGoodsOutDetailActivity extends BaseActivity {
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    @BindView(R.id.tv_outAmount)
    TextView tvOutAmount;
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.tv_recipient)
    TextView tvRecipient;


    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_outdetail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        tvOperator.setText(spUtils.getString(SpUtils.StAFFNAME, ""));
        tvOutTime.setText(UIUtils.getStr4Intent(this, "outTime"));
        tvOutAmount.setText(UIUtils.getStr4Intent(this, "outAmount"));
        tvGoodsName.setText(UIUtils.getStr4Intent(this, "goodsName"));
        tvRecipient.setText(UIUtils.getStr4Intent(this, "staffName"));
    }

    private void initView() {
        setTitle("详情");
    }

    @OnClick({R.id.btn_outRecord})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_outRecord:
                Intent intent = new Intent(this, LifeGoodsOutRecordActivity.class);
                intent.putExtra("lbID", UIUtils.getStr4Intent(this, "lbID"));
                startActivity(intent);
                break;
        }
    }

}
