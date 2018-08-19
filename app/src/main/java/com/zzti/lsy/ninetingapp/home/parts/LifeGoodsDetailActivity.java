package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品详情
 */
public class LifeGoodsDetailActivity extends BaseActivity {
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_money)
    TextView tvMoney;


    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("详情");
    }

    @OnClick({R.id.btn_out, R.id.btn_outRecord})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out://日用品出库
                startActivity(new Intent(this, LifeGoodsOutActivity.class));
                break;
            case R.id.btn_outRecord:
                startActivity(new Intent(this, LifeGoodsOutRecordActivity.class));
                break;
        }
    }

}
