package com.zzti.lsy.ninetingapp.mine;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.photo.CustomHelper;
import com.zzti.lsy.ninetingapp.photo.TakePhotoActivity;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/14 19:18
 * 我的信息
 */
public class MyMessageActivity extends TakePhotoActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    private CustomHelper customHelper;
    View view;
    private PopupWindow popupWindow;
    private TextView tvPhoto;
    private TextView tvSelectPhoto;
    private TextView tvCancel;

    @Override
    public int getContentViewId() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_mymessage, null);
        return R.layout.activity_mymessage;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        tvName.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, 0) == 1) {
            tvType.setText("生产员");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, 0) == 2) {
            tvType.setText("配件管理员");
        }
        customHelper = CustomHelper.of(view);
        initView();
    }


    private void initView() {
        setTitle("个人信息");
        initPop();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_photo, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        tvPhoto = contentview.findViewById(R.id.tv_photo);
        tvSelectPhoto = contentview.findViewById(R.id.tv_selectPhoto);
        tvCancel = contentview.findViewById(R.id.tv_cancel);
        tvPhoto.setOnClickListener(this);
        tvSelectPhoto.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        popupWindow.setAnimationStyle(R.style.anim_bottomPop);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_photo:
            case R.id.tv_selectPhoto:
                customHelper.onClick(1, false, view, getTakePhoto());
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(App.get())
                .load(images.get(0).getCompressPath()).apply(requestOptions)
                .into(ivHead);
        if (UIUtils.isNetworkConnected()) {
            //TODO 上传头像
//            upImage(images.get(0).getCompressPath());
        }
    }


    @OnClick(R.id.iv_head)
    public void viewClick() {
        //  设置背景色
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(tvName, Gravity.BOTTOM, 0, 0);
    }
}
