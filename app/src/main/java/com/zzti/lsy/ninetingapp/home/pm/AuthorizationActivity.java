package com.zzti.lsy.ninetingapp.home.pm;

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
import com.zzti.lsy.ninetingapp.home.device.DeviceFormActivity;
import com.zzti.lsy.ninetingapp.home.production.FormListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2019/3/26 10:38
 * @Describe
 */
public class AuthorizationActivity extends BaseActivity {
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

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        RepertoryListFragment repertoryListFragment = RepertoryListFragment.newInstance();
        fragments.add(repertoryListFragment);

        ProductionFragment productionFragment = ProductionFragment.newInstance();
        fragments.add(productionFragment);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"库存", "生产"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
    }

    @OnClick({R.id.iv_toolbarBack})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
        }
    }
}
