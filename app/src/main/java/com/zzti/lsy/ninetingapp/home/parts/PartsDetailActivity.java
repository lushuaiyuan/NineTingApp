package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/9 14:56
 * 配件详情界面
 */
public class PartsDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_factory)
    TextView tvFactory;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;

    private PartsInfoEntity partsInfoEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        partsInfoEntity = (PartsInfoEntity) getIntent().getSerializableExtra("PartsInfo");
        tvAmount.setText(partsInfoEntity.getPartsNumber());
        tvFactory.setText(partsInfoEntity.getFactoryName());
        tvModel.setText(partsInfoEntity.getPartsModel());
        tvName.setText(partsInfoEntity.getPartsName());
        tvPrice.setText(partsInfoEntity.getPurchasedPrice());
        tvTotalMoney.setText(Integer.parseInt(partsInfoEntity.getPartsNumber()) * Double.parseDouble(partsInfoEntity.getPurchasedPrice()) + "");
    }

    @OnClick({R.id.btn_in, R.id.btn_out, R.id.btn_outRecord})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_in://入库
                Intent intent1 = new Intent(this, PartsInputActivity.class);
                intent1.putExtra("TAG", 2);//代表入库
                intent1.putExtra("PartsInfo", partsInfoEntity);
                startActivity(intent1);

                break;
            case R.id.btn_out://出库
                Intent intent = new Intent(this, PartsOutDetailActivity.class);
                intent.putExtra("partsID", partsInfoEntity.getPartsID());
                intent.putExtra("partsName", partsInfoEntity.getPartsName());
                intent.putExtra("partsModel", partsInfoEntity.getPartsModel());
                intent.putExtra("partsNumber", partsInfoEntity.getPartsNumber());
                startActivity(intent);

                break;
            case R.id.btn_outRecord://出库记录

                break;
        }
    }


    private void initView() {
        setTitle("配件详情");
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
