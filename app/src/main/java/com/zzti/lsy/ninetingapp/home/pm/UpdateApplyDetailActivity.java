package com.zzti.lsy.ninetingapp.home.pm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.UpdateApply;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2019/4/4 10:21
 * @Describe 审批详情
 */
public class UpdateApplyDetailActivity extends BaseActivity {
    @BindView(R.id.tv_applyID)
    TextView tvApplyID;
    @BindView(R.id.tv_applyTime)
    TextView tvApplyTime;
    @BindView(R.id.tv_projectName)
    TextView tvProjectName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_applyUser)
    TextView tvApplyUser;
    @BindView(R.id.tv_updateInfo)
    TextView tvUpdateInfo;

    @Override
    public int getContentViewId() {
        return R.layout.activity_update_apply_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        UpdateApply updateApply = getIntent().getParcelableExtra("updateApply");
        tvApplyID.setText(updateApply.getApplyID());
        tvApplyTime.setText(updateApply.getApplyTime().replace("T", " "));
        tvApplyUser.setText(updateApply.getApplyUser());
        tvProjectName.setText(updateApply.getProjectName());
        tvUpdateInfo.setText(updateApply.getUpdateInfo().replace(";", "\n"));
        if (updateApply.getType().equals("0")) {//库存修改
            tvType.setText("库存修改");
        } else {//生产记录修改
            tvType.setText("生产修改");
        }
    }

    private void initView() {
        setTitle("审批详情");
    }

    @OnClick({R.id.btn_apply})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply:
                MAlertDialog.show(this, "温馨提示", "是否审核通过当前修改？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick(String msg) {
                        apply();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, true);
                break;
        }
    }

    private void apply() {
        HashMap<String, String> params = new HashMap<>();
        params.put("applyID", tvApplyID.getText().toString());
        OkHttpManager.postFormBody(Urls.APPROVE_APPROVENUMBER, params, tvApplyID, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    if(tvType.getText().toString().equals("库存修改")) {
                        EventBus.getDefault().post(new EventMessage(C.EventCode.I, null));
                    }else{
                        EventBus.getDefault().post(new EventMessage(C.EventCode.J, null));
                    }
                    finish();
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
}
