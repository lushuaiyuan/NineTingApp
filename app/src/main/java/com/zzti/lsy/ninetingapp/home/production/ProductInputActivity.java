package com.zzti.lsy.ninetingapp.home.production;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.home.pm.ProductionFragment;
import com.zzti.lsy.ninetingapp.home.pm.RepertoryListFragment;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2019/4/4 14:02
 * @Describe 生产录入
 */
public class ProductInputActivity extends BaseActivity {
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
        int tag = UIUtils.getInt4Intent(this, "TAG");//0代表的是录入 1代表的是修改
        StatisticalList statisticalList = getIntent().getParcelableExtra("StatisticalList");

        if (tag == 0) {
            setTitle("生产录入");
        } else {
            setTitle("生产修改");
        }
        List<Fragment> fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putInt("TAG", tag);
        if (statisticalList != null) {
            bundle.putParcelable("statisticalList", statisticalList);
        }
        GCInputFragment gcInputFragment = GCInputFragment.newInstance();
        gcInputFragment.setArguments(bundle);
        fragments.add(gcInputFragment);

        BCInputFragment bcInputFragment = BCInputFragment.newInstance();
        bcInputFragment.setArguments(bundle);
        fragments.add(bcInputFragment);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"罐车", "泵车"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (statisticalList != null && statisticalList.getVehicleTypeName().contains("罐车")) {
            mTabLayout.getTabAt(0).select();
        } else if(statisticalList != null && statisticalList.getVehicleTypeName().contains("泵车")){
            mTabLayout.getTabAt(1).select();
        }
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
