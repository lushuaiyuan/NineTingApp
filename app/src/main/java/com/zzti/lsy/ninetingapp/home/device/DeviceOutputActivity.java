package com.zzti.lsy.ninetingapp.home.device;

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

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.ProjectAddressAdapter;
import com.zzti.lsy.ninetingapp.home.entity.ProjectAddressEntitiy;
import com.zzti.lsy.ninetingapp.network.Constant;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备出库详情
 */
public class DeviceOutputActivity extends BaseActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.tv_outTime)
    TextView tvOutTime;
    @BindView(R.id.tv_outDestination)
    TextView tvOutDestination;
    @BindView(R.id.et_outReason)
    EditText etOutReason;

    //出库目的地
    private PopupWindow popupWindowProject;
    private ListView mListView;
    private ProjectAddressAdapter projectAddressAdapter;
    private List<ProjectAddressEntitiy> outDestinationEntitiys;

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_output;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            ProjectAddressEntitiy projectAddressEntitiy = new ProjectAddressEntitiy();
            projectAddressEntitiy.setId("5555" + i);
            projectAddressEntitiy.setName("项目部" + i);
            outDestinationEntitiys.add(projectAddressEntitiy);
        }
        projectAddressAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("出库详情");
        initPop();
    }

    private void initPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowProject = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowProject.setFocusable(true);
        popupWindowProject.setOutsideTouchable(true);
        //设置消失监听
        popupWindowProject.setOnDismissListener(this);
        popupWindowProject.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowProject.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListView = contentview.findViewById(R.id.pop_list);
        outDestinationEntitiys = new ArrayList<>();
        projectAddressAdapter = new ProjectAddressAdapter(outDestinationEntitiys);
        mListView.setAdapter(projectAddressAdapter);
        mListView.setOnItemClickListener(this);
        popupWindowProject.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.tv_outTime, R.id.tv_outDestination, R.id.btn_outPut})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_outTime:
                showCustomTime();
                break;
            case R.id.tv_outDestination:
                if (outDestinationEntitiys.size() > 0) {
                    hideSoftInput(tvOutDestination);
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (outDestinationEntitiys.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = projectAddressAdapter.getView(0, null, mListView);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListView.getDividerHeight()) * 5;
                        mListView.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListView.getLayoutParams();
                        params.height = totalHei;
                        mListView.setLayoutParams(params);
                    }
                    popupWindowProject.showAtLocation(tvOutDestination, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(Constant.NONDATA);
                }
                break;
            case R.id.btn_outPut:
                if (StringUtil.isNullOrEmpty(tvOutTime.getText().toString())) {
                    UIUtils.showT("请选择出库时间");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvOutDestination.getText().toString())) {
                    UIUtils.showT("出库目的地不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etOutReason.getText().toString())) {
                    UIUtils.showT("出库原因不能为空");
                    return;
                }
                Intent intent = new Intent(this, SuccessActivity.class);
                intent.putExtra("TAG", 8);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(DeviceOutputActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvOutTime.setText(DateUtil.getDate(date));

            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvOutDestination.setText(outDestinationEntitiys.get(i).getName());
        popupWindowProject.dismiss();
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}
