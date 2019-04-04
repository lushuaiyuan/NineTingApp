package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 生产录入界面
 */
public class ProductInputActivity extends BaseActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.et_amount)
    EditText etAmount;//生产方量
    @BindView(R.id.et_eight)
    EditText etEight;//8方以下
    @BindView(R.id.et_six)
    EditText etSix;//6方以下
    @BindView(R.id.et_fillThe)
    EditText etFillThe;//补方
    @BindView(R.id.et_remainMaterial)
    EditText etRemainMaterial;//剩料
    @BindView(R.id.et_washTime)
    EditText etWashTime;//洗料耗时
    @BindView(R.id.et_oilMass)
    EditText etOilMass;//加油升数
    @BindView(R.id.et_distance)
    EditText etDistance;//距离基地
    @BindView(R.id.et_overTime)
    EditText etOverTime;//加班时长
    @BindView(R.id.et_overTime_TrainNumber)
    EditText etOverTimeTrainNumber;//加班趟数
    @BindView(R.id.et_timeConsuming)
    EditText etTimeConsuming;//耗时
    @BindView(R.id.et_travelKm)
    EditText etTravelKm;//耗时
    @BindView(R.id.et_washing)
    EditText etWashing;//洗料
    @BindView(R.id.et_water)
    EditText etWater;//水


    @BindView(R.id.et_remark)
    EditText etRemark;//备注

    @BindView(R.id.btn_submit)
    Button btnOperator;//操作按钮
    private int tag;//0代表的是录入 1代表的是修改

    private StatisticalList statisticalList;
    private String slID;

    @Override
    public int getContentViewId() {
        return R.layout.activity_product_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initData() {
        statisticalList = new StatisticalList();
        if (tag == 0) {
            setTitle("生产录入");
            btnOperator.setText("提交");
            tvTime.setText(DateUtil.getCurrentDate());
            tvCarNumber.setEnabled(true);
        } else if (tag == 1) {
            setTitle("生产修改");
            btnOperator.setText("修改");
            StatisticalList statisticalList = (StatisticalList) getIntent().getSerializableExtra("StatisticalList");
            slID = statisticalList.getSlID();
            tvTime.setText(statisticalList.getSlDateTime());
//            etTravelKm.setText(statisticalList.getTravelKm());
            tvCarNumber.setText(statisticalList.getPlateNumber());
            tvCarNumber.setEnabled(false);
            tvCarNumber.setBackgroundColor(getResources().getColor(R.color.color_white));
            etAmount.setText(statisticalList.getSquareQuantity());
            etEight.setText(statisticalList.getEightBelow());
            etSix.setText(statisticalList.getSixBelow());
//            etFillThe.setText(statisticalList.getAddQuantity());
            etRemainMaterial.setText(statisticalList.getRemainder());
//            etWashTime.setText(statisticalList.getWashTime());
            etOilMass.setText(statisticalList.getQilWear());
//            if (!StringUtil.isNullOrEmpty(statisticalList.getDistance())) {
//                etDistance.setText(statisticalList.getDistance());
//            }
//            etOverTime.setText(statisticalList.getAddWorkTime());
//            etOverTimeTrainNumber.setText(statisticalList.getAddWorkCount());
//            etTimeConsuming.setText(statisticalList.getTimeConsuming());
            etWashing.setText(statisticalList.getWashing());
            etWater.setText(statisticalList.getWater());
            if (!StringUtil.isNullOrEmpty(etRemark.getText().toString())) {//备注
                statisticalList.setRemark(etRemark.getText().toString());
            }
        }
    }


    private void initView() {
        tag = UIUtils.getInt4Intent(this, "TAG");
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etOilMass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDistance.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTimeConsuming.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTravelKm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @OnClick({R.id.tv_carNumber, R.id.btn_submit})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_carNumber:
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("FLAG", 1);//获取车牌号
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_submit:
                hideSoftInput(etAmount);
                if (StringUtil.isNullOrEmpty(tvCarNumber.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                    UIUtils.showT("生产方量不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etEight.getText().toString())) {
                    UIUtils.showT("8方以下不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etSix.getText().toString())) {
                    UIUtils.showT("6方以下不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etFillThe.getText().toString())) {
                    UIUtils.showT("补方不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etRemainMaterial.getText().toString())) {
                    UIUtils.showT("剩料不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWashTime.getText().toString())) {
                    UIUtils.showT("洗料耗时不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etOilMass.getText().toString())) {
                    UIUtils.showT("加油升数不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etOverTime.getText().toString())) {
                    UIUtils.showT("加班时长不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etOverTimeTrainNumber.getText().toString())) {
                    UIUtils.showT("加班趟数不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etTimeConsuming.getText().toString())) {
                    UIUtils.showT("耗时不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etTravelKm.getText().toString())) {
                    UIUtils.showT("行驶公里数不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWashing.getText().toString())) {
                    UIUtils.showT("洗料值不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWater.getText().toString())) {
                    UIUtils.showT("水值不能为空");
                    return;
                }
                if (!StringUtil.isNullOrEmpty(slID)) {
                    statisticalList.setSlID(slID);
                }
                statisticalList.setSlDateTime(tvTime.getText().toString());
                statisticalList.setPlateNumber(tvCarNumber.getText().toString());//车牌号
                statisticalList.setSquareQuantity(etAmount.getText().toString());//生产方量
                statisticalList.setEightBelow(etEight.getText().toString());//8方以下
                statisticalList.setSixBelow(etSix.getText().toString());//6方以下
//                statisticalList.setAddQuantity(etFillThe.getText().toString());//补方
                statisticalList.setRemainder(etRemainMaterial.getText().toString());//剩料
//                statisticalList.setWashTime(etWashTime.getText().toString());//洗料耗时
                statisticalList.setQilWear(etOilMass.getText().toString());//加油升数
//                statisticalList.setDistance(etDistance.getText().toString());//距离基地
//                statisticalList.setAddWorkTime(etOverTime.getText().toString());//加班时长
//                statisticalList.setAddWorkCount(etOverTimeTrainNumber.getText().toString());//加班趟数
//                statisticalList.setTimeConsuming(etTimeConsuming.getText().toString());//耗时
                statisticalList.setWashing(etWashing.getText().toString());//洗料
                statisticalList.setWater(etWater.getText().toString());//水
                if (!StringUtil.isNullOrEmpty(etRemark.getText().toString())) {//备注
                    statisticalList.setRemark(etRemark.getText().toString());
                }
                statisticalList.setProjectID(spUtils.getString(SpUtils.PROJECTID, ""));
                statisticalList.setUserID(spUtils.getString(SpUtils.USERID, ""));
                MAlertDialog.show(this, "温馨提示", "是否提交数据？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        showDia();
                        submitInputData();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);

                break;
        }
    }


    private void submitInputData() {
        cancelDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("RecordJson", new Gson().toJson(statisticalList));
        String url = "";
        if (tag == 0) {
            url = Urls.RECORD_ADDRECORD;
        } else if (tag == 1) {
            url = Urls.RECORD_UPDATERECORD;
        }

        OkHttpManager.postFormBody(url, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    tvCarNumber.setText("");
                    etOilMass.getText().clear();
                    etAmount.getText().clear();
                    etDistance.getText().clear();
                    etTimeConsuming.getText().clear();
                    etEight.getText().clear();
                    etSix.getText().clear();
                    etFillThe.getText().clear();
                    etRemainMaterial.getText().clear();
                    etWashTime.getText().clear();
                    etOverTime.getText().clear();
                    etOverTimeTrainNumber.getText().clear();
                    etTravelKm.setText("0");
                    etWashing.setText("0");
                    etWater.setText("0");
                    etRemark.getText().clear();
                    if (tag == 0) {
                        Intent intent = new Intent(ProductInputActivity.this, SuccessActivity.class);
                        intent.putExtra("TAG", 1);
                        startActivity(intent);
                    } else if (tag == 1) {
                        setResult(2);
                        finish();
                        UIUtils.showT("修改成功");
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            tvCarNumber.setText(data.getStringExtra("carNumber"));
        }
    }


    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
