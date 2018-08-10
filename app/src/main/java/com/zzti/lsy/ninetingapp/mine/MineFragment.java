package com.zzti.lsy.ninetingapp.mine;

import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.utils.ActivityStack;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/3 16:39
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_loginOut)
    TextView tvLoginOut;

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
        tvToolbarTitle.setText("我的");
    }

    @Override
    protected void initData() {
        tvLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MAlertDialog.show(mActivity, "提示", "确定退出？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        SpUtils.getInstance().put(SpUtils.USERNAME, "");
                        SpUtils.getInstance().put(SpUtils.LOGINSTATE, false);
                        SpUtils.getInstance().put(SpUtils.OPTYPE, -1);
                        ActivityStack.get().exit();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
            }
        });
    }
}
