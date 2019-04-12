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
import com.zzti.lsy.ninetingapp.entity.AlarmItemEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.entity.RecordCountEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.AlarmAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeBxAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeByAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeNsAdapter;
import com.zzti.lsy.ninetingapp.home.device.BxNsActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceDetailActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsListActivity;
import com.zzti.lsy.ninetingapp.home.production.ProductStatisticsActivity;
import com.zzti.lsy.ninetingapp.home.repair.RepairRecordActivity;
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
import butterknife.OnClick;

/**
 * 项目经理 operator 2
 */
public class PMManageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycleView_bj)
    RecyclerView mRecycleViewBj;
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_lookMore_ns)
    TextView tvLookMoreNs;
    @BindView(R.id.recycleView_ns)
    RecyclerView mRecycleViewNs;

    @BindView(R.id.tv_lookMore_qbx)
    TextView tvLookMoreQBx;
    @BindView(R.id.recycleView_qbx)
    RecyclerView mRecycleViewQBx;

    @BindView(R.id.tv_lookMore_sbx)
    TextView tvLookMoreSBx;
    @BindView(R.id.recycleView_sbx)
    RecyclerView mRecycleViewSBx;

    @BindView(R.id.recycleView_by)
    RecyclerView mRecycleViewBy;

    private List<NsBxEntity> homeHintEntitiesNs;
    private List<NsBxEntity> homeHintEntitiesQBx;
    private List<NsBxEntity> homeHintEntitiesSBx;
    private List<NsBxEntity> homeHintEntitiesBy;
    private List<AlarmItemEntity> alarmItemEntities;
    private HomeBxAdapter homeQBxAdapter;
    private HomeBxAdapter homeSBxAdapter;
    private HomeNsAdapter homeNsAdapter;
    private HomeByAdapter homeByAdapter;
    private AlarmAdapter alarmAdapter;

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
        homeHintEntitiesQBx = new ArrayList<>();
        homeHintEntitiesSBx = new ArrayList<>();
        homeHintEntitiesNs = new ArrayList<>();
        alarmItemEntities = new ArrayList<>();
        homeHintEntitiesBy = new ArrayList<>();

        LinearLayoutManager linearLayoutManagerBj = new LinearLayoutManager(getContext());
        linearLayoutManagerBj.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewBj.setLayoutManager(linearLayoutManagerBj);
        alarmAdapter = new AlarmAdapter(alarmItemEntities);
        mRecycleViewBj.setAdapter(alarmAdapter);


        LinearLayoutManager linearLayoutManagerNs = new LinearLayoutManager(getContext());
        linearLayoutManagerNs.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewNs.setLayoutManager(linearLayoutManagerNs);
        homeNsAdapter = new HomeNsAdapter(homeHintEntitiesNs);
        mRecycleViewNs.setAdapter(homeNsAdapter);
        homeNsAdapter.setOnItemClickListener(this);


        LinearLayoutManager linearLayoutManagerQBx = new LinearLayoutManager(getContext());
        linearLayoutManagerQBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewQBx.setLayoutManager(linearLayoutManagerQBx);
        homeQBxAdapter = new HomeBxAdapter(homeHintEntitiesQBx);
        mRecycleViewQBx.setAdapter(homeQBxAdapter);
        homeQBxAdapter.setOnItemClickListener(this);


        LinearLayoutManager linearLayoutManagerSBx = new LinearLayoutManager(getContext());
        linearLayoutManagerSBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewSBx.setLayoutManager(linearLayoutManagerSBx);
        homeSBxAdapter = new HomeBxAdapter(homeHintEntitiesSBx);
        mRecycleViewSBx.setAdapter(homeSBxAdapter);
        homeSBxAdapter.setOnItemClickListener(this);

        LinearLayoutManager linearLayoutManagerBy = new LinearLayoutManager(getContext());
        linearLayoutManagerBy.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewBy.setLayoutManager(linearLayoutManagerBy);
        homeByAdapter = new HomeByAdapter(homeHintEntitiesBy);
        mRecycleViewBy.setAdapter(homeByAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCarExpire();
                getProjectRecordCount();
            }
        });
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getCarExpire();
            getProjectRecordCount();
        }
    }

    /**
     * 获取综合油耗报警以及生产量
     */
    private void getProjectRecordCount() {
        alarmItemEntities.clear();
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.RECORD_GETPROJECTRECORDCOUNT, params, smartRefreshLayout, new OkHttpManager.OnResponse<String>() {
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
                    RecordCountEntity recordCountEntity = ParseUtils.parseJson(msgInfo.getData(), RecordCountEntity.class);
                    if (recordCountEntity != null) {
                        tvToday.setText(recordCountEntity.getDayQuantity() + "方");
                        tvMonth.setText(recordCountEntity.getMonthQuantity() + "方");
                        tvYear.setText(recordCountEntity.getYearQuantity() + "方");
                        RecordCountEntity.Alarm alarm = recordCountEntity.getAlarms();
                        if (alarm != null) {
                            int length;
                            String[] splitAlarmItems = alarm.getAlarmitem().split("\\|");
                            String[] splitAllWears = alarm.getAllWear().split("\\|");
                            if (splitAlarmItems.length > splitAllWears.length) {
                                length = splitAllWears.length;
                            } else {
                                length = splitAlarmItems.length;
                            }
                            for (int i = 0; i < length; i++) {
                                AlarmItemEntity alarmItemEntity = new AlarmItemEntity();
                                alarmItemEntity.setTitle(splitAlarmItems[i]);
                                alarmItemEntity.setContent(splitAllWears[i]);
                                alarmItemEntities.add(alarmItemEntity);
                            }
                        }
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                alarmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }


    /**
     * 获取提醒实体类
     */
    private void getCarExpire() {
        homeHintEntitiesQBx.clear();
        homeHintEntitiesSBx.clear();
        homeHintEntitiesNs.clear();
        homeHintEntitiesBy.clear();
        OkHttpManager.postFormBody(Urls.POST_GETCAREXPIRE, null, mRecycleViewQBx, new OkHttpManager.OnResponse<String>() {
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
                                } else if (nsBxEntity.getTypeName().equals("强险")) {
                                    homeHintEntitiesQBx.add(nsBxEntity);
                                } else if (nsBxEntity.getTypeName().equals("商业险")) {
                                    homeHintEntitiesSBx.add(nsBxEntity);
                                } else if (nsBxEntity.getTypeName().equals("保养")) {
                                    homeHintEntitiesBy.add(nsBxEntity);
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
                            if (homeHintEntitiesQBx.size() == 0) {
                                tvLookMoreQBx.setText("暂无数据");
                                tvLookMoreQBx.setEnabled(false);
                            } else {
                                tvLookMoreQBx.setText("查看更多");
                                tvLookMoreQBx.setEnabled(true);
                                if (homeHintEntitiesQBx.size() > 5) {
                                    homeHintEntitiesQBx.subList(0, 4);
                                }
                            }
                            if (homeHintEntitiesSBx.size() == 0) {
                                tvLookMoreSBx.setText("暂无数据");
                                tvLookMoreSBx.setEnabled(false);
                            } else {
                                tvLookMoreSBx.setText("查看更多");
                                tvLookMoreSBx.setEnabled(true);
                                if (homeHintEntitiesSBx.size() > 5) {
                                    homeHintEntitiesSBx.subList(0, 4);
                                }
                            }
                        } else {
                            tvLookMoreNs.setVisibility(View.GONE);
                            tvLookMoreQBx.setVisibility(View.GONE);
                            tvLookMoreSBx.setVisibility(View.GONE);
                            UIUtils.showT("暂无保险、年审、保养数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                homeQBxAdapter.notifyDataSetChanged();
                homeSBxAdapter.notifyDataSetChanged();
                homeNsAdapter.notifyDataSetChanged();
                homeByAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.rl_menu7, R.id.tv_lookMore_ns, R.id.tv_lookMore_qbx})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1://授权
                startActivity(new Intent(mActivity, AuthorizationActivity.class));
                break;
//            case R.id.rl_menu2://款项
//                startActivity(new Intent(mActivity, PactListActivity.class));
//                break;
            case R.id.rl_menu3://配件列表
                startActivity(new Intent(mActivity, PartsListActivity.class));
                break;
            case R.id.rl_menu4://设备列表
                startActivity(new Intent(mActivity, DeviceListActivity.class));
                break;
            case R.id.rl_menu5://维修列表
                startActivity(new Intent(mActivity, RepairRecordActivity.class));
                break;
            case R.id.rl_menu6://维修统计
                startActivity(new Intent(mActivity, MaintenanceStatisticActivity.class));
                break;
            case R.id.rl_menu7://生产统计
                startActivity(new Intent(mActivity, ProductStatisticsActivity.class));
                break;
            case R.id.tv_lookMore_ns:
                Intent intent1 = new Intent(mActivity, BxNsActivity.class);
                intent1.putExtra("TAG", 1);
                startActivity(intent1);
                break;
            case R.id.tv_lookMore_qbx:
                Intent intent2 = new Intent(mActivity, BxNsActivity.class);
                intent2.putExtra("TAG", 0);
                startActivity(intent2);
                break;

        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DeviceDetailActivity.class);
        if (adapter == homeQBxAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesQBx.get(position).getPlateNumber());
        }
        if (adapter == homeSBxAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesSBx.get(position).getPlateNumber());
        } else if (adapter == homeNsAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesNs.get(position).getPlateNumber());
        }
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
