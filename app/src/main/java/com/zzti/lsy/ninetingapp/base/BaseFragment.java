package com.zzti.lsy.ninetingapp.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.view.CustomDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * @author ：on lsy on 2017/7/24 14:22
 * @Email：lushuaiyuan@itsports.club
 * @describe：
 */
public abstract class BaseFragment extends Fragment {
    protected ImageView ivToolbarBack;
    //    protected ImageView ivToolbarMenu;
    protected TextView tvToolbarMenu;
    protected TextView tvToolbarTitle;
    protected Toolbar mToolBar;

    protected Activity mActivity;
    private CustomDialog loadingDialog;


    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        View view = LayoutInflater.from(mActivity)
                .inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        if (openEventBus()) {
            EventBus.getDefault()
                    .register(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolBar = view.findViewById(R.id.toolbar);
        if (mToolBar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            initTitle();
        }
    }

    protected void initTitle() {
        ivToolbarBack = mToolBar.findViewById(R.id.iv_toolbarBack);
        tvToolbarTitle = mToolBar.findViewById(R.id.tv_toolbarTitle);
        tvToolbarMenu = mToolBar.findViewById(R.id.tv_toolbarMenu);
        ivToolbarBack.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 执行数据的加载
     */
    protected abstract void initView();

    /**
     * 执行数据的加载
     */
    protected abstract void initData();


    private int count = 0;

    public void showDia() {
        if (loadingDialog == null) {
            //create
            loadingDialog = new CustomDialog(mActivity, R.style.CustomDialog);
            count = 0;
        }
        if (!loadingDialog.isShowing()) {
            //show
            loadingDialog.show();
        }
        count++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelDia(true);
        if (openEventBus()) {
            EventBus.getDefault()
                    .unregister(this);
        }
    }

    protected boolean openEventBus() {
        return false;
    }

    public void cancelDia() {
        cancelDia(false);
    }

    private void cancelDia(boolean b) {
        if (b) {
            count = 0;
        }
        count--;
        if (count <= 0 && loadingDialog != null && loadingDialog.isShowing()) {
            //cancel
            loadingDialog.cancel();
        }
    }

}
