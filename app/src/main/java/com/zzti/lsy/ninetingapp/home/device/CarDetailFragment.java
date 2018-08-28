package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备详情
 */
public class CarDetailFragment extends BaseFragment {
    @BindView(R.id.tv_carType)
    TextView tvCarType;//车辆类型
    @BindView(R.id.tv_emission_standard)
    TextView tvEmission_standard;//排放量
    @BindView(R.id.tv_address)
    TextView tvAddress;//存放地点
    @BindView(R.id.tv_map)
    TextView tvMap;//查看地图
    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer;//生产厂家
    @BindView(R.id.tv_vin)
    TextView tvVin;//识别码
    @BindView(R.id.tv_engine_number)
    TextView tvEngineNumber;//发动机编号
    @BindView(R.id.tv_buyTime)
    TextView tvBuyTime;//购买日期
    @BindView(R.id.tv_buyMoney)
    TextView tvBuyMoney;//购买金额
    @BindView(R.id.tv_drivingNumber)
    TextView tvDrivingNumber;//行驶证号码
    @BindView(R.id.tv_drivingNumber_giveTime)
    TextView tvDrivingNumberGiveTime;//行驶证发放日期
    @BindView(R.id.tv_drivingNumber_validityTime)
    TextView tvDrivingNumberValidityTime;//行驶证有效期

    public static CarDetailFragment newInstance() {
        CarDetailFragment fragment = new CarDetailFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_map, R.id.btn_outPut})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_map:
                startActivity(new Intent(mActivity, MapActivity.class));
                break;
            case R.id.btn_outPut:

                break;
        }
    }
}
