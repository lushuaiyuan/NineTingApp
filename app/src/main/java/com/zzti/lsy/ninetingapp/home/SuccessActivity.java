package com.zzti.lsy.ninetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成功
 */
public class SuccessActivity extends BaseActivity {
    @BindView(R.id.btn_input)
    Button btnInput;
    @BindView(R.id.btn_form)
    Button btnForm;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    @Override
    public int getContentViewId() {
        return R.layout.activity_input_success;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    int tag = 0; //1代表生产员的生产录入成功 2代表配件管理员的配件录入成功  3配件管理员入库成功 4配件管理员出库成功

    private void initData() {
        tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 1) {
            setTitle("录入结果");
            tvMessage.setText("录入成功");
            btnInput.setText("继续录入");
            btnForm.setText("生成表格");
        } else if (tag == 2) {
            setTitle("录入结果");
            tvMessage.setText("录入成功");
            btnInput.setText("继续录入");
            btnForm.setText("查看列表");
        } else if (tag == 3) {
            setTitle("入库结果");
            tvMessage.setText("入库成功");
            btnInput.setText("查看入库列表");
            btnForm.setVisibility(View.GONE);
        } else if (tag == 4) {
            setTitle("出库结果");
            tvMessage.setText("出库成功");
            btnInput.setText("查看出库详情");
            btnForm.setText("查看出库列表");
        }

    }

    private void initView() {

    }

    @OnClick({R.id.btn_input, R.id.btn_form})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input:
                if (tag == 3) { //查看配件入库列表
                    startActivity(new Intent(this, PartsInListActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 4) { //查看配件在途详情
                    startActivity(new Intent(this, InWayDetailActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                }

                finish();
                break;
            case R.id.btn_form:
                finish();
                if (tag == 1) {//生产员查看统计
                    startActivity(new Intent(this, FormListActivity.class));
                } else if (tag == 2) {//设备管理员查看配件列表
                    startActivity(new Intent(this, PartsListActivity.class));
                } else if (tag == 4) {//设备管理员查看配件出库列表
                    startActivity(new Intent(this, PartsOutListActivity.class));
                }
                EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                break;
        }
    }


}
