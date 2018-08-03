package com.zzti.lsy.ninetingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zzti.lsy.ninetingapp.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.radio_group_button)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_home)
    RadioButton mRadioButtonHome;
    private Fragment[] mFragments;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mFragments = DataGenerator.getFragments();
        initView();
    }

    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            Fragment mFragment = null;

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_home:
                        mFragment = mFragments[0];
                        break;
                    case R.id.radio_button_message:
                        mFragment = mFragments[1];
                        break;
                    case R.id.radio_button_mine:
                        mFragment = mFragments[2];
                        break;
                }
                if (mFragments != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, mFragment).commit();
                }
            }
        });
        // 保证第一次会回调OnCheckedChangeListener
        mRadioButtonHome.setChecked(true);
    }


}
