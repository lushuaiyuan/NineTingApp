package com.zzti.lsy.ninetingapp.home.parts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/9 14:56
 * 配件详情界面
 */
public class PartsDetailActivity extends BaseActivity {
    @BindView(R.id.btn_operator)
    Button btnOperator;


    //在库状态的话 下面的按钮显示出库  维修状态的话下面的按钮不显示  在途状态的话下面的按钮显示的是查看出库详情
    private int tag = 0; //1 在库  2维修  3在途

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
        tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 1) {
            btnOperator.setVisibility(View.VISIBLE);
            btnOperator.setText("出库");
        } else if (tag == 2) { //维修
            btnOperator.setVisibility(View.GONE);
        } else if (tag == 3) {//在途
            btnOperator.setVisibility(View.VISIBLE);
            btnOperator.setText("查看出库详情");
        }
    }

    private void initView() {
        setTitle("配件详情");
    }


}
