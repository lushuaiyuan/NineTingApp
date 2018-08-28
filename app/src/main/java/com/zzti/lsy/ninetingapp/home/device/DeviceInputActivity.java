package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceInputActivity extends BaseActivity {
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.tv_emission_standard)
    TextView tvEmissionStandard; //排放标准
    @BindView(R.id.tv_carState)
    TextView tvCarState;//车辆状态
    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer;//生产厂家
    @BindView(R.id.et_vin)
    EditText etVin;//识别码
    @BindView(R.id.et_engine_number)
    EditText etEngineNumber;//发动机号
    @BindView(R.id.tv_buyTime)
    TextView tvBuyTime;//购买时间
    @BindView(R.id.et_buyMoney)
    EditText etBuyMoney;//购买金额
    @BindView(R.id.et_drivingNumber)
    EditText etDrivingNumber;//行驶证号码
    @BindView(R.id.tv_drivingNumber_giveTime)
    TextView tvDrivingNumberGiveTime;//行驶证发放日期
    @BindView(R.id.et_drivingNumber_validityTime)
    EditText etDrivingNumberValidityTime;//行驶证有效期

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {

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

    private void initView() {
        setTitle("设备录入");
    }

    @OnClick({R.id.tv_carType, R.id.tv_emission_standard, R.id.tv_carState, R.id.tv_manufacturer, R.id.tv_buyTime, R.id.tv_drivingNumber_giveTime, R.id.btn_inputYearInsurance})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_carType:

                break;
            case R.id.tv_emission_standard:

                break;
            case R.id.tv_carState:

                break;
            case R.id.tv_manufacturer:

                break;
            case R.id.tv_buyTime:

                break;
            case R.id.tv_drivingNumber_giveTime:

                break;
            case R.id.btn_inputYearInsurance:
                startActivity(new Intent(this, YearInsuranceActivity.class));
                break;

        }
    }

}
