package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.home.adapter.HomeBxAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeNsAdapter;
import com.zzti.lsy.ninetingapp.home.device.BxNsActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceDetailActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.home.machinery.MaintenanceRecordActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsListActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目经理 operator 4
 */
public class PMManageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
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

        if (UIUtils.isNetworkConnected()) {
            showDia();
//            getCarExpire();
        }
    }

    public static Fragment newInstance() {
        PMManageFragment fragment = new PMManageFragment();
        return fragment;
    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.tv_lookMore_ns, R.id.tv_lookMore_bx})
    public void viewClick(View view) {//设备列表
        switch (view.getId()) {
            case R.id.rl_menu1:
                startActivity(new Intent(mActivity, DeviceListActivity.class));
                break;
            case R.id.rl_menu2://配件列表
                startActivity(new Intent(mActivity, PartsListActivity.class));
                break;
            case R.id.rl_menu3://采购审批
                UIUtils.showT("采购审批");
                break;
            case R.id.rl_menu4://维修审批
                startActivity(new Intent(mActivity, MaintenanceRecordActivity.class));
                break;
            case R.id.rl_menu5://生产统计
                UIUtils.showT("生产统计");
                break;
            case R.id.rl_menu6://维修统计
                startActivity(new Intent(mActivity,MaintenanceStatistic.class));
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
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
