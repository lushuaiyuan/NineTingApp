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
    @BindView(R.id.tv_bxDay)
    TextView tvBxDay;
    @BindView(R.id.tv_nsDay)
    TextView tvNsDay;
    @BindView(R.id.tv_ns_EndTime)
    TextView tvNsEndTime;
    @BindView(R.id.tv_bx_EndTime)
    TextView tvBxEndTime;
    @BindView(R.id.tv_bxType)
    TextView tvBxType;
    @BindView(R.id.tv_bxContent)
    TextView tvBxContent;
    @BindView(R.id.btn_alertBx)
    Button btnAlertBx;
    @BindView(R.id.btn_alertNs)
    Button btnAlertNs;


    private CarInfoEntity carInfoEntity;
    private String beforeAVEDate;
    private String beforeIEDate;

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
        int t1 = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), carInfoEntity.getAVEDate().split("T")[0]);
        tvNsDay.setText(String.valueOf(t1));
        if (t1 > 30) {
            btnAlertNs.setVisibility(View.GONE);
        } else {
            btnAlertNs.setVisibility(View.VISIBLE);
        }
        int t2 = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), carInfoEntity.getIEDate().split("T")[0]);
        tvBxDay.setText(String.valueOf(t2));
        if (t2 > 30) {
            btnAlertBx.setVisibility(View.GONE);
        } else {
            btnAlertBx.setVisibility(View.VISIBLE);
        }
        tvNsEndTime.setText(carInfoEntity.getAVEDate().split("T")[0]);
        tvBxEndTime.setText(carInfoEntity.getIEDate().split("T")[0]);
        tvBxType.setText(carInfoEntity.getInsuranceName());
        beforeAVEDate = carInfoEntity.getAVEDate().split("T")[0];
        beforeIEDate = carInfoEntity.getIEDate().split("T")[0];
    }

    @Override
    protected void initData() {
        showDia();
        getBxType();
    }

    /**
     * 获取保险类型
     */
    private void getBxType() {
        OkHttpManager.postFormBody(Urls.POST_GETINSURANCETYPE, null, tvBxType, new OkHttpManager.OnResponse<String>() {
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            InsuranceEntity insuranceEntity = ParseUtils.parseJson(jsonArray.getString(i), InsuranceEntity.class);
                            if (insuranceEntity.getInsuranceID().equals(carInfoEntity.getInsuranceID())) {
                                tvBxContent.setText(insuranceEntity.getInsuranceContent());
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }


    @OnClick({R.id.btn_alertBx, R.id.btn_alertNs})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alertBx://修改保险
                alertData(1);
                break;
            case R.id.btn_alertNs://修改年审
                alertData(2);
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
        if (type == 1) {//保险
            String ieDate = DateUtil.getAfterMonth(beforeIEDate, 12);
            carInfoEntity.setIEDate(ieDate);
        } else if (type == 2) {//年审
            String aveDate = DateUtil.getAfterMonth(beforeAVEDate, 12);
            carInfoEntity.setAVEDate(aveDate);
        } else {
            return;
        }
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("carJson", new Gson().toJson(carInfoEntity));
        OkHttpManager.postFormBody(Urls.POST_UPDATCARINFO, params, tvBxContent, new OkHttpManager.OnResponse<String>() {
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
                    if (type == 1) {
                        String ieDate = DateUtil.getAfterMonth(beforeIEDate, 12);
                        int dayBetweenTwo = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), ieDate);
                        tvBxDay.setText(String.valueOf(dayBetweenTwo));
                        tvBxEndTime.setText(carInfoEntity.getIEDate());
                        btnAlertBx.setVisibility(View.GONE);
                    } else {
                        String aveDate = DateUtil.getAfterMonth(beforeAVEDate, 12);
                        int dayBetweenTwo = DateUtil.getDayBetweenTwo(DateUtil.getCurrentDate(), aveDate);
                        tvNsDay.setText(String.valueOf(dayBetweenTwo));
                        tvNsEndTime.setText(carInfoEntity.getAVEDate());
                        btnAlertNs.setVisibility(View.GONE);
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    if (type == 1) {//保险
                        carInfoEntity.setIEDate(beforeIEDate);
                    } else if (type == 2) {//年审
                        carInfoEntity.setAVEDate(beforeAVEDate);
                    }
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                if (type == 1) {//保险
                    carInfoEntity.setIEDate(beforeIEDate);
                } else if (type == 2) {//年审
                    carInfoEntity.setAVEDate(beforeAVEDate);
                }
            }
        });
    }
}
