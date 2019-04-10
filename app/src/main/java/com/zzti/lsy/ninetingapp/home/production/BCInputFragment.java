package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.generalmanager.StaffListActivity;
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
 * @author lsy
 * @create 2019/4/4 14:06
 * @Describe 泵车录入的Fragment
 */
public class BCInputFragment extends BaseFragment {
    @BindView(R.id.tv_time)
    TextView tvTime; //时间
    @BindView(R.id.tv_projectName)
    TextView tvProjectName; //项目部
    @BindView(R.id.tv_staffName)
    TextView tvStaffName; //经手人
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;//车牌号
    @BindView(R.id.tv_carType)
    TextView tvCarType;//车辆类型
    @BindView(R.id.et_workSite)
    EditText etWorkSite;//施工地点
    @BindView(R.id.et_workPart)
    EditText etWorkPart;//施工部位

    @BindView(R.id.et_inOrderqua)
    EditText etInOrderqua;//内单方量
    @BindView(R.id.et_onOrderqua)
    EditText etOnOrderqua;//外单方量 泵车 如果填写外单方量 那么外单单价必填
    @BindView(R.id.et_onOrderprice)
    EditText etOnOrderprice;//外单单价
    @BindView(R.id.et_onOrderpriceCount)
    EditText etOnOrderpriceCount;//外单金额 泵车  外单方量*外单单价

    @BindView(R.id.et_qilWear)
    EditText etQilWear;//加油升数
    @BindView(R.id.et_wearPrice)
    EditText etWearPrice;//油价
    @BindView(R.id.et_wearCount)
    EditText etWearCount;//加油金额    根据加油升数和油价自动计算（可编辑）
    @BindView(R.id.et_quantityCount)
    EditText etQuantityCount;//合计方量 外单方量+内单方量
    @BindView(R.id.tv_wearUser)
    TextView tvWearUser;//加油负责人
    @BindView(R.id.et_remark)
    EditText etRemark;//备注

    @BindView(R.id.btn_submit)
    Button btnOperator;//操作按钮


