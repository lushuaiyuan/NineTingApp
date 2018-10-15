package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PactInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.PactListAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAdapter;
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
 * author：anxin on 2018/10/12 19:07
 * 合同列表
 */
public class PactListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_project)
    RelativeLayout rlProject;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.rl_pactType)
    RelativeLayout rlPactType;
    @BindView(R.id.tv_pactType)
    TextView tvPactType;
    @BindView(R.id.rl_pactSchedule)
    RelativeLayout rlPactSchedule;
    @BindView(R.id.tv_pactSchedule)
    TextView tvPactSchedule;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PactInfo> pactInfos;
    private PactListAdapter pactListAdapter;

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ConditionAdapter projectAdapter;
    private List<ConditionEntity> projectEntities;

    //合同类型pop
    private PopupWindow popupWindowPactType;
    private ListView mListViewPactType;
    private ConditionAdapter pactTypeAdapter;
    private List<ConditionEntity> pactTypes;

    //合同进度pop
    private PopupWindow popupWindowPactSchedule;
    private ListView mListViewPactSchedule;
    private ConditionAdapter pactSchedulesAdapter;
    private List<ConditionEntity> pactSchedules;

    private int pageIndex = 1;
    private String wherestr = "";

    @Override
    public int getContentViewId() {
        return R.layout.activity_pact_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initPactTypePop();
        initPactSchedule();
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理角色
            initProjectPop();
        }
        initData();
    }

    private void initProjectPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, UIUtils.getWidth(this) / 3 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
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
        projectAdapter = new ConditionAdapter(projectEntities);
        mListViewProject.setAdapter(projectAdapter);
        mListViewProject.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_upPop);
    }

    private void initPactTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        if (rlProject.getVisibility() == View.VISIBLE) {
            popupWindowPactType = new PopupWindow(contentview, UIUtils.getWidth(this) / 3 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindowPactType = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindowPactType.setFocusable(true);
        popupWindowPactType.setOutsideTouchable(true);
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
        mListViewPactType = contentview.findViewById(R.id.pop_list);
        pactTypes = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("项目合同");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("外单合同");
        pactTypes.add(conditionEntity0);
        pactTypes.add(conditionEntity1);
        pactTypeAdapter = new ConditionAdapter(pactTypes);
        pactTypeAdapter.setTag(0);
        mListViewPactType.setAdapter(pactTypeAdapter);
        mListViewPactType.setOnItemClickListener(this);
        popupWindowPactType.setAnimationStyle(R.style.anim_upPop);
    }

    private void initPactSchedule() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        if (rlProject.getVisibility() == View.VISIBLE) {
            popupWindowPactSchedule = new PopupWindow(contentview, UIUtils.getWidth(this) / 3 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindowPactSchedule = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindowPactSchedule.setFocusable(true);
        popupWindowPactSchedule.setOutsideTouchable(true);
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
        mListViewPactSchedule = contentview.findViewById(R.id.pop_list);
        pactSchedules = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("未执行");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("执行中");
        ConditionEntity conditionEntity2 = new ConditionEntity();
        conditionEntity2.setName("已结束");

        pactSchedules.add(conditionEntity0);
        pactSchedules.add(conditionEntity1);
        pactSchedules.add(conditionEntity2);
        pactSchedulesAdapter = new ConditionAdapter(pactSchedules);
        mListViewPactSchedule.setAdapter(pactSchedulesAdapter);
        pactSchedulesAdapter.setTag(0);
        mListViewPactSchedule.setOnItemClickListener(this);
        popupWindowPactSchedule.setAnimationStyle(R.style.anim_upPop);
    }


    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        pactInfos = new ArrayList<>();
        pactListAdapter = new PactListAdapter(pactInfos);
        mRecycleView.setAdapter(pactListAdapter);
        pactListAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getPactList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                wherestr = "";
                projectID = "";
                pactSchedule = "";
                pactType = "";
                etSearch.setText("");
                tvProject.setText("项目部");
                tvPactSchedule.setText("合同类型");
                tvPactType.setText("合同进度");
                pactInfos.clear();
                getPactList();
            }
        });
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理角色
            showDia();
            getProject();
        }
        showDia();
        getPactList();
    }

    /**
     * 获取项目部
     */
    private void getProject() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, params, tvProject, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    projectEntities.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProjectEntity projectEntity = ParseUtils.parseJson(jsonArray.getString(i), ProjectEntity.class);
                            ConditionEntity conditionEntity = new ConditionEntity();
                            conditionEntity.setId(projectEntity.getProjectID());
                            conditionEntity.setName(projectEntity.getProjectName());
                            projectEntities.add(conditionEntity);
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
     * 获取合同列表
     */
    private void getPactList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.ADMIN_GETPACTLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(mSmartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PactInfo pactInfo = ParseUtils.parseJson(jsonArray.getString(i), PactInfo.class);
                                pactInfos.add(pactInfo);
                            }
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                pactListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    private void initView() {
        setTitle("合同列表");
        mSmartRefreshLayout.setEnableLoadMore(true);
        mSmartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        mSmartRefreshLayout.setEnableAutoLoadMore(false);

        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理才显示项目部的查询条件
            rlProject.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            tvToolbarMenu.setVisibility(View.VISIBLE);
            tvToolbarMenu.setText("添加");
        } else {
            rlProject.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            tvToolbarMenu.setVisibility(View.GONE);
        }
    }

    private int selcetPosition;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        selcetPosition = position;
        Intent intent = new Intent(this, PactInputActivity.class);
        intent.putExtra("TAG", 1);//修改
        intent.putExtra("PACTINFO", pactInfos.get(position));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            double addMoney = Double.parseDouble(data.getStringExtra("addMoney"));
            double pactInMoney = Double.parseDouble(pactInfos.get(selcetPosition).getPactInMoney());
            double pactOutMoney = Double.parseDouble(pactInfos.get(selcetPosition).getPactOutMoney());

            pactInfos.get(selcetPosition).setPactInMoney(String.valueOf(pactInMoney + addMoney));
            pactInfos.get(selcetPosition).setPactOutMoney(String.valueOf(pactOutMoney - addMoney));
            pactListAdapter.notifyItemChanged(selcetPosition);
        }
    }

    private int type;
    private String pactType;
    private String pactSchedule;
    private String projectID;

    @OnClick({R.id.iv_search, R.id.rl_project, R.id.rl_pactType, R.id.rl_pactSchedule, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        hideSoftInput(etSearch);
        switch (view.getId()) {
            case R.id.tv_toolbarMenu:
                Intent intent = new Intent(this, PactInputActivity.class);
                intent.putExtra("TAG", 0);//录入
                startActivity(intent);
                break;
            case R.id.rl_project:
                type = 1;
                if (projectEntities.size() > 0) {
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
                    popupWindowProject.showAsDropDown(rlProject, 0, 0, Gravity.LEFT);
                } else {
                    UIUtils.showT("暂无数据");
                }
                break;
            case R.id.rl_pactType:
                type = 2;
                popupWindowPactType.showAsDropDown(rlPactType, 0, 0, Gravity.LEFT);
                break;
            case R.id.rl_pactSchedule:
                type = 3;
                popupWindowPactSchedule.showAsDropDown(rlPactSchedule, 0, 0, Gravity.LEFT);
                break;
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("请输入合同编号");
                    break;
                }
                wherestr = "";
                if (!StringUtil.isNullOrEmpty(pactType)) {
                    wherestr += " and pactType=\'" + pactType + "\'";
                }
                if (!StringUtil.isNullOrEmpty(pactSchedule)) {
                    wherestr += " and pactSchedule=\'" + pactSchedule + "\'";
                }
                if (!StringUtil.isNullOrEmpty(projectID)) {
                    wherestr += " and projectID=\'" + projectID + "\'";
                }
                wherestr += " and pactID like \'%" + etSearch.getText().toString() + "%\'";
                showDia();
                pactInfos.clear();
                getPactList();
                break;
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        if (type == 1) {
            projectID = projectEntities.get(i).getId();
            tvProject.setText(projectEntities.get(i).getName());
            popupWindowProject.dismiss();
            wherestr += " and projectID=\'" + projectID + "\'";
            if (!StringUtil.isNullOrEmpty(pactType)) {
                wherestr += " and pactType=\'" + pactType + "\'";
            }
            if (!StringUtil.isNullOrEmpty(pactSchedule)) {
                wherestr += " and pactSchedule=\'" + pactSchedule + "\'";
            }
        } else if (type == 2) {
            pactType = pactTypes.get(i).getName();
            tvPactType.setText(pactType);
            popupWindowPactType.dismiss();
            wherestr += " and pactType=\'" + pactType + "\'";
            if (!StringUtil.isNullOrEmpty(projectID)) {
                wherestr += " and projectID=\'" + projectID + "\'";
            }
            if (!StringUtil.isNullOrEmpty(pactSchedule)) {
                wherestr += " and pactSchedule=\'" + pactSchedule + "\'";
            }
        } else if (type == 3) {
            pactSchedule = pactSchedules.get(i).getName();
            tvPactSchedule.setText(pactSchedule);
            popupWindowPactSchedule.dismiss();
            wherestr += " and pactSchedule=\'" + pactSchedule + "\'";
            if (!StringUtil.isNullOrEmpty(projectID)) {
                wherestr += " and projectID=\'" + projectID + "\'";
            }
            if (!StringUtil.isNullOrEmpty(pactType)) {
                wherestr += " and pactType=\'" + pactType + "\'";
            }
        }
        if (!StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            wherestr += " and pactID like \'%" + etSearch.getText().toString() + "%\'";
        }
        showDia();
        pactInfos.clear();
        getPactList();
    }
}
