package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.home.generalmanager.LifeGoodsPurchaseListFragment;
import com.zzti.lsy.ninetingapp.home.generalmanager.PartsPurchaseListFragment;
import com.zzti.lsy.ninetingapp.home.generalmanager.RepairRecordFragment;
import com.zzti.lsy.ninetingapp.home.production.FormListActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备列表
 */
public class DeviceListActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarMenu)
    TextView tvToolbarMenu;
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bx_ns;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    int flag;

    private void initData() {
        flag = UIUtils.getInt4Intent(this, "FLAG");
        String projectID = UIUtils.getStr4Intent(this, "projectID");
        String projectName = UIUtils.getStr4Intent(this, "projectName");
        String carNumber = UIUtils.getStr4Intent(this, "carNumber");
        List<Fragment> fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putSerializable("FLAG", flag);
        bundle.putSerializable("projectID", projectID);
        bundle.putSerializable("projectName", projectName);
        bundle.putSerializable("carNumber", carNumber);
        DeviceListFragment deviceListFragment = DeviceListFragment.newInstance();
        deviceListFragment.setArguments(bundle);

        fragments.add(deviceListFragment);
        RentCarListFragment rentCarListFragment = RentCarListFragment.newInstance();
        rentCarListFragment.setArguments(bundle);
        fragments.add(rentCarListFragment);

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"自有车", "租用车"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        tvToolbarMenu.setVisibility(View.VISIBLE);
        tvToolbarMenu.setText("表格");
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1) {
                    tvToolbarMenu.setVisibility(View.GONE);
                } else {
                    tvToolbarMenu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @OnClick({R.id.iv_toolbarBack, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
            case R.id.tv_toolbarMenu:
                if (flag == 4) {
                    startActivity(new Intent(this, FormListActivity.class));
                } else {
                    startActivity(new Intent(this, DeviceFormActivity.class));
                }
                break;
        }
    }
}
