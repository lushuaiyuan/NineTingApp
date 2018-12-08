package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceInputActivity extends BaseActivity {
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();

    }

    private void initData() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new InsideDeviceInputFragment());
        fragments.add(new RecentCarInputFragment());
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"自有录入", "外租录入"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {

    }


    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
