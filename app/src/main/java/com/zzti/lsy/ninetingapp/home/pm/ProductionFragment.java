package com.zzti.lsy.ninetingapp.home.pm;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.entity.UpdateApply;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.AuthorizationAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2019/4/3 19:25
 * @Describe 劳保审批列表
 */
public class ProductionFragment extends BaseFragment implements AdapterView.OnItemClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rl_project)
    RelativeLayout rlProject;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    private PopupWindow popupWindowStatus;
    private ListView lvStatus;
    private ConditionAdapter conditionAdapter;
    private List<ConditionEntity> conditions;

    //项目部
    private PopupWindow popupWindowProject;
    private ListView mListViewProject;
    private ConditionAdapter projectAdapter;
    private List<ConditionEntity> projectEntities;

    private int pageIndex = 1;
    private String wherestr = "";
    private int type;
    private String projectID;
    private String status;

    private List<UpdateApply> updateApplies;
    private AuthorizationAdapter authorizationAdapter;

    public static ProductionFragment newInstance() {
        ProductionFragment fragment = new ProductionFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parts_approve;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        myIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && getActivity() != null && updateApplies.size() == 0) {
            if (UIUtils.isNetworkConnected()) {
                showDia();
                myIsVisibleToUser = false;
                getUpdateApplies();
            }
        }
    }


    private boolean myIsVisibleToUser;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(myIsVisibleToUser);
    }


    @Override
    protected void initView() {
        initStatusPop();
        initProjectPop();
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        //使上拉加载具有弹性效果：
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
    }

    private void initProjectPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, UIUtils.getWidth(mActivity) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
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

    private void initStatusPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowStatus = new PopupWindow(contentview, UIUtils.getWidth(mActivity) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowStatus.setFocusable(true);
        popupWindowStatus.setOutsideTouchable(true);
        popupWindowStatus.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowStatus.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvStatus = contentview.findViewById(R.id.pop_list);
        conditions = new ArrayList<>();
        ConditionEntity conditionEntity0 = new ConditionEntity();
        conditionEntity0.setName("已审核");
        conditionEntity0.setId("0");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("待审核");
        conditionEntity1.setId("1");

        conditions.add(conditionEntity0);
        conditions.add(conditionEntity1);
        conditionAdapter = new ConditionAdapter(conditions);
        lvStatus.setAdapter(conditionAdapter);
        lvStatus.setOnItemClickListener(this);
        popupWindowStatus.setAnimationStyle(R.style.anim_upPop);
    }

    @Override
    protected void initData() {
        updateApplies = new ArrayList<>();
        authorizationAdapter = new AuthorizationAdapter(updateApplies);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.setAdapter(authorizationAdapter);
        authorizationAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getUpdateApplies();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                wherestr = "";
                tvStatus.setText("处理状态");
                tvProject.setText("项目部");
                updateApplies.clear();
                getUpdateApplies();
            }
        });
        showDia();
        getProject();
    }

    /**
     * 获取列表
     */
    private void getUpdateApplies() {
        HashMap<String, String> params = new HashMap<>();
        if (!tvStatus.getText().toString().equals("处理状态")) {
            wherestr += " and status=" + status;
        }
        if (!tvProject.getText().toString().equals("项目部")) {
            wherestr += " and projectID=" + projectID;
        }
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        params.put("pageIndex", String.valueOf(pageIndex));

        OkHttpManager.postFormBody(Urls.APPROVE_GETUPDATENUMBER, params, tvProject, new OkHttpManager.OnResponse<String>() {
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            UpdateApply updateApply = ParseUtils.parseJson(jsonArray.getString(i), UpdateApply.class);
                            if (updateApply.getType().equals("1")) {
                                updateApplies.add(updateApply);
                            }
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
                if (updateApplies.size() == 0) {
                    UIUtils.showT(C.Constant.NODATA);
                }
                authorizationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }


    @OnClick({R.id.rl_project, R.id.rl_status})
    public void viewClick(View view) {
        switch (view.getId()) {
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
            case R.id.rl_status:
                type = 2;
                popupWindowStatus.showAsDropDown(rlStatus, 0, 0, Gravity.LEFT);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        pageIndex = 1;
        if (type == 1) {
            tvProject.setText(projectEntities.get(i).getName());
            projectID = projectEntities.get(i).getId();
            popupWindowProject.dismiss();
        } else if (type == 2) {
            tvStatus.setText(conditions.get(i).getName());
            status = conditions.get(i).getId();
            popupWindowStatus.dismiss();
        }
        showDia();
        updateApplies.clear();
        getUpdateApplies();
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


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //TODO 进入详情

    }
}
