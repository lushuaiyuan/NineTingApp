package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.PartsListAdapter;
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
 * author：anxin on 2018/8/8 16:18
 * 配件列表
 */
public class PartsListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<PartsInfoEntity> partsInfoEntities;
    private PartsListAdapter partsListAdapter;
    private int pageIndex = 1;//页码
    private String wherestr;//查询条件
    private int tag;//1代表维修申请进来（获取配件名称）  2代表点击配件列表菜单进来  3配件入库  4配件出库

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }


    private void initView() {
        setTitle("配件列表");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
        tag = UIUtils.getInt4Intent(this, "TAG");
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        partsInfoEntities = new ArrayList<>();
        partsListAdapter = new PartsListAdapter(partsInfoEntities);
        partsListAdapter.setTag(tag);
        mRecycleView.setAdapter(partsListAdapter);
        partsListAdapter.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getPartsList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                etSearch.setText("");
                wherestr = "";
                partsInfoEntities.clear();
                getPartsList();
            }
        });
        showDia();
        getPartsList();
    }

    private void getPartsList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (StringUtil.isNullOrEmpty(wherestr)) {
            params.put("wherestr", "");
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.POST_GETPARTS, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                                PartsInfoEntity partsInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), PartsInfoEntity.class);
                                partsInfoEntities.add(partsInfoEntity);
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
                partsListAdapter.notifyDataSetChanged();
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
        if (tag == 1) {//维修申请进来
            String partsID = partsInfoEntities.get(position).getPartsID();
            String partsName = partsInfoEntities.get(position).getPartsName();
            String partsModel = partsInfoEntities.get(position).getPartsModel();
            String partsNumber = partsInfoEntities.get(position).getPartsNumber();
            Intent intent = new Intent();
            intent.putExtra("partsID", partsID);
            intent.putExtra("partsName", partsModel + "——" + partsName);
            intent.putExtra("partsNumber", partsNumber);
            setResult(2, intent);
            finish();
        } else if (tag == 3) {//配件入库
            Intent intent = new Intent(this, PartsInputActivity.class);
            intent.putExtra("TAG", 2);
            intent.putExtra("PartsInfo", partsInfoEntities.get(position));
            startActivity(intent);
        } else if (tag == 4) {//配件出库
            //TODO
            Intent intent = new Intent(this, PartsOutDetailActivity.class);
            intent.putExtra("TAG", 2);
            startActivity(intent);
        }

    }

    @OnClick({R.id.iv_search})
    public void viewClick() {
        hideSoftInput(etSearch);
        if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            UIUtils.showT("请输入搜索内容");
            return;
        }
        wherestr = "partsName like \'%" + etSearch.getText().toString() + "%\' or partsModel like \'%" + etSearch.getText().toString() + "%\'";
        pageIndex = 1;
        partsInfoEntities.clear();
        showDia();
        getPartsList();
    }

}
