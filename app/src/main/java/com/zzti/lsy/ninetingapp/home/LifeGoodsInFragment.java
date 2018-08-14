package com.zzti.lsy.ninetingapp.home;

import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品入库界面
 */
public class LifeGoodsInFragment extends BaseFragment {
    @BindView(R.id.et_goodsName)
    EditText etGoodsName;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lifegoods_in;
    }

    @Override
    protected void initView() {
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    protected void initData() {
        tvOperator.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
    }

    @OnClick(R.id.btn_submit)
    public void viewClick() {
        if (StringUtil.isNullOrEmpty(etGoodsName.getText().toString())) {
            UIUtils.showT("名称不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
            UIUtils.showT("数量不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
            UIUtils.showT("单价不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
            UIUtils.showT("金额不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvOperator.getText().toString())) {
            UIUtils.showT("经手人不能为空");
            return;
        }
        submit();
    }

    private void submit() {

    }
}
