package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RentCarEntity;
import com.zzti.lsy.ninetingapp.event.C;
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
 * @author lsy
 * @create 2018/12/2 21:08
 * @Describe 设备录入（外租车）
 */
public class RentCarInputFragment extends BaseFragment {
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.et_drivingNumber)
    EditText etDrivingNumber;//行驶证号码
    @BindView(R.id.et_carOwner)
    EditText etCarOwner;//车主
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;//租用起始日期
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;//租用结束日期

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_outside_device_input;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.btn_submit, R.id.tv_startTime, R.id.tv_endTime})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etDrivingNumber.getText().toString())) {
                    UIUtils.showT("行驶证号不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etCarOwner.getText().toString())) {
                    UIUtils.showT("车主不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(tvStartTime.getText().toString())) {
                    UIUtils.showT("租用起始时间不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
                    UIUtils.showT("租用终止时间不能为空");
                    break;
                }
                showDia();
                submitData();
                break;
            case R.id.tv_startTime:
                showCustomTime(1);
                break;
            case R.id.tv_endTime:
                showCustomTime(2);
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submitData() {
        String projectID = SpUtils.getInstance().getString(SpUtils.PROJECTID, "");
        RentCarEntity rentCarEntity = new RentCarEntity();
        rentCarEntity.setPlateNumber(etCarNumber.getText().toString());
        rentCarEntity.setDrivingLicenseNumber(etDrivingNumber.getText().toString());
        rentCarEntity.setOverTime(tvEndTime.getText().toString());
        rentCarEntity.setStartTime(tvStartTime.getText().toString());
        rentCarEntity.setOwnerName(etCarOwner.getText().toString());
        rentCarEntity.setProjectID(projectID);
        HashMap<String, String> params = new HashMap<>();
        params.put("rentCarJson", new Gson().toJson(rentCarEntity));
        OkHttpManager.postFormBody(Urls.POST_ADDRENTCAR, params, tvEndTime, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(mActivity, SuccessActivity.class);
                    intent.putExtra("TAG", 6);
                    startActivity(intent);
                    mActivity.finish();
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int tag) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (tag == 1) {
                    tvStartTime.setText(DateUtil.getDate(date));
                } else if (tag == 2) {
                    tvEndTime.setText(DateUtil.getDate(date));
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

}
