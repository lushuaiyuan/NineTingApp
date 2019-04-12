package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 保险年审
 */
public class BxNsActivity extends BaseActivity {
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
        fragments.add(new QBxFragment());
        fragments.add(new SBxFragment());
        fragments.add(new NsFragment());

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"强险", "商业险", "年审"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        int tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 0) {
            mTabLayout.getTabAt(0).select();
        } else if (tag == 1) {
            mTabLayout.getTabAt(1).select();
        } else if (tag == 2) {
            mTabLayout.getTabAt(2).select();
        }

    }

    private void initView() {

    }


    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
