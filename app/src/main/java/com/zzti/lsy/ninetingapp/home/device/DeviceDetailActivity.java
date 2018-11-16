package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/7 14:28
 * 当前类需要根据车牌号获取值
 * 车辆详情
 */
public class DeviceDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.radio_group_carDetail)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_carDetail)
    RadioButton radioButtonCarDetail;
    @BindView(R.id.radio_button_bxns)
    RadioButton radioButtonBxNs;

    private Fragment[] mFragments = new Fragment[2];

    @Override
    public int getContentViewId() {
        return R.layout.activity_car_deatil;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private int tag; //0代表进入车辆详情选中的是详情 1代表保险年审

    private void initData() {
        String carNumber = UIUtils.getStr4Intent(this, "carNumber");
        tvCarNumber.setText(carNumber);
        tag = UIUtils.getInt4Intent(this, "TAG");
        showDia();
        getCarDetail();
    }

    private void getCarDetail() {
        HashMap<String, String> params = new HashMap<>();
        params.put("wherestr", "plateNumber=\"" + tvCarNumber.getText().toString() + "\"");
        params.put("pageIndex", "0");
        OkHttpManager.postFormBody(Urls.POST_GETCARLIST, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        CarInfoEntity carInfoEntity = ParseUtils.parseJson(jsonArray.getString(0), CarInfoEntity.class);
                        showData(carInfoEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    private void showData(CarInfoEntity carInfoEntity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("carInfoEntity", carInfoEntity);
        CarDetailFragment carDetailFragment = CarDetailFragment.newInstance();
        BXNSFragment bxnsFragment = BXNSFragment.newInstance();
        carDetailFragment.setArguments(bundle);
        bxnsFragment.setArguments(bundle);
        mFragments[0] = carDetailFragment;
        mFragments[1] = bxnsFragment;

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
        if (tag == 0) {
            // 保证第一次会回调OnCheckedChangeListener
            radioButtonCarDetail.setChecked(true);
        } else if (tag == 1) {
            radioButtonBxNs.setChecked(true);
        }

        String status = carInfoEntity.getStatus();
        if (status.equals("0")) {
            tvStatus.setText("存放中");
            tvStatus.setTextColor(App.get().getResources().getColor(R.color.color_6bcfd6));
        } else if (status.equals("1")) {
            tvStatus.setText("工作中");
            tvStatus.setTextColor(App.get().getResources().getColor(R.color.color_ffb947));
        } else if (status.equals("2")) {
            tvStatus.setText("维修中");
            tvStatus.setTextColor(App.get().getResources().getColor(R.color.color_fe81b3));
        }
        String projectName = carInfoEntity.getProjectName();
        tvProjectAddress.setText(projectName);
    }

    private void initView() {
        setTitle("车辆详情");
    }
}
