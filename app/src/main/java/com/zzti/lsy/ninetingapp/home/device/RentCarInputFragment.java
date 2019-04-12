package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RentCarEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

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
 * @author lsy
 * @create 2018/12/2 21:08
 * @Describe 设备录入（外租车）
 */
public class RentCarInputFragment extends BaseFragment implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.et_drivingNumber)
    EditText etDrivingNumber;//行驶证号码
    @BindView(R.id.et_carOwner)
    EditText etCarOwner;//车主
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;//租用起始日期
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;//租用结束日期

    //车辆类型pop
    private PopupWindow popupWindowCarType;
    private ListView mListViewCarType;
    private CarTypeAdapter carTypeAdapter;
    private List<CarTypeEntity> carTypeEntities;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_outside_device_input;
    }

    @Override
    protected void initView() {
        initCarTypePop();
    }

    private void initCarTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarType = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarType.setFocusable(true);
        popupWindowCarType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowCarType.setOnDismissListener(this);
        popupWindowCarType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCarType.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewCarType = contentview.findViewById(R.id.pop_list);
        carTypeEntities = new ArrayList<>();
        carTypeAdapter = new CarTypeAdapter(carTypeEntities);
        mListViewCarType.setAdapter(carTypeAdapter);
        mListViewCarType.setOnItemClickListener(this);
        popupWindowCarType.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.btn_submit, R.id.tv_carType, R.id.tv_startTime, R.id.tv_endTime})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_carType:
                carTypeEntities.clear();
                showDia();
                getCarType();
                break;
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
     * 获取车辆类型
     */
    private void getCarType() {
        OkHttpManager.postFormBody(Urls.POST_GETCARTYPE, null, tvCarType, new OkHttpManager.OnResponse<String>() {
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
                            CarTypeEntity carTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), CarTypeEntity.class);
                            carTypeEntities.add(carTypeEntity);
                        }
                        if (carTypeEntities.size() > 0) {
                            //设置背景色
                            setBackgroundAlpha(0.5f);
                            if (carTypeEntities.size() >= 5) {
                                //动态设置listView的高度
                                View listItem = carTypeAdapter.getView(0, null, mListViewCarType);
                                listItem.measure(0, 0);
                                int totalHei = (listItem.getMeasuredHeight() + mListViewCarType.getDividerHeight()) * 5;
                                mListViewCarType.getLayoutParams().height = totalHei;
                                ViewGroup.LayoutParams params = mListViewCarType.getLayoutParams();
                                params.height = totalHei;
                                mListViewCarType.setLayoutParams(params);
                            }
                            popupWindowCarType.showAtLocation(tvCarType, Gravity.BOTTOM, 0, 0);
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
     * 提交数据
     */
    private void submitData() {
        String projectID = SpUtils.getInstance().getString(SpUtils.PROJECTID, "");
        RentCarEntity rentCarEntity = new RentCarEntity();
        rentCarEntity.setPlateNumber(etCarNumber.getText().toString());
        rentCarEntity.setVehicleTypeName(tvCarType.getText().toString());
        rentCarEntity.setVehicleTypeID(carTypeID);
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

    private String carTypeID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        tvCarType.setText(carTypeEntities.get(position).getVehicleTypeName());
        carTypeID = carTypeEntities.get(position).getVehicleTypeID();
        popupWindowCarType.dismiss();
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
    }
}
