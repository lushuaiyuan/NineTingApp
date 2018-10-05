package com.zzti.lsy.ninetingapp.home.parts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.LaobaoDelivery;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.LifeGoodsOutRecordAdapter;
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

/**
 * 日用品出库记录
 */
public class LifeGoodsOutRecordActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<LaobaoDelivery> laobaoDeliveries;
    private LifeGoodsOutRecordAdapter lifeGoodsOutRecordAdapter;
    private String lbID;
    private int pageIndex = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_outrecord;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }


    private void initData() {
        lbID = UIUtils.getStr4Intent(this, "lbID");
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        laobaoDeliveries = new ArrayList<>();
        lifeGoodsOutRecordAdapter = new LifeGoodsOutRecordAdapter(laobaoDeliveries);
        mRecycleView.setAdapter(lifeGoodsOutRecordAdapter);
        showDia();
        getRecord();
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getRecord();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                laobaoDeliveries.clear();
                getRecord();
            }
        });
    }


    /**
     * 获取出库记录
     */
    private void getRecord() {
        HashMap<String, String> params = new HashMap<>();
        if (!StringUtil.isNullOrEmpty(lbID)) {
            params.put("wherestr", "lbID=" + lbID);
        }else{
            params.put("wherestr", "");
        }
        params.put("pageIndex", String.valueOf(pageIndex));
        OkHttpManager.postFormBody(Urls.PARTS_GETLAOBAOOUT, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                                LaobaoDelivery laobaoDelivery = ParseUtils.parseJson(jsonArray.getString(i), LaobaoDelivery.class);
                                laobaoDeliveries.add(laobaoDelivery);
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
                lifeGoodsOutRecordAdapter.notifyDataSetChanged();

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
        setTitle("表格查看");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }
}
