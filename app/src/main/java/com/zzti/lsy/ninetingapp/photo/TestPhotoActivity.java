package com.zzti.lsy.ninetingapp.photo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestPhotoActivity extends TakePhotoActivity implements View.OnClickListener, PopupWindow.OnDismissListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
    private CustomHelper customHelper;
    private View view;
    private PopupWindow popupWindow;
    private TextView tvPhoto;
    private TextView tvSelectPhoto;
    private TextView tvCancel;
    @BindView(R.id.recycleView_photo)
    RecyclerView mRecyclerView;
    PhotoAdapter photoAdapter;
    private List<String> pics;

    @Override
    public int getContentViewId() {
        view = LayoutInflater.from(this).
                inflate(R.layout.activity_testphoto, null);
        return R.layout.activity_testphoto;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        pics = new ArrayList<>();
        pics.add("");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        photoAdapter = new PhotoAdapter(pics);
        mRecyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(this);
        photoAdapter.setOnItemLongClickListener(this);
    }

    private void initView() {
        customHelper = CustomHelper.of(view);
        initPop();
    }

    private void initPop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.popup_photo, null);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (StringUtil.isNullOrEmpty(pics.get(position))) {
            setBackgroundAlpha(0.5f);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        if (!StringUtil.isNullOrEmpty(pics.get(position))) {

            MAlertDialog.show(this, "提示", "是否删除？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {

                @Override
                public void onConfirmClick(String msg) {
                    pics.remove(position);
                    if (!pics.contains("")) {
                        pics.add(pics.size(), "");
                    }
                    photoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelClick() {

                }
            }, true);
        }
        return false;
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
        if (pics.get(0).equals("")) {
            pics.remove(0);
        }
        for (int i = 0; i < images.size(); i++) {
            if (!StringUtil.isNullOrEmpty(images.get(i).getOriginalPath())) {
                pics.add(images.get(i).getOriginalPath());
            } else {
                pics.add(images.get(i).getCompressPath());
            }
        }
        if (pics.size() < 5) {
            if (!pics.contains("")) {
                if (pics.size() == 4) {
                    photoAdapter.notifyDataSetChanged();
                    return;
                } else {
                    pics.add(pics.size(), "");
                }
            } else {
                pics.remove("");
                pics.add(pics.size(), "");
            }
        } else {
            if (pics.contains("")) {
                pics.remove("");
            }
        }
        photoAdapter.notifyDataSetChanged();
    }
}
