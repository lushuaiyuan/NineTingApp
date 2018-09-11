package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.InsuranceEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.InsuranceAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 保险年审时间
 */
public class YearInsuranceActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.tv_ns_endTime)
    TextView tvNsEndTime;//年审到期时间
    @BindView(R.id.tv_bx_type)
    TextView tvBxType;//保险类型
    @BindView(R.id.tv_bx_buyTime)
    TextView tvBxBuyTime;//保险购买时间
    @BindView(R.id.tv_bx_endTime)
    TextView tvBxEndTime;//保险到期时间


    //车辆类型pop
    private PopupWindow popupWindowInsurance;
    private ListView mListViewInsurance;
    private InsuranceAdapter insuranceAdapter;
    private List<InsuranceEntity> insuranceEntities;

    CarInfoEntity carInfoEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_year_insurance;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initPop();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowInsurance = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowInsurance.setFocusable(true);
        popupWindowInsurance.setOutsideTouchable(true);
        //设置消失监听
        popupWindowInsurance.setOnDismissListener(this);
        popupWindowInsurance.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowInsurance.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewInsurance = contentview.findViewById(R.id.pop_list);
        insuranceEntities = new ArrayList<>();
        insuranceAdapter = new InsuranceAdapter(insuranceEntities);
        mListViewInsurance.setAdapter(insuranceAdapter);
        mListViewInsurance.setOnItemClickListener(this);
        popupWindowInsurance.setAnimationStyle(R.style.anim_bottomPop);
    }


    private void initView() {
        setTitle("设备录入");
        carInfoEntity = (CarInfoEntity) getIntent().getSerializableExtra("carInfoEntity");
    }

    @OnClick({R.id.tv_ns_endTime, R.id.tv_bx_type, R.id.tv_bx_buyTime, R.id.tv_bx_endTime, R.id.btn_inputYearInsurance})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ns_endTime:
                showCustomTime(1);
                break;
            case R.id.tv_bx_buyTime:
                showCustomTime(2);
                break;
            case R.id.tv_bx_type:
                showDia();
                getBxType();
                break;
            case R.id.tv_bx_endTime:
                showCustomTime(3);
                break;
            case R.id.btn_inputYearInsurance:

                MAlertDialog.show(this, "提示", "是否录入数据？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        submitData();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                },true);
                break;
        }
    }

    /**
     * 提交车辆数据
     */
    private void submitData() {
        if (StringUtil.isNullOrEmpty(tvNsEndTime.getText().toString())) {
            UIUtils.showT("请选择年审到期时间");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvBxBuyTime.getText().toString())) {
            UIUtils.showT("请选择保险购买时间");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvBxEndTime.getText().toString())) {
            UIUtils.showT("请选择保险到期时间");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvBxType.getText().toString())) {
            UIUtils.showT("请选择保险类型");
            return;
        }
        carInfoEntity.setAVEDate(tvNsEndTime.getText().toString());
        carInfoEntity.setIPDate(tvBxBuyTime.getText().toString());
        carInfoEntity.setIEDate(tvBxEndTime.getText().toString());
        carInfoEntity.setInsuranceID(insuranceID);
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("carJson", new Gson().toJson(carInfoEntity));
        OkHttpManager.postFormBody(Urls.POST_ADDCAR, params, tvBxType, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(YearInsuranceActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 6);
                    startActivity(intent);
                    finish();
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });

    }

    /**
     * 获取保险类型数据
     */
    private void getBxType() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETINSURANCETYPE, params, tvBxType, new OkHttpManager.OnResponse<String>() {
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
                            insuranceEntities.add(insuranceEntity);
                        }
                        if (insuranceEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (insuranceEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = insuranceAdapter.getView(0, null, mListViewInsurance);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewInsurance.getDividerHeight()) * 5;
                                mListViewInsurance.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewInsurance.getLayoutParams();
                                params.height = totalHei;
                                mListViewInsurance.setLayoutParams(params);
                            }
                            popupWindowInsurance.showAtLocation(tvBxType, Gravity.BOTTOM, 0, 0);
                        } else {
                            UIUtils.showT("暂无数据");
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

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int tag) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(YearInsuranceActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (tag == 1) {
                    tvNsEndTime.setText(DateUtil.getDate(date));
                } else if (tag == 2) {
                    tvBxBuyTime.setText(DateUtil.getDate(date));
                } else if (tag == 3) {
                    tvBxEndTime.setText(DateUtil.getDate(date));
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

    private String insuranceID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvBxType.setText(insuranceEntities.get(i).getInsuranceName());
        insuranceID = insuranceEntities.get(i).getInsuranceID();
        popupWindowInsurance.dismiss();
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
    }
}
