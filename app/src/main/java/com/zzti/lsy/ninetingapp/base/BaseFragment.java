package com.zzti.lsy.ninetingapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.LoginActivity;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.utils.ActivityStack;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.view.CustomDialog;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

import static com.scwang.smartrefresh.layout.constant.RefreshState.Loading;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;

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

    /**
     * 登出
     */
    protected void loginOut() {
        MAlertDialog.show(mActivity, "提示", "登录失效，请重新登录", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick(String msg) {
                SpUtils.getInstance().put(SpUtils.LOGINSTATE, false);
                SpUtils.getInstance().put(SpUtils.OPTYPE, -1);
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityStack.get().exit();
            }

            @Override
            public void onCancelClick() {

            }
        }, true);
    }
    protected void endRefresh(SmartRefreshLayout mSmartRefreshLayout) {
        if (mSmartRefreshLayout != null && mSmartRefreshLayout.getState() == Refreshing) {
            mSmartRefreshLayout.finishRefresh();
        }
        if (mSmartRefreshLayout != null && mSmartRefreshLayout.getState() == Loading) {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    //设置屏幕背景透明效果
    protected void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }
}
