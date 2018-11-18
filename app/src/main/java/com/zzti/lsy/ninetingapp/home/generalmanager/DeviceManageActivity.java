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
import com.zzti.lsy.ninetingapp.entity.DeviceManageEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RecycleViewItemData;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.DeviceManageAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author lsy
 * @create 2018/11/15 21:19
 * @Describe 设备管理
 */
public class DeviceManageActivity extends BaseActivity  {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    private List<RecycleViewItemData> dataList;
    private DeviceManageAdapter deviceManageAdapter;


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
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        deviceManageAdapter = new DeviceManageAdapter(dataList);
        mRecycleView.setAdapter(deviceManageAdapter);
        DeviceManageAdapter.OnItemClickListener onItemClickListener = new DeviceManageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                Intent intent = new Intent(DeviceManageActivity.this, DeviceListActivity.class);
                DeviceManageEntity deviceManageEntity = (DeviceManageEntity) dataList.get(position).getT();
                intent.putExtra("projectID", deviceManageEntity.getProjectID());
                intent.putExtra("projectName", deviceManageEntity.getProjectName());
                startActivity(intent);
            }

        };
        deviceManageAdapter.setOnItemClickListener(onItemClickListener);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dataList.clear();
                getDeviceManage();
            }
        });
        showDia();
        getDeviceManage();
    }

    /**
     * 获取设备管理数据
     */
    private void getDeviceManage() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.ADMIN_GETCARCOUNT, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                            DeviceManageEntity deviceManageEntity = ParseUtils.parseJson(jsonArray.getString(i), DeviceManageEntity.class);
                            dataList.add(new RecycleViewItemData(new DeviceManageEntity(deviceManageEntity.getProjectName(),deviceManageEntity.getProjectID()), 1));
                            for (int j = 0; j < deviceManageEntity.getDeviceDetials().size(); j++) {
                                dataList.add(new RecycleViewItemData(deviceManageEntity.getDeviceDetials().get(j), 2));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                deviceManageAdapter.notifyDataSetChanged();
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
        setTitle("设备管理");
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

}
