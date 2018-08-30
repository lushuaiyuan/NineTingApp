package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsInFragment;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsOutFragment;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zzti.lsy.ninetingapp.utils.UIUtils.dip2px;

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
        return R.layout.activity_lifegood_out_in;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();

    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BxFragment());
        fragments.add(new NsFragment());

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"保险", "年审"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        int tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 0) {
            mTabLayout.getTabAt(0).select();
        } else if (tag == 1) {
            mTabLayout.getTabAt(1).select();
        }

    }

    private void initView() {

    }


    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}
