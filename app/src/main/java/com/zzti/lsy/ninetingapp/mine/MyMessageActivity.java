package com.zzti.lsy.ninetingapp.mine;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StaffEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.CustomHelper;
import com.zzti.lsy.ninetingapp.photo.TakePhotoActivity;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/14 19:18
 * 我的信息
 */
public class MyMessageActivity extends TakePhotoActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_staffName)
    TextView tvStaffName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_resume)
    TextView tvResume;
    @BindView(R.id.tv_project)
    TextView tvProject;
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
        initView();
        initData();
    }

    private void initData() {
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getStaff();
        }
    }

    /**
     * 获取员工角色
     */
    private void getStaff() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", spUtils.getString(SpUtils.USERID, ""));
        OkHttpManager.postFormBody(Urls.POST_GETSTAFF, params, tvToolbarMenu, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    StaffEntity staffEntity = ParseUtils.parseJson(msgInfo.getData(), StaffEntity.class);
                    tvStaffName.setText(staffEntity.getStaffName());
                    tvAge.setText(String.valueOf(staffEntity.getStaffAge()));
                    if (spUtils.getInt(SpUtils.OPTYPE, -1) == 0) {
                        tvType.setText("总经理");
                    } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 1) {
                        tvType.setText("机械师");
                    } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 2) {
                        tvType.setText("项目经理");
                    } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 3) {
                        tvType.setText("配件管理员");
                    } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 4) {
                        tvType.setText("设备管理员");
                    } else if (spUtils.getInt(SpUtils.OPTYPE, -1) == 5) {
                        tvType.setText("统计员");
                    }
                    tvResume.setText(staffEntity.getResume());
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }


    private void initView() {
        setTitle("个人信息");
        tvProject.setText(spUtils.getString(SpUtils.PROJECT,""));
        customHelper = CustomHelper.of(view);
//        initPop();
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


//    @OnClick(R.id.iv_head)
//    public void viewClick() {
//        //  设置背景色
//        setBackgroundAlpha(0.5f);
//        popupWindow.showAtLocation(tvName, Gravity.BOTTOM, 0, 0);
//    }
}
