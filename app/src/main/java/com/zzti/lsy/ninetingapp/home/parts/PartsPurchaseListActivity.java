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
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsPurchased;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.PartsPurcheaseListAdapter;
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
 * @create 2018/10/4 13:17
 * @Describe 配件入库工单列表
 */
public class PartsPurchaseListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecyclerView;

    private List<PartsPurchased> partsPurchaseds;
    private PartsPurcheaseListAdapter partsPurcheaseListAdapter;

    private PopupWindow popupWindowStatus;
    private ListView lvStatus;
    private ConditionAdapter conditionAdapter;
    private List<ConditionEntity> conditions;

    private int pageIndex = 1;//页码
    private String wherestr = "";//查询条件

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
        conditionEntity1.setName("项目经理已审批");
        conditionEntity1.setId("1");
        ConditionEntity conditionEntity2 = new ConditionEntity();
        conditionEntity2.setName("待审批");
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
        partsPurchaseds = new ArrayList<>();
        partsPurcheaseListAdapter = new PartsPurcheaseListAdapter(partsPurchaseds);
        mRecyclerView.setAdapter(partsPurcheaseListAdapter);
        partsPurcheaseListAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getPurchaseList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                hideSoftInput(etSearch);
                pageIndex = 1;
                partsPurchaseds.clear();
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
        setTitle("配件入库工单");
        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {
            tvToolbarMenu.setVisibility(View.VISIBLE);
            tvToolbarMenu.setText("采购");
            tvToolbarMenu.setOnClickListener(this);
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
                if (!StringUtil.isNullOrEmpty(status)) {
                    wherestr += " and status=" + status;
                }
                wherestr += " and partsName like \'%" + etSearch.getText().toString() + "%\' or partsModel like \'%" + etSearch.getText().toString() + "%\'";
                showDia();
                partsPurchaseds.clear();
                getPurchaseList();
                break;
            case R.id.tv_status:
                setBackgroundAlpha(0.5f);
                popupWindowStatus.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent1 = new Intent(this, PartsInputActivity.class);
        intent1.putExtra("TAG", 1);//代表采购
        startActivity(intent1);
    }

    private String status;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        tvStatus.setText(conditions.get(i).getName());
        status = conditions.get(i).getId();
        wherestr += " and status=" + status;
        if (!StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            wherestr += " and partsName like \'%" + etSearch.getText().toString() + "%\' or partsModel like \'%" + etSearch.getText().toString() + "%\'";
        }
        popupWindowStatus.dismiss();
        showDia();
        partsPurchaseds.clear();
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
        OkHttpManager.postFormBody(Urls.APPROVE_GETPARTSIN, params, mRecyclerView, new OkHttpManager.OnResponse<String>() {
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
                                PartsPurchased partsPurchased = ParseUtils.parseJson(jsonArray.getString(i), PartsPurchased.class);
                                partsPurchaseds.add(partsPurchased);
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
                partsPurcheaseListAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this, PartsPurchaseDetailActivity.class);
        intent.putExtra("partsPurchased", partsPurchaseds.get(position));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                int status = data.getIntExtra("status", -2);
                String date = data.getStringExtra("date");
                partsPurchaseds.get(selectPosition).setStatus(String.valueOf(status));
                partsPurchaseds.get(selectPosition).setPurchasedDate(date);
                partsPurcheaseListAdapter.notifyItemChanged(selectPosition);
            }
        }
    }
}
