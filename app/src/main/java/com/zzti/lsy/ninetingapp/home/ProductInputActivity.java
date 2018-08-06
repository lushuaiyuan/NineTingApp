package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 生产录入界面
 */
public class ProductInputActivity extends BaseActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_oilMass)
    EditText etOilMass;

    @Override
    public int getContentViewId() {
        return R.layout.activity_product_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("生产录入");
    }

    @OnClick({R.id.tv_carNumber, R.id.tv_address, R.id.btn_submit})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address:

                break;
            case R.id.tv_carNumber:
                startActivity(new Intent(this, CarListActivity.class));
                break;
            case R.id.btn_submit:
                startActivity(new Intent(this, InputSuccessActivity.class));
                break;
        }
    }
}
