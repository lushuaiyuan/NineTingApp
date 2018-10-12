package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PactInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.entity.RepairTypeEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RepairTypeAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/10/12 20:06
 * 合同录入界面
 */
public class PactInputActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.et_pactID)
    EditText etPactID;
    @BindView(R.id.et_pactContent)
    EditText etPactContent;
    @BindView(R.id.tv_pactType)
    TextView tvPactType;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_pactSchedule)
    TextView tvPactSchedule;
    @BindView(R.id.et_pactMoney)
    EditText etPactMoney;
    @BindView(R.id.et_pactRealMoney)
    EditText etPactRealMoney;
    @BindView(R.id.et_pactInMoney)
    EditText etPactInMoney;
    @BindView(R.id.et_pactOutMoney)
    EditText etPactOutMoney;
    @BindView(R.id.et_pactTime)
    EditText etPactTime;


    private PopupWindow popupWindowPactType;
    private ListView lvPactType;
    private ConditionAdapter conditionAdapterPactType;
    private List<ConditionEntity> conditionsPactTypes;

    private PopupWindow popupWindowPactSchedule;
    private ListView lvPactSchedule;
    private ConditionAdapter conditionAdapterPactSchedule;
    private List<ConditionEntity> conditionsPactSchedules;

    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ProjectAdapter projectAdapter;
    private List<ProjectEntity> projectEntities;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pact_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        setTitle("录入合同");
        initPopPactType();
        initPopPactSchedule();
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

    private void initPopPactSchedule() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowPactSchedule = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPactSchedule.setFocusable(true);
        popupWindowPactSchedule.setOutsideTouchable(true);
        //设置消失监听
        popupWindowPactSchedule.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowPactSchedule.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvPactSchedule = contentview.findViewById(R.id.pop_list);
        conditionsPactSchedules = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("未执行");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("执行中");
        ConditionEntity conditionEntity2 = new ConditionEntity();
        conditionEntity2.setName("已结束");

        conditionsPactSchedules.add(conditionEntity0);
        conditionsPactSchedules.add(conditionEntity1);
        conditionsPactSchedules.add(conditionEntity2);

        conditionAdapterPactSchedule = new ConditionAdapter(conditionsPactSchedules);
        conditionAdapterPactSchedule.setTag(1);//背景色为黑色
        lvPactSchedule.setAdapter(conditionAdapterPactSchedule);
        lvPactSchedule.setOnItemClickListener(this);
        popupWindowPactSchedule.setAnimationStyle(R.style.anim_upPop);
    }


    private void initPopPactType() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowPactType = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPactType.setFocusable(true);
        popupWindowPactType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowPactType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowPactType.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvPactType = contentview.findViewById(R.id.pop_list);
        conditionsPactTypes = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("项目合同");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("外单合同");

        conditionsPactTypes.add(conditionEntity0);
        conditionsPactTypes.add(conditionEntity1);

        conditionAdapterPactType = new ConditionAdapter(conditionsPactTypes);
        conditionAdapterPactType.setTag(1);
        lvPactType.setAdapter(conditionAdapterPactType);
        lvPactType.setOnItemClickListener(this);
        popupWindowPactType.setAnimationStyle(R.style.anim_upPop);
    }

    private void initData() {
        showDia();
        getProject();
    }

    private void getProject() {
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, null, tvPactSchedule, new OkHttpManager.OnResponse<String>() {
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
                            if (!projectEntity.getProjectID().equals(spUtils.getString(SpUtils.PROJECTID, ""))) {
                                projectEntities.add(projectEntity);
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

    private int condition = 1;

    @OnClick({R.id.tv_pactType, R.id.tv_project, R.id.tv_pactSchedule, R.id.btn_inputPact})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pactType://合同类型
                condition = 1;
                setBackgroundAlpha(0.5f);
                popupWindowPactType.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_project:
                condition = 3;
                setBackgroundAlpha(0.5f);
                popupWindowProject.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_pactSchedule://合同进度
                condition = 2;
                setBackgroundAlpha(0.5f);
                popupWindowPactSchedule.showAtLocation(etPactContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_inputPact://录入合同
                if (StringUtil.isNullOrEmpty(etPactID.getText().toString())) {
                    UIUtils.showT("合同编号不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(tvPactType.getText().toString())) {
                    UIUtils.showT("合同类型不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(tvProject.getText().toString())) {
                    UIUtils.showT("项目部不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(tvPactSchedule.getText().toString())) {
                    UIUtils.showT("合同进度不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPactContent.getText().toString())) {
                    UIUtils.showT("合同简介不能为空");
                    break;
                }

                if (StringUtil.isNullOrEmpty(etPactMoney.getText().toString())) {
                    UIUtils.showT("合同总金额不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPactRealMoney.getText().toString())) {
                    UIUtils.showT("合同应收金额不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPactInMoney.getText().toString())) {
                    UIUtils.showT("合同已收金额不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPactOutMoney.getText().toString())) {
                    UIUtils.showT("合同未收金额不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPactTime.getText().toString())) {
                    UIUtils.showT("合同周期不能为空");
                    break;
                }
                PactInfo pactInfo = new PactInfo();
                pactInfo.setPactID(etPactID.getText().toString());
                pactInfo.setPactContent(etPactContent.getText().toString());
                pactInfo.setPactMoney(etPactMoney.getText().toString());
                pactInfo.setPactInMoney(etPactInMoney.getText().toString());
                pactInfo.setPactOutMoney(etPactOutMoney.getText().toString());
                pactInfo.setPactTime(etPactTime.getText().toString());
                pactInfo.setPactRealMoney(etPactRealMoney.getText().toString());
                pactInfo.setPactSchedule(tvPactSchedule.getText().toString());
                pactInfo.setPactType(tvPactType.getText().toString());
                pactInfo.setProjectID(projectID);
                showDia();
                submitPact(pactInfo);
                break;
        }
    }

    /**
     * 录入合同
     *
     * @param pactInfo
     */
    private void submitPact(PactInfo pactInfo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pactJson", new Gson().toJson(pactInfo));
        OkHttpManager.postFormBody(Urls.ADMIN_ADDPACT, params, tvPactSchedule, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    Intent intent = new Intent(PactInputActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 9);
                    startActivity(intent);
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
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

    private String projectID;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (condition == 1) {//合同类型
            tvPactType.setText(conditionsPactTypes.get(i).getName());
            popupWindowPactType.dismiss();
        } else if (condition == 2) {//合同进度
            tvPactSchedule.setText(conditionsPactSchedules.get(i).getName());
            popupWindowPactSchedule.dismiss();
        } else if (condition == 3) {//项目部
            tvProject.setText(projectEntities.get(i).getProjectName());
            projectID = projectEntities.get(i).getProjectID();
            popupWindowProject.dismiss();
        }
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}
