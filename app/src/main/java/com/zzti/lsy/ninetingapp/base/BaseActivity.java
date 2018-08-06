package com.zzti.lsy.ninetingapp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.jaeger.library.StatusBarUtil;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.utils.ActivityStack;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.CustomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @author ：on lsy on 2017/7/24 11:54
 * @Email：lushuaiyuan@itsports.club
 * @describe：基础类
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected ImageView ivToolbarBack;
    //    protected ImageView ivToolbarMenu;
    protected TextView tvToolbarMenu;
    protected TextView tvToolbarTitle;
    protected Toolbar mToolBar;

    protected SpUtils spUtils;
    private CustomDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        setStatusBar(getResources().getColor(R.color.colorPrimary), 255);
        ButterKnife.bind(this);
        ActivityStack.get()
                .add(this);
        spUtils = SpUtils.getInstance();
        mToolBar = findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            initTitle();
        }
        initAllMembersView(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (openEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (openEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMianThread(EventMessage paramEventCenter) {
        onEventComing(paramEventCenter);
    }

    protected void onEventComing(EventMessage paramEventCenter) {

    }

    public abstract int getContentViewId();

    protected boolean openEventBus() {
        return false;
    }

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    protected void initTitle() {
        ivToolbarBack = mToolBar.findViewById(R.id.iv_toolbarBack);
        tvToolbarTitle = mToolBar.findViewById(R.id.tv_toolbarTitle);
        tvToolbarMenu = mToolBar.findViewById(R.id.tv_toolbarMenu);
//        ivToolbarMenu = mToolBar.findViewById(R.id.iv_toolbarMenu);
        if (ivToolbarBack != null) {
            ivToolbarBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    finish();
                }
            });
        }
    }

    protected void setTitle(String paramString) {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(paramString);
        }
    }


    protected void setStatusBar(int color, int alpha) {
        StatusBarUtil.setColor(this, color, alpha);
    }


    private int count = 0;

    public void showDia() {
        if (loadingDialog == null) {
            //create
            loadingDialog = new CustomDialog(this, R.style.CustomDialog);
            count = 0;
        }
        if (!loadingDialog.isShowing()) {
            //show
            loadingDialog.show();
        }
        count++;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        cancelDia(true);
        // 结束Activity&从堆栈中移除
        ActivityStack.get()
                .remove(this);
        OkHttpManager.cancel(this);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (isOpen) {//隐藏软键盘
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
            view.clearFocus();
        }
    }

    //设置屏幕背景透明效果
    protected void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }
}
