package com.zzti.lsy.ninetingapp;

import android.support.v4.app.Fragment;

import com.zzti.lsy.ninetingapp.home.device.BXNSFragment;
import com.zzti.lsy.ninetingapp.home.device.CarDetailFragment;
import com.zzti.lsy.ninetingapp.home.device.DeviceManageFragment;
import com.zzti.lsy.ninetingapp.home.production.HomeFragment;
import com.zzti.lsy.ninetingapp.home.parts.PartsManagerFragment;
import com.zzti.lsy.ninetingapp.mine.MineFragment;
import com.zzti.lsy.ninetingapp.task.TaskFragment;

/**
 * author：anxin on 2018/8/3 16:22
 */
public class DataGenerator {

    public static Fragment[] getFragments() {
        Fragment fragments[] = new Fragment[5];
        fragments[0] = HomeFragment.newInstance();
        fragments[1] = PartsManagerFragment.newInstance();
        fragments[2] = DeviceManageFragment.newInstance();
        fragments[3] = TaskFragment.newInstance();
        fragments[4] = MineFragment.newInstance();
        return fragments;
    }

    public static Fragment[] getFragmentCarDetails() {
        Fragment fragments[] = new Fragment[2];
        fragments[0] = CarDetailFragment.newInstance();
        fragments[1] = BXNSFragment.newInstance();
        return fragments;
    }


}
