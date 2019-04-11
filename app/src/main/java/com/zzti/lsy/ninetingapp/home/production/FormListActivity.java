package com.zzti.lsy.ninetingapp.home.production;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/7 15:59
 * 生产表格
 */
public class FormListActivity extends BaseActivity {
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
        List<Fragment> fragments = new ArrayList<>();
//        Bundle bundle = new Bundle();
        GCFormFragment gcFormFragment = GCFormFragment.newInstance();
//        gcInputFragment.setArguments(bundle);
        fragments.add(gcFormFragment);

        BCFormFragment bcFormFragment = BCFormFragment.newInstance();
//        bcInputFragment.setArguments(bundle);
        fragments.add(bcFormFragment);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"罐车", "泵车"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
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
