package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.entity.LaobaoPurchased;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsPurcheaseListAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
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
 * @author lsy
 * @create 2018/10/3 21:33
 * @Describe 日用品入库工单列表
 */
public class LifeGoodsPurchaseListActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecyclerView;

    private List<LaobaoPurchased> laobaoPurchaseds;
    private LifeGoodsPurcheaseListAdapter lifeGoodsPurcheaseListAdapter;
    private int pageIndex = 1;//页码
    private String wherestr = "";//查询条件
    private String status = "";
    private PopupWindow popupWindowStatus;
    private ListView lvStatus;
    private ConditionAdapter conditionAdapter;
    private List<ConditionEntity> conditions;

    @Override
    public int getContentViewId() {
        return R.layout.activity_purchese_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initPop();
        initData();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowStatus = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowStatus.setFocusable(true);
        popupWindowStatus.setOutsideTouchable(true);
        //设置消失监听
        popupWindowStatus.setOnDismissListener(this);
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
        conditionAdapter.setTag(1);
        lvStatus.setAdapter(conditionAdapter);
        lvStatus.setOnItemClickListener(this);
        popupWindowStatus.setAnimationStyle(R.style.anim_bottomPop);
    }


    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        laobaoPurchaseds = new ArrayList<>();
        lifeGoodsPurcheaseListAdapter = new LifeGoodsPurcheaseListAdapter(laobaoPurchaseds);
        mRecyclerView.setAdapter(lifeGoodsPurcheaseListAdapter);
        lifeGoodsPurcheaseListAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getPurchaseList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                laobaoPurchaseds.clear();
                etSearch.setText("");
                wherestr = "";
                status = "";
                tvStatus.setText("");
                getPurchaseList();
            }
        });
        showDia();
        getPurchaseList();
    }

    private void initView() {
        setTitle("日用品入库工单");
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//配件管理员
            tvToolbarMenu.setVisibility(View.VISIBLE);
            tvToolbarMenu.setText("采购");
            tvToolbarMenu.setOnClickListener(this);
        } else {
            if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
                tvStatus.setText("待总经理审批");
                status = "1";
            } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
                tvStatus.setText("待项目经理审批");
                status = "2";
            }
            wherestr += " and status=" + status;
        }
    }

    @OnClick({R.id.iv_search, R.id.tv_status})
    public void viewClick(View view) {
        hideSoftInput(etSearch);
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("输入内容不能为空");
                    break;
                }
                wherestr = "";
                pageIndex = 1;
                if (!StringUtil.isNullOrEmpty(status)) {
                    wherestr += " and status=" + status;
                }
                wherestr += " and lbName like \'%" + etSearch.getText().toString() + "%\'";
                showDia();
                laobaoPurchaseds.clear();
                getPurchaseList();
                break;
            case R.id.tv_status:
                setBackgroundAlpha(0.5f);
                popupWindowStatus.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        pageIndex = 1;
        tvStatus.setText(conditions.get(i).getName());
        status = conditions.get(i).getId();
        wherestr += " and status=" + status;
        if (!StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            wherestr += " and lbName like \'%" + etSearch.getText().toString() + "%\'";
        }
        popupWindowStatus.dismiss();
        showDia();
        laobaoPurchaseds.clear();
        getPurchaseList();
    }

    public void getPurchaseList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.APPROVE_GETLAOBAO, params, mRecyclerView, new OkHttpManager.OnResponse<String>() {
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
                                LaobaoPurchased laobaoPurchased = ParseUtils.parseJson(jsonArray.getString(i), LaobaoPurchased.class);
                                laobaoPurchaseds.add(laobaoPurchased);
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
                lifeGoodsPurcheaseListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });

    }


    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private int selectPosition;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        selectPosition = position;
        Intent intent = new Intent(this, LifeGoodsPurchaseDetailActivity.class);
        intent.putExtra("laobaoPurchased", laobaoPurchaseds.get(position));
        startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, LifeGoodsInActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                int status = data.getIntExtra("status", -2);
                laobaoPurchaseds.get(selectPosition).setStatus(String.valueOf(status));
                lifeGoodsPurcheaseListAdapter.notifyItemChanged(selectPosition);
            }
        }
    }
}
