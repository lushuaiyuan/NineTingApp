package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.DataGenerator;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/7 14:28
 * 车辆详情
 */
public class DeviceDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.radio_group_carDetail)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_carDetail)
    RadioButton radioButtonCarDetail;
    @BindView(R.id.radio_button_bxns)
    RadioButton radioButtonBxNs;

    private Fragment[] mFragments;

    @Override
    public int getContentViewId() {
        return R.layout.activity_car_deatil;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mFragments = DataGenerator.getFragmentCarDetails();
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setTitle("车辆详情");

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            Fragment mFragment = null;

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_carDetail:
                        mFragment = mFragments[0];
                        radioButtonCarDetail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        radioButtonBxNs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        break;
                    case R.id.radio_button_bxns:
                        mFragment = mFragments[1];
                        radioButtonCarDetail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        radioButtonBxNs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        break;
                }
                if (mFragments != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_carDetail, mFragment).commit();
                }
            }
        });
        // 保证第一次会回调OnCheckedChangeListener
        radioButtonCarDetail.setChecked(true);
    }
}
