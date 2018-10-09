package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsAdapter;
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
 * 日用品列表
 */
public class LifeGoodsListActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<LaoBao> lifeGoodEntities;
    private LifeGoodsAdapter lifeGoodsAdapter;
    private int pageIndex = 1;//页码
    private String wherestr;//查询条件
//    private int tag;//1代表日用品列表 2代表采购  2代表出库进来

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        lifeGoodEntities = new ArrayList<>();
        lifeGoodsAdapter = new LifeGoodsAdapter(lifeGoodEntities);
        mRecycleView.setAdapter(lifeGoodsAdapter);
        lifeGoodsAdapter.setOnItemClickListener(this);
//        tag = UIUtils.getInt4Intent(this, "TAG");
//        lifeGoodsAdapter.setTag(tag);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getLaoBaoList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                lifeGoodEntities.clear();
                etSearch.setText("");
                wherestr = "";
                getLaoBaoList();
            }
        });
        showDia();
        getLaoBaoList();
    }


    /**
     * 获取日用品列表
     */
    private void getLaoBaoList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (StringUtil.isNullOrEmpty(wherestr)) {
            params.put("wherestr", "");
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.PARTS_GETLAOBAO, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                LaoBao laoBao = ParseUtils.parseJson(jsonArray.getString(i), LaoBao.class);
                                lifeGoodEntities.add(laoBao);
                            }
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                lifeGoodsAdapter.notifyDataSetChanged();
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
        setTitle("日用品");
        etSearch.setHint("请输入日用品名称");
        tvToolbarMenu.setVisibility(View.VISIBLE);
        tvToolbarMenu.setText("出库记录");
        tvToolbarMenu.setOnClickListener(this);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, LifeGoodsOutRecordActivity.class);
        intent.putExtra("lbID", "");
        startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, LifeGoodsDetailActivity.class);
        intent.putExtra("LaoBao", lifeGoodEntities.get(position));
        startActivity(intent);
    }

    @OnClick({R.id.iv_search})
    public void viewClick() {
        hideSoftInput(etSearch);
        if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            UIUtils.showT("请输入搜索内容");
            return;
        }
        wherestr = "lbName like \'%" + etSearch.getText().toString() + "%\'";
        pageIndex = 1;
        lifeGoodEntities.clear();
        showDia();
        getLaoBaoList();
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
