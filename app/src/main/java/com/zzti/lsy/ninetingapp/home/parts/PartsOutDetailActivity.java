package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAddressAdapter;
import com.zzti.lsy.ninetingapp.entity.ProjectAddressEntitiy;
import com.zzti.lsy.ninetingapp.network.Constant;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 出库详情
 */
public class PartsOutDetailActivity extends BaseActivity implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_outAddress)
    TextView tvOutAddress;
    @BindView(R.id.et_reason)
    TextView etReason;

    private PopupWindow popupWindow;
    private ListView mListView;
    private ProjectAddressAdapter projectAddressAdapter;
    private List<ProjectAddressEntitiy> projectAddressEntitiys;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pratsout_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initData() {
        tvOutTime.setText(DateUtil.getCurrentDate());
        //TODO
        for (int i = 0; i < 6; i++) {
            ProjectAddressEntitiy projectAddressEntitiy = new ProjectAddressEntitiy();
            projectAddressEntitiy.setId("5555" + i);
            projectAddressEntitiy.setName("项目部" + i);
            projectAddressEntitiys.add(projectAddressEntitiy);
        }
        projectAddressAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("配件出库");
        initPop();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListView = contentview.findViewById(R.id.pop_list);
        projectAddressEntitiys = new ArrayList<>();
        projectAddressAdapter = new ProjectAddressAdapter(projectAddressEntitiys);
        mListView.setAdapter(projectAddressAdapter);
        mListView.setOnItemClickListener(this);
        popupWindow.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.tv_outAddress, R.id.btn_out})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_outAddress:
                if (projectAddressEntitiys.size() > 0) {
                    hideSoftInput(tvOutAddress);
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (projectAddressEntitiys.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = projectAddressAdapter.getView(0, null, mListView);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListView.getDividerHeight()) * 5;
                        mListView.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListView.getLayoutParams();
                        params.height = totalHei;
                        mListView.setLayoutParams(params);
                    }
                    popupWindow.showAtLocation(tvOutAddress, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(Constant.NONDATA);
                }
                break;
            case R.id.btn_out:
                if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                    UIUtils.showT("出库数量不能为空");
                    return;
                }

                if (StringUtil.isNullOrEmpty(tvOutAddress.getText().toString())) {
                    UIUtils.showT("出库目的地不能为空");
                    return;
                }

                if (StringUtil.isNullOrEmpty(etReason.getText().toString())) {
                    UIUtils.showT("出库原因不能为空");
                    return;
                }
                outParts();
                break;
        }

    }

    //出库
    private void outParts() {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("TAG", 4);
        startActivity(intent);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvOutAddress.setText(projectAddressEntitiys.get(i).getName());
        popupWindow.dismiss();
    }

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }
}
