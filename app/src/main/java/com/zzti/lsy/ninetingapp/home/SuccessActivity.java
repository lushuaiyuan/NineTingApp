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
import com.zzti.lsy.ninetingapp.home.device.DeviceInputActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceInputListActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceOutputDetailActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceOutputListActivity;
import com.zzti.lsy.ninetingapp.home.generalmanager.PactInputActivity;
import com.zzti.lsy.ninetingapp.home.generalmanager.PactListActivity;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsListActivity;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsOutActivity;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsOutDetailActivity;
import com.zzti.lsy.ninetingapp.home.parts.LifeGoodsOutRecordActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsInWayDetailActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsListActivity;
import com.zzti.lsy.ninetingapp.home.production.FormListActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成功
 */
public class SuccessActivity extends BaseActivity {
    @BindView(R.id.btn_solid)
    Button btnSolid;
    @BindView(R.id.btn_empty)
    Button btnEmpty;
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

    int tag = 0; //1代表生产员的生产录入成功 2代表配件管理员的配件入库成功 3代表配件管理员的日用品录入成功  4配件管理员配件出库成功
    // 5配件管理员对日用品出库成功 6设备管理员录入成功 7设备管理员入库成功 8设备管理员出库成功 9合同录入
    int type;//1代表录入  2代表入库

    private void initData() {
        tag = UIUtils.getInt4Intent(this, "TAG");
        type = UIUtils.getInt4Intent(this, "TYPE");
        if (tag == 1) {
            setTitle("录入结果");
            tvMessage.setText("录入成功");
            btnSolid.setText("继续录入");
            btnEmpty.setText("生成表格");
        } else if (tag == 2) {
            setTitle("录入结果");
            tvMessage.setText("录入结果");
            btnSolid.setText("继续录入");
            btnEmpty.setText("查看列表");
        } else if (tag == 3) {
            setTitle("录入结果");
            tvMessage.setText("录入结果");
            btnSolid.setText("继续录入");
            btnEmpty.setText("查看列表");
        } else if (tag == 4) {
            setTitle("出库结果");
            tvMessage.setText("出库成功");
            btnSolid.setText("查看出库详情");
            btnEmpty.setText("查看出库列表");
        } else if (tag == 5) {
            setTitle("出库结果");
            tvMessage.setText("出库成功");
            btnSolid.setText("查看出库详情");
            btnEmpty.setText("查看出库列表");
        } else if (tag == 6) {
            setTitle("录入结果");
            tvMessage.setText("录入成功");
            btnSolid.setText("继续录入");
            btnEmpty.setText("查看列表");
        } else if (tag == 7) {
            setTitle("入库结果");
            tvMessage.setText("入库成功");
            btnSolid.setText("查看入库列表");
            btnEmpty.setVisibility(View.INVISIBLE);
            btnEmpty.setEnabled(false);
        } else if (tag == 8) {
            setTitle("出库结果");
            tvMessage.setText("出库成功");
            btnSolid.setText("查看出库详情");
            btnEmpty.setText("查看出库列表");
        } else if (tag == 9) {//合同
            setTitle("录入结果");
            tvMessage.setText("录入成功");
            btnSolid.setText("继续录入");
            btnEmpty.setText("查看列表");
        }

    }

    private void initView() {

    }

    @OnClick({R.id.btn_solid, R.id.btn_empty})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_solid:
                if (tag == 2) { //配件管理员继续录入配件
                    if (type == 2) {//入库
                        Intent intent2 = new Intent(this, PartsListActivity.class);
                        intent2.putExtra("TAG", 3);
                        startActivity(intent2);
                        EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                    }
                } else if (tag == 3) { //配件管理员继续录入日用品
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 4) { //查看配件在途详情
                    startActivity(new Intent(this, PartsInWayDetailActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 5) { //查看日用品出库详情
                    Intent intent = new Intent(this, LifeGoodsOutDetailActivity.class);
                    intent.putExtra("outTime", UIUtils.getStr4Intent(this, "outTime"));
                    intent.putExtra("lbID", UIUtils.getStr4Intent(this, "lbID"));
                    intent.putExtra("outAmount", UIUtils.getStr4Intent(this, "outAmount"));
                    intent.putExtra("goodsName", UIUtils.getStr4Intent(this, "goodsName"));
                    intent.putExtra("staffName", UIUtils.getStr4Intent(this, "staffName"));
                    startActivity(intent);
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 6) {//设备继续录入
                    startActivity(new Intent(this, DeviceInputActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 7) {//设备录入成功查看列表
                    startActivity(new Intent(this, DeviceInputListActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 8) {//设备出库成功查看详情
                    startActivity(new Intent(this, DeviceOutputDetailActivity.class));
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                } else if (tag == 9) {//总经理录入合同成功继续录入
                    Intent intent = new Intent(this, PactInputActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                }
                finish();
                break;
            case R.id.btn_empty:
                finish();
                if (tag == 1) {//生产员查看统计
                    startActivity(new Intent(this, FormListActivity.class));
                } else if (tag == 2) {//配件管理员查看配件列表
                    startActivity(new Intent(this, PartsListActivity.class));
                } else if (tag == 3) {//配件管理员查看日用品列表
                    startActivity(new Intent(this, LifeGoodsListActivity.class));
                } else if (tag == 4) {//配件管理员查看配件列表
                    startActivity(new Intent(this, PartsListActivity.class));
                } else if (tag == 5) {//配件管理员对日用品出库成功 查看列表
                    startActivity(new Intent(this, LifeGoodsListActivity.class));
                } else if (tag == 6) {//设备管理员录入成功
                    startActivity(new Intent(this, DeviceListActivity.class));
                } else if (tag == 8) {//设备管理员出库成功查看列表
                    startActivity(new Intent(this, DeviceOutputListActivity.class));
                } else if (tag == 9) {//总经理录入合同成功查看列表
                    Intent intent = new Intent(this, PactListActivity.class);
                    intent.putExtra("TAG", 1);
                    startActivity(intent);
                }
                EventBus.getDefault().post(new EventMessage<>(C.EventCode.A, true));
                break;
        }
    }


}
