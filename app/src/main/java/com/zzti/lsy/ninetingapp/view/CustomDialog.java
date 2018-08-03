package com.zzti.lsy.ninetingapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.zzti.lsy.ninetingapp.R;


/**
 * 作者：on lsy on 2017/7/24 12:03
 * 邮箱：lushuaiyuan@itsports.club
 * 描述：自定义进度条
 */
public class CustomDialog extends ProgressDialog {
//    public CustomDialog(Context context) {
//        super(context);
//    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        // 设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }


}
