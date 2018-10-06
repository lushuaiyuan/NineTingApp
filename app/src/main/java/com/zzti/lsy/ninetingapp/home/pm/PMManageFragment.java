package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.HomeBxAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeNsAdapter;
import com.zzti.lsy.ninetingapp.home.device.BxNsActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceDetailActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsPurchaseListActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsPurchaseListActivity;
import com.zzti.lsy.ninetingapp.home.repair.RepairRecordActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目经理 operator 4
 */
public class PMManageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_lookMore_ns)
    TextView tvLookMoreNs;
    @BindView(R.id.tv_lookMore_bx)
    TextView tvLookMoreBx;
    @BindView(R.id.recycleView_ns)
    RecyclerView mRecycleViewNs;
    @BindView(R.id.recycleView_bx)
    RecyclerView mRecycleViewBx;
    private List<NsBxEntity> homeHintEntitiesNs;
    private List<NsBxEntity> homeHintEntitiesBx;
    private HomeBxAdapter homeBxAdapter;
    private HomeNsAdapter homeNsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pm_manage;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
    }

    @Override
    protected void initData() {
        homeHintEntitiesBx = new ArrayList<>();
        homeHintEntitiesNs = new ArrayList<>();

        LinearLayoutManager linearLayoutManagerNs = new LinearLayoutManager(getContext());
        linearLayoutManagerNs.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewNs.setLayoutManager(linearLayoutManagerNs);
        homeNsAdapter = new HomeNsAdapter(homeHintEntitiesNs);
        mRecycleViewNs.setAdapter(homeNsAdapter);
        homeNsAdapter.setOnItemClickListener(this);


        LinearLayoutManager linearLayoutManagerBx = new LinearLayoutManager(getContext());
        linearLayoutManagerBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewBx.setLayoutManager(linearLayoutManagerBx);
        homeBxAdapter = new HomeBxAdapter(homeHintEntitiesBx);
        mRecycleViewBx.setAdapter(homeBxAdapter);
        homeBxAdapter.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCarExpire();
            }
        });
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getCarExpire();
        }
    }

    /**
     * 获取提醒实体类
     */
    private void getCarExpire() {
        homeHintEntitiesBx.clear();
        homeHintEntitiesNs.clear();
        OkHttpManager.postFormBody(Urls.POST_GETCAREXPIRE, null, mRecycleViewBx, new OkHttpManager.OnResponse<String>() {
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
                                    homeHintEntitiesNs.add(nsBxEntity);
                                } else if (nsBxEntity.getTypeName().equals("保险")) {
                                    homeHintEntitiesBx.add(nsBxEntity);
                                }
                            }
                            if (homeHintEntitiesNs.size() == 0) {
                                tvLookMoreNs.setText("暂无数据");
                                tvLookMoreNs.setEnabled(false);
                            } else {
                                tvLookMoreNs.setText("查看更多");
                                tvLookMoreNs.setEnabled(true);
                                if (homeHintEntitiesNs.size() > 5) {
                                    homeHintEntitiesNs.subList(0, 4);
                                }
                            }
                            if (homeHintEntitiesBx.size() == 0) {
                                tvLookMoreBx.setText("暂无数据");
                                tvLookMoreBx.setEnabled(false);
                            } else {
                                tvLookMoreBx.setText("查看更多");
                                tvLookMoreBx.setEnabled(true);
                                if (homeHintEntitiesBx.size() > 5) {
                                    homeHintEntitiesBx.subList(0, 4);
                                }
                            }
                        } else {
                            tvLookMoreNs.setVisibility(View.GONE);
                            tvLookMoreBx.setVisibility(View.GONE);
                            UIUtils.showT("暂无保险年审数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                homeBxAdapter.notifyDataSetChanged();
                homeNsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }

    public static Fragment newInstance() {
        PMManageFragment fragment = new PMManageFragment();
        return fragment;
    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.rl_menu7, R.id.tv_lookMore_ns, R.id.tv_lookMore_bx})
    public void viewClick(View view) {//设备列表
        switch (view.getId()) {
            case R.id.rl_menu1:
                startActivity(new Intent(mActivity, DeviceListActivity.class));
                break;
            case R.id.rl_menu2://配件列表
                startActivity(new Intent(mActivity, PartsListActivity.class));
                break;
            case R.id.rl_menu3://配件审批
                startActivity(new Intent(mActivity, PartsPurchaseListActivity.class));
                break;
            case R.id.rl_menu4://日用品审批
                startActivity(new Intent(mActivity, LifeGoodsPurchaseListActivity.class));
                break;
            case R.id.rl_menu5://维修审批
                startActivity(new Intent(mActivity, RepairRecordActivity.class));

                break;
            case R.id.rl_menu6://生产统计
                UIUtils.showT("生产统计");
                break;
            case R.id.rl_menu7://维修统计
                startActivity(new Intent(mActivity, MaintenanceStatistic.class));
                break;
            case R.id.tv_lookMore_ns:
                Intent intent1 = new Intent(mActivity, BxNsActivity.class);
                intent1.putExtra("TAG", 1);
                startActivity(intent1);
                break;
            case R.id.tv_lookMore_bx:
                Intent intent2 = new Intent(mActivity, BxNsActivity.class);
                intent2.putExtra("TAG", 0);
                startActivity(intent2);
                break;

        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DeviceDetailActivity.class);
        if (adapter == homeBxAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesBx.get(position).getPlateNumber());
        } else if (adapter == homeNsAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesNs.get(position).getPlateNumber());
        }
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
