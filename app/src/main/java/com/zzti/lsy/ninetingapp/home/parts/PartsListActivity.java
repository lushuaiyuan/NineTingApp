package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.PartsListAdapter;
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
 * author：anxin on 2018/8/8 16:18
 * 配件列表
 */
public class PartsListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.radio_group_condition)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_all)
    RadioButton mRadioButtonAll;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PartsInfoEntity> partsInfoEntities;
    private PartsListAdapter partsListAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private int tag;//1代表维修申请进来（获取配件名称）  2代表点击配件列表菜单进来

    private void initView() {
        setTitle("配件列表");
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
        tag = UIUtils.getInt4Intent(this, "TAG");
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_all: //全部

                        break;
                    case R.id.radio_button_inWareHouse://在库

                        break;
                    case R.id.radio_button_maintain://维修

                        break;
                    case R.id.radio_button_onRoad://在途

                        break;
                }
            }
        });
        // 保证第一次会回调OnCheckedChangeListener
        mRadioButtonAll.setChecked(true);
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        partsInfoEntities = new ArrayList<>();
        partsListAdapter = new PartsListAdapter(partsInfoEntities);
        mRecycleView.setAdapter(partsListAdapter);
        partsListAdapter.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getPartsList();
            }
        });

        showDia();
        getPartsList();
    }

    private void getPartsList() {
        partsInfoEntities.clear();
        OkHttpManager.postFormBody(Urls.POST_GETPARTS, null, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PartsInfoEntity partsInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), PartsInfoEntity.class);
                                partsInfoEntities.add(partsInfoEntity);
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
                partsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (tag == 1) {//维修申请进来
            String partsID = partsInfoEntities.get(position).getPartsID();
            String partsName = partsInfoEntities.get(position).getPartsName();
            Intent intent = new Intent();
            intent.putExtra("partsID", partsID);
            intent.putExtra("partsName", partsName);
            setResult(2, intent);
            finish();
        } else if (tag == 2) {//配件列表菜单进入

        }
    }
}