    private int tag;//0代表的是录入 1代表的是修改
    private String carTypeID;//车辆类型ID
    private String projectID;//项目部编号
    private String slID;
    private StatisticalList statisticalList;//修改的时候传过来的数据


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bc_input;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            tag = arguments.getInt("TAG");
            statisticalList = arguments.getParcelable("statisticalList");
        }
    }

    @Override
    protected void initView() {
        etInOrderqua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double onOrderqua = Double.parseDouble(etOnOrderqua.getText().toString());
                    double inorderqua = Double.parseDouble(editable.toString());
                    etQuantityCount.setText(String.valueOf(onOrderqua + inorderqua));
                } else {
                    if (StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString())) {
                        etQuantityCount.setText(editable.toString());
                    } else {
                        etQuantityCount.setText(etOnOrderqua.toString());
                    }
                }

            }
        });
        etOnOrderqua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isNullOrEmpty(etInOrderqua.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double inorderqua = Double.parseDouble(etInOrderqua.getText().toString());
                    double onOrderqua = Double.parseDouble(editable.toString());
                    etQuantityCount.setText(String.valueOf(onOrderqua + inorderqua));
                } else {
                    if (StringUtil.isNullOrEmpty(etInOrderqua.getText().toString())) {
                        etQuantityCount.setText(editable.toString());
                    } else {
                        etQuantityCount.setText(etInOrderqua.toString());
                    }
                }
                if (!StringUtil.isNullOrEmpty(etOnOrderprice.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double onOrderPrice = Double.parseDouble(etOnOrderprice.getText().toString());
                    double onOrderqua = Double.parseDouble(editable.toString());
                    etOnOrderpriceCount.setText(String.valueOf(onOrderPrice * onOrderqua));
                } else {
                    etOnOrderpriceCount.setText("");
                }
            }
        });
        etOnOrderprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double onOrderqua = Double.parseDouble(etOnOrderqua.getText().toString());
                    double onOrderPrice = Double.parseDouble(editable.toString());
                    etOnOrderpriceCount.setText(String.valueOf(onOrderPrice * onOrderqua));
                } else {
                    etOnOrderpriceCount.setText("");
                }
            }
        });
        etQilWear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isNullOrEmpty(etWearPrice.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double wearPrice = Double.parseDouble(etWearPrice.getText().toString());
                    double qilWear = Double.parseDouble(editable.toString());
                    etWearCount.setText(String.valueOf((wearPrice * qilWear)));
                } else {
                    etWearCount.setText("");
                }
            }
        });

        etWearPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isNullOrEmpty(etQilWear.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                    double qilWear = Double.parseDouble(etQilWear.getText().toString());
                    double wearPrice = Double.parseDouble(editable.toString());
                    etWearCount.setText(String.valueOf((wearPrice * qilWear)));
                } else {
                    etWearCount.setText("");
                }
            }
        });

    }

    @Override
    protected void initData() {
        if (tag == 0) {
            statisticalList = new StatisticalList();
            btnOperator.setText("提交");
            tvTime.setText(DateUtil.getCurrentDate());
            tvProjectName.setText(SpUtils.getInstance().getString(SpUtils.PROJECT, ""));
            projectID = SpUtils.getInstance().getString(SpUtils.PROJECTID, "");
            tvStaffName.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        } else if (tag == 1) {
            btnOperator.setText("修改");
            slID = statisticalList.getSlID();
            projectID = statisticalList.getProjectID();
            tvTime.setText(statisticalList.getSlDateTime());
            tvStaffName.setText(statisticalList.getStaffName());
            tvProjectName.setText(statisticalList.getProjectName());
            tvCarNumber.setText(statisticalList.getPlateNumber());
            tvCarNumber.setEnabled(false);
            tvCarNumber.setBackgroundColor(getResources().getColor(R.color.color_white));
            tvCarType.setText(statisticalList.getVehicleTypeName());
            etWorkSite.setText(statisticalList.getWorkSite());
            etWorkPart.setText(statisticalList.getWorkPart());
            //TODO
            etInOrderqua.setText(statisticalList.getInOrderqua());
            if (!StringUtil.isNullOrEmpty(statisticalList.getOnOrderqua())) {
                etOnOrderqua.setText(statisticalList.getOnOrderqua());
                etOnOrderprice.setText(statisticalList.getOnOrderprice());
                etOnOrderpriceCount.setText(statisticalList.getOnOrderpriceCount());
            }

            etQilWear.setText(statisticalList.getQilWear());
            etWearPrice.setText(statisticalList.getWearPrice());
            etWearCount.setText(statisticalList.getWearCount());
            etQuantityCount.setText(statisticalList.getQuantityCount());
            tvWearUser.setText(statisticalList.getWearUser());
            etRemark.setText(statisticalList.getRemark());
        }
    }

    @OnClick({R.id.tv_carNumber, R.id.tv_wearUser, R.id.btn_submit})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_carNumber:
                Intent intent = new Intent(mActivity, DeviceListActivity.class);
                intent.putExtra("FLAG", 1);//获取车牌号
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_wearUser:
                Intent intent1 = new Intent(mActivity, StaffListActivity.class);
                intent1.putExtra("TYPE", 1);
                intent1.putExtra("projectID", projectID);
                intent1.putExtra("projectName", tvProjectName.getText().toString());
                startActivityForResult(intent1, 2);//获取加油负责人
                break;
            case R.id.btn_submit:
                hideSoftInput(etInOrderqua);
                if (StringUtil.isNullOrEmpty(tvProjectName.getText().toString())) {
                    UIUtils.showT("项目部不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvStaffName.getText().toString())) {
                    UIUtils.showT("经手人不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvCarNumber.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvCarType.getText().toString())) {
                    UIUtils.showT("车辆类型不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWorkSite.getText().toString())) {
                    UIUtils.showT("施工地不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWorkPart.getText().toString())) {
                    UIUtils.showT("施工部位不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etInOrderqua.getText().toString())) {
                    UIUtils.showT("内单方量不能为空");
                    return;
                }
                if (!StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString()) && StringUtil.isNullOrEmpty(etOnOrderprice.getText().toString())) {
                    UIUtils.showT("外单方量不为空的时候，外单单价也不能为空");//外单方量 泵车 如果填写外单方量 那么外单单价必填
                    return;
                }

                if (!StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString()) && StringUtil.isNullOrEmpty(etOnOrderprice.getText().toString()) && StringUtil.isNullOrEmpty(etOnOrderpriceCount.getText().toString())) {
                    UIUtils.showT("外单金额不能为空");//外单金额
                    return;
                }
                if (StringUtil.isNullOrEmpty(etQilWear.getText().toString())) {
                    UIUtils.showT("加油升数不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWearPrice.getText().toString())) {
                    UIUtils.showT("油价不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWearCount.getText().toString())) {
                    UIUtils.showT("加油金额不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etQuantityCount.getText().toString())) {
                    UIUtils.showT("合计方量不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvWearUser.getText().toString())) {
                    UIUtils.showT("加油负责人不能为空");
                    return;
                }
                if (!StringUtil.isNullOrEmpty(slID)) {
                    statisticalList.setSlID(slID);
                }
                statisticalList.setProjectID(SpUtils.getInstance().getString(SpUtils.PROJECTID, ""));//项目部
                statisticalList.setUserID(SpUtils.getInstance().getString(SpUtils.USERID, ""));//经手人
                statisticalList.setSlDateTime(tvTime.getText().toString());
                statisticalList.setPlateNumber(tvCarNumber.getText().toString());//车牌号
                if (!StringUtil.isNullOrEmpty(carTypeID)) {
                    statisticalList.setCarTypeID(carTypeID);//车辆类型
                }
                statisticalList.setWorkSite(etWorkSite.getText().toString());//施工地点
                statisticalList.setWorkPart(etWorkPart.getText().toString());//施工部位
                statisticalList.setInOrderqua(etInOrderqua.getText().toString());//内单方量
                if (!StringUtil.isNullOrEmpty(etOnOrderqua.getText().toString())) {
                    statisticalList.setOnOrderqua(etOnOrderqua.getText().toString());//外单方量
                    statisticalList.setOnOrderqua(etOnOrderprice.getText().toString());//外单单价
                    statisticalList.setOnOrderpriceCount(etOnOrderpriceCount.getText().toString());//外单金额
                }
                statisticalList.setQilWear(etQilWear.getText().toString());//加油升数
                statisticalList.setWashing(etWearPrice.getText().toString());//油价
                statisticalList.setWearCount(etWearCount.getText().toString());//加油金额
                statisticalList.setQuantityCount(etQuantityCount.getText().toString());//合计方量
                statisticalList.setWearUser(tvWearUser.getText().toString());//加油负责人
                if (!StringUtil.isNullOrEmpty(etRemark.getText().toString())) {//备注
                    statisticalList.setRemark(etRemark.getText().toString());
                }

                MAlertDialog.show(mActivity, "温馨提示", "是否提交数据？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
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
                    tvCarType.setText("");
                    etWorkSite.getText().clear();
                    etWorkPart.getText().clear();
                    etInOrderqua.getText().clear();
                    etOnOrderqua.getText().clear();
                    etOnOrderprice.getText().clear();
                    etOnOrderpriceCount.getText().clear();

                    etQilWear.getText().clear();
                    etWearPrice.getText().clear();
                    etWearCount.getText().clear();
                    etQuantityCount.getText().clear();
                    tvWearUser.setText("");
                    etRemark.getText().clear();

                    if (tag == 0) {
                        Intent intent = new Intent(mActivity, SuccessActivity.class);
                        intent.putExtra("TAG", 1);
                        startActivity(intent);
                    } else if (tag == 1) {
                        mActivity.setResult(2);
                        mActivity.finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            tvCarNumber.setText(data.getStringExtra("carNumber"));
            if (!StringUtil.isNullOrEmpty(data.getStringExtra("carType"))) {
                tvCarType.setText(data.getStringExtra("carType"));
                carTypeID = data.getStringExtra("carTypeID");
            }
        } else if (requestCode == 2 && resultCode == 2) {
            tvWearUser.setText(data.getStringExtra("wearName"));
        }
    }

    public static BCInputFragment newInstance() {
        BCInputFragment fragment = new BCInputFragment();
        return fragment;
    }
}
