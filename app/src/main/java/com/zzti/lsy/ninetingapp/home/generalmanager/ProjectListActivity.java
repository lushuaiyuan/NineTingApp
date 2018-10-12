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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectDetailAdapter;
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
 * author：anxin on 2018/10/12 10:41
 * 项目列表
 */
public class ProjectListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecyclerView;
    List<ProjectEntity> projectEntities;
    private ProjectDetailAdapter projectDetailAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectEntities = new ArrayList<>();
        projectDetailAdapter = new ProjectDetailAdapter(projectEntities);
        mRecyclerView.setAdapter(projectDetailAdapter);
        projectDetailAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getProjectDetailList();
            }
        });
        showDia();
        getProjectDetailList();
    }

    private void getProjectDetailList() {
        projectEntities.clear();
        OkHttpManager.postFormBody(Urls.POST_GETPROJECT, null, mRecyclerView, new OkHttpManager.OnResponse<String>() {
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
                            ProjectEntity projectEntity = ParseUtils.parseJson(jsonArray.getString(i), ProjectEntity.class);
                            projectEntities.add(projectEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                projectDetailAdapter.notifyDataSetChanged();
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
        setTitle("项目列表");
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("projectEntity", projectEntities.get(position));
        startActivity(intent);
    }
}
