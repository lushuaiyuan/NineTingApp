package com.zzti.lsy.ninetingapp.mine;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.LoginActivity;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ActivityStack;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/3 16:39
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;


    public static MineFragment newInstance() {
        MineFragment taskFragment = new MineFragment();
        return taskFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        tvName.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 5) {
            tvType.setText("生产员");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 3) {
            tvType.setText("配件管理员");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 4) {
            tvType.setText("设备管理员");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 2) {
            tvType.setText("项目经理");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 0) {
            tvType.setText("总经理");
        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 1) {
            tvType.setText("机械师");
        }
    }


    @OnClick({R.id.ll_myMsg, R.id.ll_logOut})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_myMsg:
                startActivity(new Intent(mActivity, MyMessageActivity.class));
                break;
            case R.id.ll_logOut:
                MAlertDialog.show(mActivity, "提示", "确定退出？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        LoginOut();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    /**
     * 登出
     */
    private void LoginOut() {
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_LOGIN_OUT, params, tvName, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                SpUtils.getInstance().put(SpUtils.LOGINSTATE, false);
                SpUtils.getInstance().put(SpUtils.OPTYPE, -1);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityStack.get().exit();
            }
        });
    }

}
