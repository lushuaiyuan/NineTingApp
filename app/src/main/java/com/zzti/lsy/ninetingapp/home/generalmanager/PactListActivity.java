package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PactInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.PactListAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
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
public class PactListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PactInfo> pactInfos;
    private PactListAdapter pactListAdapter;
    private int pageIndex = 1;
    private String wherestr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pact_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
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
                pactInfos.clear();
                getPactList();
            }
        });
        //TODO
//        showDia();
//        getPactList();
    }

    private void getPactList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (StringUtil.isNullOrEmpty(wherestr)) {
            params.put("wherestr", "");
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

        int tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 1) {//总经理
            tvToolbarMenu.setVisibility(View.VISIBLE);
            tvToolbarMenu.setText("添加");
        } else if (tag == 2) {//項目经理
            tvToolbarMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @OnClick(R.id.tv_toolbarMenu)
    public void viewClick() {
        Intent intent = new Intent(this, PactInputActivity.class);
        startActivity(intent);
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
}
