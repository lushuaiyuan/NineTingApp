package com.zzti.lsy.ninetingapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;


/**
 * 消息提示框
 *
 * @author lsy
 */
public class MAlertDialog {
    // 点击确认按钮回调接口
    public interface OnConfirmListener {
        void onConfirmClick(String msg);

        void onCancelClick();
    }


    /**
     * @param activity
     * @param title
     * @param content
     * @param oneButton
     * @param confirmText
     * @param cancelText
     * @param confirmListener
     */
    public static void show(Activity activity, String title, String content, boolean oneButton, String confirmText, String cancelText,
                            final OnConfirmListener confirmListener, boolean flag) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.view_alert, null);
        view.setBackgroundResource(R.drawable.circular_shape);
        TextView tvTitle = view.findViewById(R.id.title);
        final TextView tvContent = view.findViewById(R.id.message);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView cancel = view.findViewById(R.id.cancel);
        tvContent.setText(content);
        if (!StringUtil.isNullOrEmpty(title)) {
            tvTitle.setText(title);
        }
        confirm.setText(confirmText);
        if (!oneButton) { //显示两个按钮
            cancel.setText(cancelText);
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }
        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.myDialog).create();
        dialog.setCancelable(flag);// 设置点击dialog以外区域不取消 Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(UIUtils.getWidth(activity) / 5 * 4,
                LayoutParams.WRAP_CONTENT);// 设置弹出框宽度为屏幕宽度的3/4
        // 确定
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = null;
                if (!StringUtil.isNullOrEmpty(tvContent.getText().toString()))
                    msg = tvContent.getText().toString();
                confirmListener.onConfirmClick(msg);
                dialog.dismiss();
            }
        });
        // 取消
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onCancelClick();
                dialog.dismiss();
            }
        });
    }


}