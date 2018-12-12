package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.utils.StringUtil;

import java.util.List;

/**
 * 维修记录明细
 */
public class RequiredPartsAdapter extends BaseQuickAdapter<RequiredParts, BaseViewHolder> {
    public RequiredPartsAdapter(List<RequiredParts> carMaintenanceEntities) {
        super(R.layout.item_required_parts, carMaintenanceEntities);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RequiredParts item) {
        helper.setText(R.id.tv_partsIndex, "配件明细（" + (helper.getAdapterPosition() + 1) + ")");
        EditText etPartName = helper.getView(R.id.et_partsName);
        EditText etMoney = helper.getView(R.id.et_money);
        RadioGroup radioGroup = helper.getView(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();
        switch (item.getModel()) {
            case 1:
                radioGroup.check(R.id.rb_exist);
                etPartName.setHint("请选择");
                etPartName.setEnabled(false);
                helper.getView(R.id.imageView).setVisibility(View.VISIBLE);
                etMoney.setHint("");
                etMoney.setEnabled(false);
                break;
            case 2:
                radioGroup.check(R.id.rb_purchase);
                etPartName.setHint("请输入所需配件型号——名称");
                etPartName.setEnabled(true);
                helper.getView(R.id.imageView).setVisibility(View.GONE);
                etMoney.setHint("请输入");
                etMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etMoney.setEnabled(true);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int p = 0;
                switch (checkedId) {
                    case R.id.rb_exist:
                        p = 1;
                        break;
                    case R.id.rb_purchase:
                        p = 2;
                        break;
                }
                item.setModel(p);
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
        if (StringUtil.isNullOrEmpty(item.getPartsName())) {
            helper.setText(R.id.et_partsName, "");
        } else {
            if (type == 1) {//录入
                helper.setText(R.id.et_partsName, item.getPartsName());
            } else {
                helper.setText(R.id.et_partsName, item.getPartsModel() + "——" + item.getPartsName());
            }
        }
        if (!StringUtil.isNullOrEmpty(item.getRpNumber())) { //所需配件数量
            helper.setText(R.id.tv_partsAmount, item.getRpNumber());
        }
        if (!StringUtil.isNullOrEmpty(item.getPartsNumber())) {//配件库存
            helper.setText(R.id.tv_amount, "库存" + item.getPartsNumber());
        }
        if (type == 1) {//录入的时候部分按钮可以操作
            if (helper.getAdapterPosition() >= 1) {
                helper.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            }
            helper.getView(R.id.ib_sub).setVisibility(View.VISIBLE);
            helper.getView(R.id.ib_add).setVisibility(View.VISIBLE);
            helper.getView(R.id.imageView).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
            if (item.getModel() == 1) {
                helper.getView(R.id.et_partsName).setClickable(true);
                helper.addOnClickListener(R.id.et_partsName);
            } else {
                helper.getView(R.id.et_partsName).setClickable(false);
            }
            helper.addOnClickListener(R.id.ib_sub).addOnClickListener(R.id.ib_add).addOnClickListener(R.id.tv_delete);
        } else {
            helper.getView(R.id.imageView).setVisibility(View.GONE);
            helper.getView(R.id.tv_delete).setVisibility(View.GONE);
            helper.getView(R.id.ib_sub).setVisibility(View.GONE);
            helper.getView(R.id.ib_add).setVisibility(View.GONE);
            helper.getView(R.id.tv_amount).setVisibility(View.GONE);
        }
    }

    private int type; //1代表录入  2代表记录

    public void setType(int type) {
        this.type = type;
    }
}
