package com.zzti.lsy.ninetingapp.home.repair;

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
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairTypeEntity;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.home.adapter.RepairRecordAdapter;
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
 * 维修记录
 */
public class RepairRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_handleState)
    RelativeLayout rlHandleState;
    @BindView(R.id.rl_repairType)
    RelativeLayout rlRepairType;
    @BindView(R.id.tv_handleState)
    TextView tvHandleState;
    @BindView(R.id.tv_repairType)
    TextView tvRepairType;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<RepairinfoEntity> repairinfoEntities;
    private RepairRecordAdapter repairRecordAdapter;


    private PopupWindow popupWindowStatus;
    private ListView lvStatus;
    private ConditionAdapter conditionAdapter;
    private List<ConditionEntity> conditions;

    private PopupWindow popupWindowRepairType;
    private ListView lvRepairType;
    private RepairTypeAdapter repairTypeAdapter;
    private List<RepairTypeEntity> repairTypeEntities;

    private int condition = 1;
    private String repairTypeID;
    private String statusID;


    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_record;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private int pageIndex = 1;
    private String wherestr = "";

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        repairinfoEntities = new ArrayList<>();
        repairRecordAdapter = new RepairRecordAdapter(repairinfoEntities);
        mRecycleView.setAdapter(repairRecordAdapter);
        repairRecordAdapter.setOnItemClickListener(this);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getRecord();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                wherestr = "";
                statusID = "";
                repairTypeID = "";
                etSearch.setText("");
                tvHandleState.setText("处理状态");
                tvRepairType.setText("维修类型");
                repairinfoEntities.clear();
                getRecord();
            }
        });
        showDia();
        getRepairType();
        getRecord();
    }

    /**
     * 获取维修类型
     */
    private void getRepairType() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRTYPE, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                            RepairTypeEntity repairTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), RepairTypeEntity.class);
                            repairTypeEntities.add(repairTypeEntity);
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

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getRecord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(smartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RepairinfoEntity repairinfoEntity = ParseUtils.parseJson(jsonArray.getString(i), RepairinfoEntity.class);
                            repairinfoEntities.add(repairinfoEntity);
                        }
                        if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                repairRecordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }


    private void initView() {
        setTitle("维修记录");
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadMore(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
        initPopStatus();
        initPopRepairType();

        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
            tvHandleState.setText("待总经理审批");
            statusID = "1";
        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
            tvHandleState.setText("待项目经理审批");
            statusID = "2";
        }
        wherestr += " and status=" + statusID;
    }




    @OnClick({R.id.iv_search, R.id.rl_handleState, R.id.rl_repairType})
    public void viewClick(View view) {
        hideSoftInput(etSearch);
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("请输入车牌号");
                    break;
                }
                if (!UIUtils.validateCarNum(etSearch.getText().toString())) {
                    UIUtils.showT("车牌号格式不正确");
                    break;
                }
                pageIndex = 1;
                wherestr += " and plateNumber=" + "\"" + etSearch.getText().toString() + "\"";
                showDia();
                repairinfoEntities.clear();
                getRecord();
                break;
            case R.id.rl_handleState://处理状态
                condition = 1;
                popupWindowStatus.showAsDropDown(rlHandleState, 0, 0, Gravity.LEFT);
                break;
            case R.id.rl_repairType://维修类型
                condition = 2;
                if (repairTypeEntities.size() > 0) {

                    if (repairTypeEntities.size() >= 4) {
                        //动态设置listView的高度
                        View listItem = repairTypeAdapter.getView(0, null, lvRepairType);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + lvRepairType.getDividerHeight()) * 4;
                        lvRepairType.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = lvRepairType.getLayoutParams();
                        params.height = totalHei;
                        lvRepairType.setLayoutParams(params);
                    }
                    popupWindowRepairType.showAsDropDown(rlRepairType, 0, 0, Gravity.LEFT);
                } else {
                    UIUtils.showT("暂无数据");
                }
                break;
        }
    }

    private void initPopRepairType() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowRepairType = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowRepairType.setFocusable(true);
        popupWindowRepairType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowRepairType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowRepairType.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvRepairType = contentview.findViewById(R.id.pop_list);
        repairTypeEntities = new ArrayList<>();
        repairTypeAdapter = new RepairTypeAdapter(repairTypeEntities);
        repairTypeAdapter.setTag(2);//背景色为黑色
        lvRepairType.setAdapter(repairTypeAdapter);
        lvRepairType.setOnItemClickListener(this);
        popupWindowRepairType.setAnimationStyle(R.style.anim_upPop);
    }

    private void initPopStatus() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowStatus = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowStatus.setFocusable(true);
        popupWindowStatus.setOutsideTouchable(true);
        //设置消失监听
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
        conditionEntity0.setName("总经理已审批");
        conditionEntity0.setId("0");
        ConditionEntity conditionEntity1 = new ConditionEntity();
        conditionEntity1.setName("待总经理审批");
        conditionEntity1.setId("1");
        ConditionEntity conditionEntity2 = new ConditionEntity();
        conditionEntity2.setName("待项目经理审批");
        conditionEntity2.setId("2");
        ConditionEntity conditionEntity3 = new ConditionEntity();
        conditionEntity3.setName("已撤销");
        conditionEntity3.setId("3");
        ConditionEntity conditionEntity4 = new ConditionEntity();
        conditionEntity4.setName("已拒绝");
        conditionEntity4.setId("-1");

        conditions.add(conditionEntity0);
        conditions.add(conditionEntity1);
        conditions.add(conditionEntity2);
        conditions.add(conditionEntity3);
        conditions.add(conditionEntity4);

        conditionAdapter = new ConditionAdapter(conditions);
        conditionAdapter.setTag(0);
        lvStatus.setAdapter(conditionAdapter);
        lvStatus.setOnItemClickListener(this);
        popupWindowStatus.setAnimationStyle(R.style.anim_upPop);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        pageIndex = 1;
        if (condition == 1) {//处理状态
            tvHandleState.setText(conditions.get(i).getName());
            statusID = conditions.get(i).getId();
            wherestr += " and status=" + statusID;
            if (!StringUtil.isNullOrEmpty(repairTypeID)) {
                wherestr += " and repairTypeID=" + repairTypeID;
            }
            popupWindowStatus.dismiss();
            showDia();
            repairinfoEntities.clear();
            getRecord();
        } else if (condition == 2) {//维修类型
            tvRepairType.setText(repairTypeEntities.get(i).getTypeName());
            repairTypeID = repairTypeEntities.get(i).getTypeID();
            wherestr += " and repairTypeID=" + repairTypeID;
            if (!StringUtil.isNullOrEmpty(statusID)) {
                wherestr += " and status=" + statusID;
            }
            popupWindowRepairType.dismiss();
            showDia();
            repairinfoEntities.clear();
            getRecord();
        }
    }

    private int selectPosition;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, RepairRecordDetailActivity.class);
        intent.putExtra("RepairinfoEntity", repairinfoEntities.get(position));
        selectPosition = position;
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                int status = data.getIntExtra("status", -2);
                repairinfoEntities.get(selectPosition).setStatus(String.valueOf(status));
                repairRecordAdapter.notifyItemChanged(selectPosition);
            }
        }
    }
}
