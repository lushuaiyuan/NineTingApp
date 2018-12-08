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
import com.zzti.lsy.ninetingapp.entity.ProductRecordEntity;
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
    @BindView(R.id.et_washMaterial)
    EditText etWashMaterial;//洗料耗时
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
    @BindView(R.id.et_remark)
    EditText etRemark;//备注
    @BindView(R.id.btn_submit)
    Button btnOperator;//操作按钮
    private int tag;//0代表的是录入 1代表的是修改

    private StatisticalList statisticalList;

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
            //TODO
            ProductRecordEntity productRecordEntity = (ProductRecordEntity) getIntent().getSerializableExtra("productRecordEntity");
            tvTime.setText(productRecordEntity.getTime());
            tvCarNumber.setText(productRecordEntity.getCarNumber());
            tvCarNumber.setEnabled(false);
            etAmount.setText(productRecordEntity.getProductAmount());
            etEight.setText(productRecordEntity.getEight());
            etSix.setText(productRecordEntity.getSix());
            etRemainMaterial.setText(productRecordEntity.getRemainMaterial());
            etWashMaterial.setText(productRecordEntity.getWashMaterial());
            if (!StringUtil.isNullOrEmpty(productRecordEntity.getDistance()))
                etDistance.setText(productRecordEntity.getDistance());
            etOilMass.setText(productRecordEntity.getOilMass());
            etOverTime.setText(productRecordEntity.getOverTime());
            etOverTimeTrainNumber.setText(productRecordEntity.getOverTimeTrainNumber());
            etTimeConsuming.setText(productRecordEntity.getTimeConsuming());
            etRemark.setText(productRecordEntity.getRemark());
        }


    }


    private void initView() {
        tag = UIUtils.getInt4Intent(this, "TAG");
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etOilMass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDistance.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTimeConsuming.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
                if (StringUtil.isNullOrEmpty(etOilMass.getText().toString())) {
                    UIUtils.showT("加油升数不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etTimeConsuming.getText().toString())) {
                    UIUtils.showT("耗时不能为空");
                    return;
                }
                if (tag == 0) {
                    statisticalList.setPlateNumber(tvCarNumber.getText().toString());
                    statisticalList.setSlDateTime(tvTime.getText().toString());
                    statisticalList.setProjectID(spUtils.getString(SpUtils.PROJECTID, ""));
                    statisticalList.setSquareQuantity(etAmount.getText().toString());
                    statisticalList.setQilWear(etOilMass.getText().toString());
                    statisticalList.setDistance(etDistance.getText().toString());
                    statisticalList.setUserID(spUtils.getString(SpUtils.USERID, ""));
                    statisticalList.setTimeConsuming(etTimeConsuming.getText().toString());
                    if (!StringUtil.isNullOrEmpty(etRemark.getText().toString())) {
                        statisticalList.setRemark(etRemark.getText().toString());
                    }
                    if (UIUtils.isNetworkConnected()) {
                        showDia();
                        submitInputData();
                    }
                } else if (tag == 1) {//修改
                    //TODO
                }
                break;
        }
    }


    private void submitInputData() {
        cancelDia();
        tvCarNumber.setText("");
        etOilMass.getText().clear();
        etAmount.getText().clear();
        etDistance.getText().clear();
        etTimeConsuming.getText().clear();
        etRemark.getText().clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("RecordJson", new Gson().toJson(statisticalList));
        OkHttpManager.postFormBody(Urls.RECORD_ADDRECORD, params, tvCarNumber, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(ProductInputActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 1);
                    startActivity(intent);
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
