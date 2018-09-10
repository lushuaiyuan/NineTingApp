package com.zzti.lsy.ninetingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LoginBack;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StaffEntity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/8 09:39
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_type)
    EditText etType;

    @BindView(R.id.iv_showPwd)
    ImageView ivShowPwd;


    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        if (SpUtils.getInstance().getBoolean(SpUtils.LOGINSTATE, false)) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
    }

    private void initView() {
        ivShowPwd.setTag(0);//默认是暗文的形式显示的
        etUserName.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        etUserName.setSelection(etUserName.getText().length());
    }

    @OnClick({R.id.iv_showPwd, R.id.btn_login})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_showPwd:
                if ((Integer) ivShowPwd.getTag() == 0) {//显示成明文
                    ivShowPwd.setTag(1);
                    ivShowPwd.setImageResource(R.mipmap.icon_showpwd);
                    etPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ivShowPwd.setTag(0);
                    ivShowPwd.setImageResource(R.mipmap.icon_hidepwd);
                    etPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etPwd.setSelection(etPwd.getText().length());
                break;
            case R.id.btn_login:
                if (StringUtil.isNullOrEmpty(etUserName.getText().toString())) {
                    UIUtils.showT("用户名不能为空");
                    break;
                }
                if (StringUtil.isNullOrEmpty(etPwd.getText().toString())) {
                    UIUtils.showT("密码不能为空");
                    break;
                }
                if (UIUtils.isNetworkConnected()) {
                    showDia();
                    login();
                }
                break;
        }
    }

    private void login() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", etUserName.getText().toString());
        params.put("password", etPwd.getText().toString());
        OkHttpManager.postFormBody(Urls.POST_LOGIN_URL, params, etPwd, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    if (!StringUtil.isNullOrEmpty(msgInfo.getData())) {
                        LoginBack loginBack = ParseUtils.parseJson(msgInfo.getData(), LoginBack.class);
                        SpUtils.getInstance().put(SpUtils.OPTYPE, loginBack.getRoleID());//操作员类型
                        SpUtils.getInstance().put(SpUtils.USERID, loginBack.getUserID());
                        SpUtils.getInstance().put(SpUtils.SESSIONID, msgInfo.getMsg());
                        SpUtils.getInstance().put(SpUtils.USERNAME, etUserName.getText().toString());
                        SpUtils.getInstance().put(SpUtils.LOGINSTATE, true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
}
