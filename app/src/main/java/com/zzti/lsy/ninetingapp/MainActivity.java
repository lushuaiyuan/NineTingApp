package com.zzti.lsy.ninetingapp;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ActivityStack;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.HashMap;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.radio_group_button)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_home)
    RadioButton mRadioButtonHome;
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

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
                    //1机械师理  2项目经理 3配件管理员 4设备管理员 5统计员 6机械师理
                    case R.id.radio_button_home:
                        if (spUtils.getInt(SpUtils.OPTYPE, -1) == 5) { //统计员
                            mFragment = mFragments[0];
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {//配件管理员
                            mFragment = mFragments[1];
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 4) {//设备管理员
                            mFragment = mFragments[2];
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {//项目经理
                            mFragment = mFragments[3];
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {//总经理
                            mFragment = mFragments[4];
                        } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 1) {//车队长
                            mFragment = mFragments[5];
                        }
                        break;
                    case R.id.radio_button_monitor:
                        mFragment = mFragments[6];
                        break;
                    case R.id.radio_button_mine:
                        mFragment = mFragments[7];
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


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                UIUtils.showT("再按一次退出登录");
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityStack.get().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
