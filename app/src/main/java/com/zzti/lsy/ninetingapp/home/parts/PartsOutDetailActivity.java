package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsDelivery;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAddressAdapter;
import com.zzti.lsy.ninetingapp.entity.ProjectAddressEntitiy;
import com.zzti.lsy.ninetingapp.home.device.DeviceInputActivity;
import com.zzti.lsy.ninetingapp.network.Constant;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
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

import static com.bumptech.glide.request.transition.NoTransition.getFactory;

/**
 * 出库详情
 */
public class PartsOutDetailActivity extends BaseActivity implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_partsName)
    TextView tvPartsName;
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_outAddress)
    TextView tvOutAddress;
    @BindView(R.id.et_reason)
    TextView etReason;

    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    private String partsID;
    private String projectID;
    private int partsNumber;//库存数量

    @Override
    public int getContentViewId() {
        return R.layout.activity_pratsout_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }


    private void initData() {
        partsID = UIUtils.getStr4Intent(this, "partsID");
        String partsName = UIUtils.getStr4Intent(this, "partsName");
        String partsModel = UIUtils.getStr4Intent(this, "partsModel");
        partsNumber = Integer.parseInt(UIUtils.getStr4Intent(this, "partsNumber"));
        tvPartsName.setText(partsModel + "—" + partsName);
        showDia();
        getProject();
    }

    /**
     * 获取项目部
     */
    private void getProject() {
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, null, tvOutAddress, new OkHttpManager.OnResponse<String>() {
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
                            ProjectEntity projectEntity = ParseUtils.parseJson(jsonArray.getString(i), ProjectEntity.class);
                            projectEntities.add(projectEntity);
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

    private void initView() {
        setTitle("配件出库");
        initPop();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
        //设置消失监听
        popupWindowProject.setOnDismissListener(this);
        popupWindowProject.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowProject.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewProject = contentview.findViewById(R.id.pop_list);
        projectEntities = new ArrayList<>();
        projectAdapter = new ProjectAdapter(projectEntities);
        mListViewProject.setAdapter(projectAdapter);
        mListViewProject.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.tv_outAddress, R.id.tv_outTime, R.id.btn_out})
    public void viewClick(View view) {
        hideSoftInput(etAmount);
        switch (view.getId()) {
            case R.id.tv_outAddress:
                if (projectEntities.size() > 0) {
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (projectEntities.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = projectAdapter.getView(0, null, mListViewProject);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListViewProject.getDividerHeight()) * 5;
                        mListViewProject.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListViewProject.getLayoutParams();
                        params.height = totalHei;
                        mListViewProject.setLayoutParams(params);
                    }
                    setBackgroundAlpha(0.5f);
                    popupWindowProject.showAtLocation(tvOutAddress, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(C.Constant.NODATA);
                }
                break;
            case R.id.tv_outTime://出库时间
                showCustomTime();
                break;
            case R.id.btn_out://出库按钮
                if (StringUtil.isNullOrEmpty(tvPartsName.getText().toString())) {
                    UIUtils.showT("出库配件不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvOutTime.getText().toString())) {
                    UIUtils.showT("出库时间不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                    UIUtils.showT("出库数量不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvOutAddress.getText().toString())) {
                    UIUtils.showT("出库目的地不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etReason.getText().toString())) {
                    UIUtils.showT("出库原因不能为空");
                    return;
                }
                if (Integer.parseInt(etAmount.getText().toString()) > partsNumber) {
                    etAmount.setText("");
                    etAmount.setFocusable(true);
                    etAmount.setFocusableInTouchMode(true);
                    etAmount.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    UIUtils.showT("出库数量不能大于库存数量");
                    return;
                }
                PartsDelivery partsDelivery = new PartsDelivery();
                partsDelivery.setPartsID(partsID);
                partsDelivery.setPdCause(etReason.getText().toString());
                partsDelivery.setPdDate(tvOutTime.getText().toString());
                partsDelivery.setPdNumber(etAmount.getText().toString());
                partsDelivery.setProjectID(projectID);
                outParts(partsDelivery);
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
        TimePickerView pvTime = new TimePickerBuilder(PartsOutDetailActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvOutTime.setText(DateUtil.getDate(date));
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();
    }

    //出库
    private void outParts(PartsDelivery partsDelivery) {
        HashMap<String, String> params = new HashMap<>();
        params.put("partOutJosn", new Gson().toJson(partsDelivery));
        OkHttpManager.postFormBody(Urls.POST_PARTSOUT, params, tvOutAddress, new OkHttpManager.OnResponse<String>() {
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
                    Intent intent = new Intent(PartsOutDetailActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 4);
                    startActivity(intent);
                }
                UIUtils.showT(msgInfo.getMsg());
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });


    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvOutAddress.setText(projectEntities.get(i).getProjectName());
        projectID = projectEntities.get(i).getProjectID();
        popupWindowProject.dismiss();
    }

}
