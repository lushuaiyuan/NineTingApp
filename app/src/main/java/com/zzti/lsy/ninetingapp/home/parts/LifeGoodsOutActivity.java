package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaobaoDelivery;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsDelivery;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品出库
 */
public class LifeGoodsOutActivity extends BaseActivity {
    @BindView(R.id.tv_lbName)
    TextView tvLbName;
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.et_outAmount)
    EditText etOutAmount;
    @BindView(R.id.et_recipient)
    EditText etRecipient;
    private String lbID;
    private int lbNumber;

    @Override

    public int getContentViewId() {
        return R.layout.activity_lifegoods_out;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }
    private void initData() {
        lbID = UIUtils.getStr4Intent(this, "lbID");
        String lbName = UIUtils.getStr4Intent(this, "lbName");
        lbNumber = Integer.parseInt(UIUtils.getStr4Intent(this, "lbNumber"));
        tvLbName.setText(lbName);
        //TODO
//        showDia();
//        getStaff();
    }

    /**
     * 获取员工列表
     */
    private void getStaff() {

    }

    private void initView() {
        setTitle("日用品出库");
    }

    @OnClick({R.id.tv_outTime, R.id.btn_out})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_outTime:
                showCustomTime();
                break;
            case R.id.btn_out:
                if (StringUtil.isNullOrEmpty(tvLbName.getText().toString())) {
                    UIUtils.showT("名称不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvOutTime.getText().toString())) {
                    UIUtils.showT("出库时间不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etOutAmount.getText().toString())) {
                    UIUtils.showT("出库数量不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etRecipient.getText().toString())) {
                    UIUtils.showT("领用人不能为空");
                    return;
                }
                if (Integer.parseInt(etOutAmount.getText().toString()) > lbNumber) {
                    etOutAmount.setText("");
                    etOutAmount.setFocusable(true);
                    etOutAmount.setFocusableInTouchMode(true);
                    etOutAmount.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    UIUtils.showT("出库数量不能大于库存数量");
                    return;
                }
                //TODO
                LaobaoDelivery laobaoDelivery = new LaobaoDelivery();
                laobaoDelivery.setLbID(lbID);
                laobaoDelivery.setLdDate(tvOutTime.getText().toString());
                laobaoDelivery.setLdNumber(etOutAmount.getText().toString());
                laobaoDelivery.setStaffID(etRecipient.getText().toString());
                laobaoDelivery.setFromProject(spUtils.getString(SpUtils.PROJECTID, ""));
                laobaoDelivery.setUserID(spUtils.getString(SpUtils.USERID, ""));
                goodsLifeOut(laobaoDelivery);
                break;
        }

    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(LifeGoodsOutActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvOutTime.setText(DateUtil.getDate(date));
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

    /**
     * 出库
     *
     * @param laobaoDelivery
     */
    private void goodsLifeOut(LaobaoDelivery laobaoDelivery) {
        HashMap<String, String> params = new HashMap<>();
        params.put("LaoOutJosn", new Gson().toJson(laobaoDelivery));
        OkHttpManager.postFormBody(Urls.POST_LAOBAOOUT, params, tvLbName, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    finish();
                    Intent intent = new Intent(LifeGoodsOutActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 5);
                    startActivity(intent);
                    UIUtils.showT("出库成功");
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
}
