package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.BXAdapter;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 强险
 */
public class QBxFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<NsBxEntity> homeHintEntities;
    private BXAdapter bxAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        myIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && getActivity() != null && homeHintEntities.size() == 0) {
            if (UIUtils.isNetworkConnected()) {
                showDia();
                myIsVisibleToUser = false;
                getCarExpire();
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
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadMore(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        homeHintEntities = new ArrayList<>();
        bxAdapter = new BXAdapter(homeHintEntities);
        mRecycleView.setAdapter(bxAdapter);
        bxAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                getCarExpire();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }
        });
    }

    /**
     * 获取提醒实体类
     */
    private void getCarExpire() {
        homeHintEntities.clear();
        OkHttpManager.postFormBody(Urls.POST_GETCAREXPIRE, null, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                                NsBxEntity nsBxEntity = ParseUtils.parseJson(jsonArray.getString(i), NsBxEntity.class);
                                if (nsBxEntity.getTypeName().equals("强险")) {
                                    homeHintEntities.add(nsBxEntity);
                                }
                            }
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                bxAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DeviceDetailActivity.class);
        intent.putExtra("carNumber", homeHintEntities.get(position).getPlateNumber());
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
