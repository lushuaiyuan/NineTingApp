package com.zzti.lsy.ninetingapp.home.production;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RecordEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.HashMap;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/7 16:26
 * 单车生产详情
 */
public class OneCarProDetailActivity extends BaseActivity {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_total_oilMass)
    TextView tvTotalOilMass;//总油耗
    @BindView(R.id.tv_total_proAmount)
    TextView tvTotalProAmount;//总方量
    @BindView(R.id.tv_total_ratio)
    TextView tvTotalRatio;//总油耗比
    @BindView(R.id.tv_month_oilMass)
    TextView tvMonthOilMass;//月油耗
    @BindView(R.id.tv_month_proAmount)
    TextView tvMonthProAmount;//月方量
    @BindView(R.id.tv_month_ratio)
    TextView tvMonthRatio;//月油耗比
    @BindView(R.id.tv_today_oilmass)
    TextView tvTodayOilMass;//今日油耗
    @BindView(R.id.tv_today_proamount)
    TextView tvTodayProAmount;//今日方量
    @BindView(R.id.tv_today_ratio)
    TextView tvTodayRatio;//今日油耗比
    @BindView(R.id.tv_yesterday_oilmass)
    TextView tvYesterDayOilMass;//昨日油耗
    @BindView(R.id.tv_yesterday_proAmount)
    TextView tvYesterDayProamount;//昨日方量
    @BindView(R.id.tv_yesterday_ratio)
    TextView tvYesterDayRatio;//昨日油耗比
    @BindView(R.id.iv_ratioStatus)
    ImageView ivRatioStatus;//油耗比的幅度
    @BindView(R.id.iv_amountStatus)
    ImageView ivAmountStatus;//方量的幅度
    @BindView(R.id.iv_oilStatus)
    ImageView ivOilStatus;//油耗的幅度

    @Override
    public int getContentViewId() {
        return R.layout.activity_onecar_pro_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        intiView();
        initData();
    }

    private void initData() {
        showDia();
        getRecord();
    }

    private void getRecord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", tvCarNumber.getText().toString());
        OkHttpManager.postFormBody(Urls.RECORD_GETCARRECORD, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    RecordEntity recordEntity = ParseUtils.parseJson(msgInfo.getData(), RecordEntity.class);
                    //总
                    tvTotalProAmount.setText(recordEntity.getZsquarequantity());
                    tvTotalOilMass.setText(recordEntity.getZqilwear());
                    tvTotalRatio.setText(String.valueOf(Double.parseDouble(recordEntity.getZratio()) * 100));
                    //月
                    tvMonthProAmount.setText(recordEntity.getMsquarequantity());
                    tvMonthOilMass.setText(recordEntity.getMqilwear());
                    tvMonthRatio.setText(String.valueOf(Double.parseDouble(recordEntity.getMratio()) * 100));
                    //日
                    tvTodayProAmount.setText(recordEntity.getSquareQuantity());
                    tvTodayOilMass.setText(recordEntity.getQilWear());
                    tvTodayRatio.setText(String.valueOf(Double.parseDouble(recordEntity.getRatio()) * 100));

                    //昨天的
                    if (StringUtil.isNullOrEmpty(recordEntity.getTsquarequantity())) {
                        tvYesterDayProamount.setText("0m³");
                    } else {
                        tvYesterDayProamount.setText(recordEntity.getTsquarequantity() + "m³");
                    }
                    if (StringUtil.isNullOrEmpty(recordEntity.getTqilwear())) {
                        tvYesterDayOilMass.setText("0L");
                    } else {
                        tvYesterDayOilMass.setText(recordEntity.getTqilwear() + "L");
                    }
                    if (StringUtil.isNullOrEmpty(recordEntity.getTratio())) {
                        tvYesterDayRatio.setText("0%");
                    } else {
                        tvYesterDayRatio.setText(recordEntity.getTratio() + "%");
                    }

                    //今天的方量大于昨天的
                    if (Double.parseDouble(recordEntity.getSquareQuantity()) > Double.parseDouble(recordEntity.getTsquarequantity())) {
                        ivAmountStatus.setImageResource(R.mipmap.icon_up);
                    } else if (Double.parseDouble(recordEntity.getSquareQuantity()) < Double.parseDouble(recordEntity.getTsquarequantity())) {
                        ivAmountStatus.setImageResource(R.mipmap.icon_down);
                    } else {
                        ivAmountStatus.setImageResource(R.mipmap.icon_flat);
                    }
                    //今天的油耗大于昨天的
                    if (Double.parseDouble(recordEntity.getQilWear()) > Double.parseDouble(recordEntity.getTqilwear())) {
                        ivOilStatus.setImageResource(R.mipmap.icon_up);
                    } else if (Double.parseDouble(recordEntity.getQilWear()) < Double.parseDouble(recordEntity.getTqilwear())) {
                        ivOilStatus.setImageResource(R.mipmap.icon_down);
                    } else {
                        ivOilStatus.setImageResource(R.mipmap.icon_flat);
                    }
                    //今天的油耗比大于昨天的
                    if (Double.parseDouble(recordEntity.getRatio()) > Double.parseDouble(recordEntity.getTratio())) {
                        ivRatioStatus.setImageResource(R.mipmap.icon_up);
                    } else if (Double.parseDouble(recordEntity.getRatio()) < Double.parseDouble(recordEntity.getTratio())) {
                        ivRatioStatus.setImageResource(R.mipmap.icon_down);
                    } else {
                        ivRatioStatus.setImageResource(R.mipmap.icon_flat);
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

    private void intiView() {
        setTitle("单车生产详情");
        tvCarNumber.setText(UIUtils.getStr4Intent(this, "plateNumber"));
        tvProjectAddress.setText(UIUtils.getStr4Intent(this, "projectName"));
    }

//    @OnClick({R.id.rl_oilMass, R.id.rl_proAmount, R.id.rl_ratio, R.id.rl_oilMassToday, R.id.rl_proAmountToday, R.id.rl_ratioToday})
//    public void viewClick(View view) {
//        switch (view.getId()) {
//            case R.id.rl_oilMass:
//                Intent intent1 = new Intent(this, StatisticDetialListActivity.class);
//                intent1.putExtra("TYPE", 1);
//                startActivity(intent1);
//                break;
//            case R.id.rl_proAmount:
//                Intent intent2 = new Intent(this, StatisticDetialListActivity.class);
//                intent2.putExtra("TYPE", 2);
//                startActivity(intent2);
//                break;
//            case R.id.rl_ratio:
//                Intent intent3 = new Intent(this, StatisticDetialListActivity.class);
//                intent3.putExtra("TYPE", 3);
//                startActivity(intent3);
//                break;
//            case R.id.rl_oilMassToday:
//
//                break;
//            case R.id.rl_proAmountToday:
//
//                break;
//            case R.id.rl_ratioToday:
//
//                break;
//        }
//    }
}
