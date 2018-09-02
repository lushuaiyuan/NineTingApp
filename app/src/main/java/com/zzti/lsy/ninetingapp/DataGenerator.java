package com.zzti.lsy.ninetingapp;

import android.support.v4.app.Fragment;

import com.zzti.lsy.ninetingapp.home.device.BXNSFragment;
import com.zzti.lsy.ninetingapp.home.device.CarDetailFragment;
import com.zzti.lsy.ninetingapp.home.device.DeviceManageFragment;
import com.zzti.lsy.ninetingapp.home.generalmanager.GeneralManagerFragment;
import com.zzti.lsy.ninetingapp.home.machinery.MaintenanceManageFragment;
import com.zzti.lsy.ninetingapp.home.pm.PMManageFragment;
import com.zzti.lsy.ninetingapp.home.production.ProductManageFragment;
import com.zzti.lsy.ninetingapp.home.parts.PartsManagerFragment;
import com.zzti.lsy.ninetingapp.mine.MineFragment;
import com.zzti.lsy.ninetingapp.task.TaskFragment;

/**
 * author：anxin on 2018/8/3 16:22
 */
public class DataGenerator {

    public static Fragment[] getFragments() {
        Fragment fragments[] = new Fragment[8];
        fragments[0] = ProductManageFragment.newInstance();
        fragments[1] = PartsManagerFragment.newInstance();
        fragments[2] = DeviceManageFragment.newInstance();
        fragments[3] = PMManageFragment.newInstance();
        fragments[4] = GeneralManagerFragment.newInstance();
        fragments[5] = MaintenanceManageFragment.newInstance();
        fragments[6] = TaskFragment.newInstance();
        fragments[7] = MineFragment.newInstance();
        return fragments;
    }

    public static Fragment[] getFragmentCarDetails() {
        Fragment fragments[] = new Fragment[2];
        fragments[0] = CarDetailFragment.newInstance();
        fragments[1] = BXNSFragment.newInstance();
        return fragments;
    }


}
