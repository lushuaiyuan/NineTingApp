package com.zzti.lsy.ninetingapp.home.device;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;

/**
 * 设备保险年审
 */
public class BXNSFragment extends BaseFragment {
    private CarInfoEntity carInfoEntity;

    public static BXNSFragment newInstance() {
        BXNSFragment fragment = new BXNSFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            carInfoEntity = (CarInfoEntity) arguments.getSerializable("carInfoEntity");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bx_ns;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
