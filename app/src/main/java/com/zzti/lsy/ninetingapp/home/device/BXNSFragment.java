package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.InsuranceEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备保险年审
 */
public class BXNSFragment extends BaseFragment {
    @BindView(R.id.tv_nsDay)
    TextView tvNsDay;//年审时间天
    @BindView(R.id.tv_qbxDay)
    TextView tvQbxDay;//强险时间天
    @BindView(R.id.tv_sbxDay)
    TextView tvSbxDay;//商险时间天
    @BindView(R.id.tv_yearTime)
    TextView tvYearTime;//年检日期
    @BindView(R.id.tv_yearExprie)
    TextView tvYearExprie;//年检时限
    @BindView(R.id.tv_qbx_EndTime)
    TextView tvQbxEndTime;//强制保险到期时间
    @BindView(R.id.tv_qbx_company)
    TextView tvQbxCompany;//强制保险公司
    @BindView(R.id.tv_qbx_address)
    TextView tvQbxAddress;//强险保单原件所在地
    @BindView(R.id.tv_sbx_EndTime)
    TextView tvSbxEndTime;//商业保险到期时间
    @BindView(R.id.tv_sbx_company)
    TextView tvSbxCompany;//商业保险公司
    @BindView(R.id.tv_sbx_address)
    TextView tvSbxAddress;//商险保单原件所在地
//    @BindView(R.id.btn_alertqBx)
//    Button btnAlertQBx;
//    @BindView(R.id.btn_alertsBx)
//    Button btnAlertSNs;
//    @BindView(R.id.btn_alertNs)
//    Button btnAlertNs;


    private CarInfoEntity carInfoEntity;
    private String beforeSOverDate;
    private String beforeQOverDate;
    private String beforeYOverDate;

    public static BXNSFragment newInstance() {
        BXNSFragment fragment = new BXNSFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            carInfoEntity = (CarInfoEntity) arguments.getSerializable("carInfoEntity");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bx_ns;
    }

    @Override
    protected void initView() {
        String nsEndTime = DateUtil.getAfterMonth(carInfoEntity.getYearTime(), Integer.parseInt(carInfoEntity.getYearExprie()) * 12);
        int t1 = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), nsEndTime);
        tvNsDay.setText(String.valueOf(t1));
        int t2 = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), tvQbxEndTime.getText().toString().split("T")[0]);
        tvQbxDay.setText(String.valueOf(t2));
        int t3 = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), tvSbxEndTime.getText().toString().split("T")[0]);
        tvSbxDay.setText(String.valueOf(t3));
        tvYearTime.setText(carInfoEntity.getYearTime());
        tvYearExprie.setText(carInfoEntity.getYearExprie());
        tvSbxEndTime.setText(carInfoEntity.getsOverTime());
        tvSbxAddress.setText(carInfoEntity.getsAddress());
        tvSbxCompany.setText(carInfoEntity.getsCompany());
        tvQbxEndTime.setText(carInfoEntity.getqOverTime());
        tvQbxAddress.setText(carInfoEntity.getqAddress());
        tvQbxCompany.setText(carInfoEntity.getqCompany());

    }

    @Override
    protected void initData() {
//        showDia();
//        getBxType();
        //TODO
        beforeQOverDate = carInfoEntity.getqOverTime();
        beforeSOverDate = carInfoEntity.getsOverTime();
        beforeYOverDate = DateUtil.getAfterMonth(carInfoEntity.getYearTime(), Integer.parseInt(carInfoEntity.getYearExprie()) * 12);
    }

//    /**
//     * 获取保险类型
//     */
//    private void getBxType() {
//        OkHttpManager.postFormBody(Urls.POST_GETINSURANCETYPE, null, tvNsDay, new OkHttpManager.OnResponse<String>() {
//            @Override
//            public String analyseResult(String result) {
//                return result;
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                cancelDia();
//                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
//                if (msgInfo.getCode() == 200) {
//                    try {
//                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            InsuranceEntity insuranceEntity = ParseUtils.parseJson(jsonArray.getString(i), InsuranceEntity.class);
//                            if (insuranceEntity.getInsuranceID().equals(carInfoEntity.getInsuranceID())) {
//                                tvBxContent.setText(insuranceEntity.getInsuranceContent());
//                                return;
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
//                    loginOut();
//                } else {
//                    UIUtils.showT(msgInfo.getMsg());
//                }
//            }
//        });
//    }


    @OnClick({R.id.btn_alertqBx, R.id.btn_alertsBx, R.id.btn_alertNs})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alertqBx://修改强保险
                alertData(1);
                break;
            case R.id.btn_alertsBx://修改商保险
                alertData(2);
                break;
            case R.id.btn_alertNs://修改年审
                alertData(3);
                break;
        }
    }

    private void alertData(final int type) {
        MAlertDialog.show(mActivity, "提示", "是否确定延期一年？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick(String msg) {
                saveData(type);
            }

            @Override
            public void onCancelClick() {

            }
        }, true);
    }

    private void saveData(final int type) {
        if (type == 1) {//强保险
            String aveDate = DateUtil.getAfterMonth(beforeQOverDate, 12);
            carInfoEntity.setqOverTime(aveDate);
        } else if (type == 2) {//商保险
            String aveDate = DateUtil.getAfterMonth(beforeSOverDate, 12);
            carInfoEntity.setsOverTime(aveDate);
        } else if (type == 3) {//年审
            //TODO
            carInfoEntity.setYearExprie(tvYearExprie.getText().toString());
        } else {
            return;
        }
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("carJson", new Gson().toJson(carInfoEntity));
        OkHttpManager.postFormBody(Urls.POST_UPDATCARINFO, params, tvNsDay, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    UIUtils.showT("修改成功");
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.B, true));
                    if (type == 1) {//强保险
                        String qOverDate = DateUtil.getAfterMonth(beforeQOverDate, 12);
                        int dayBetweenTwo = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), qOverDate);
                        tvQbxDay.setText(String.valueOf(dayBetweenTwo));
                        tvQbxEndTime.setText(carInfoEntity.getqOverTime());
                    } else if (type == 2) {//商保险
                        String sOverDate = DateUtil.getAfterMonth(beforeSOverDate, 12);
                        int dayBetweenTwo = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), sOverDate);
                        tvSbxDay.setText(String.valueOf(dayBetweenTwo));
                        tvSbxEndTime.setText(carInfoEntity.getsOverTime());
                    } else {//年审
                        //TODO
                        String yOverDate = DateUtil.getAfterMonth(carInfoEntity.getYearTime(), Integer.parseInt(tvYearExprie.getText().toString()) * 12);
                        int dayBetweenTwo = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(),yOverDate );
                        tvNsDay.setText(String.valueOf(dayBetweenTwo));
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    if (type == 1) {//强保险
                        carInfoEntity.setqOverTime(beforeQOverDate);
                    } else if (type == 2) {//商保险
                        carInfoEntity.setsOverTime(beforeSOverDate);
                    } else if (type == 3) {//年审
                        //TODO
                        carInfoEntity.setsOverTime(beforeYOverDate);
                    }
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                if (type == 1) {//强保险
                    carInfoEntity.setqOverTime(beforeQOverDate);
                } else if (type == 2) {//商保险
                    carInfoEntity.setsOverTime(beforeSOverDate);
                } else if (type == 3) {//年审
                    //TODO
                    carInfoEntity.setsOverTime(beforeYOverDate);
                }
            }
        });
    }
}
