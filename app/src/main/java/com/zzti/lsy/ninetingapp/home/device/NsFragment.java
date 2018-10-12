package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.home.adapter.NSAdapter;
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
 * 年审
 */
public class NsFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<NsBxEntity> homeHintEntities;
    private NSAdapter nsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView() {
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadMore(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        homeHintEntities = new ArrayList<>();
        nsAdapter = new NSAdapter(homeHintEntities);
        mRecycleView.setAdapter(nsAdapter);
        nsAdapter.setOnItemClickListener(this);

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
        showDia();
        getCarExpire();
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
                                if (nsBxEntity.getTypeName().equals("年审")) {
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
                nsAdapter.notifyDataSetChanged();
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
