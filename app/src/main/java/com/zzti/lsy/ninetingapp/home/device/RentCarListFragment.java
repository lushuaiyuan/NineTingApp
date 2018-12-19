package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RentCarEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.RentCarAdapter;
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
 * @author lsy
 * @create 2018/12/9 12:35
 * @Describe 租用车的Fragment
 */
public class RentCarListFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etCarNumber;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private int pageIndex = 1;
    private String wherestr = "";
    private int flag = -1; //-1默认进入到详情界面 1代表获取车牌号 2代表进入单车统计界面 3代表选中 4代表进入单车生产统计
    private List<RentCarEntity> rentCarEntities;
    private RentCarAdapter rentCarAdapter;

    public static RentCarListFragment newInstance() {
        RentCarListFragment fragment = new RentCarListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_platenumber;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            flag = arguments.getInt("FLAG");
        }
    }


    @Override
    protected void initView() {
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        //使上拉加载具有弹性效果：
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    protected void initData() {
        rentCarEntities = new ArrayList<>();
        rentCarAdapter = new RentCarAdapter(rentCarEntities);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.setAdapter(rentCarAdapter);
        if (flag == 1) {
            rentCarAdapter.setOnItemClickListener(this);
        }
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getRentCarList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                wherestr = "";
                etCarNumber.setText("");
                rentCarEntities.clear();
                getRentCarList();
            }
        });
        showDia();
        getRentCarList();
    }

    /**
     * 获取外租车的数据列表
     */
    private void getRentCarList() {
        wherestr = "";
        if (!StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
            wherestr += " and plateNumber like \'" + etCarNumber.getText().toString() + "%\'";
        }
        HashMap<String, String> params = new HashMap<>();
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        params.put("pageIndex", String.valueOf(pageIndex));
        OkHttpManager.postFormBody(Urls.POST_GETRENTCARLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                            RentCarEntity rentCarEntity = ParseUtils.parseJson(jsonArray.getString(i), RentCarEntity.class);
                            rentCarEntities.add(rentCarEntity);
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
                rentCarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    @OnClick({R.id.iv_search})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    break;
                }
                pageIndex = 1;
                showDia();
                rentCarEntities.clear();
                getRentCarList();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("carNumber", rentCarEntities.get(position).getPlateNumber());
        mActivity.setResult(2, intent);
        mActivity.finish();
    }
}
