package com.zzti.lsy.ninetingapp.home.production;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 统计
 */
public class StatisticDetailActivity extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_statistic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DayFragment());
        fragments.add(new MonthFragment());

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"日", "月"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        int type = UIUtils.getInt4Intent(this, "TYPE");
        if (type == 1) {
            tvTitle.setText("加油升数统计");
        }else if (type == 2) {
            tvTitle.setText("加油升数统计");
        }
    }

    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
