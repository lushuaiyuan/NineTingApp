package com.zzti.lsy.ninetingapp.home.machinery;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.CarMaintenanceRecordAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.ConditionAdapter;
import com.zzti.lsy.ninetingapp.home.entity.CarMaintenanceEntity;
import com.zzti.lsy.ninetingapp.home.entity.ConditionEntity;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修记录
 */
public class MaintenanceRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rl_handleState)
    RelativeLayout rlHandleState;
    @BindView(R.id.rl_carState)
    RelativeLayout rlCarState;
    @BindView(R.id.tv_handleState)
    TextView tvHandleState;
    @BindView(R.id.tv_carState)
    TextView tvCarState;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private List<CarMaintenanceEntity> carMaintenanceEntities;
    private CarMaintenanceRecordAdapter carMaintenanceRecordAdapter;

    private PopupWindow popupWindowCondition;
    private ListView lvCondition;
    private ConditionAdapter conditionAdapter;
    private List<ConditionEntity> conditions;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_record;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        carMaintenanceEntities = new ArrayList<>();
        carMaintenanceRecordAdapter = new CarMaintenanceRecordAdapter(carMaintenanceEntities);
        mRecycleView.setAdapter(carMaintenanceRecordAdapter);
        carMaintenanceRecordAdapter.setOnItemClickListener(this);

        //TODO
        for (int i = 0; i < 5; i++) {
            CarMaintenanceEntity carMaintenanceEntity = new CarMaintenanceEntity();
            if (i % 2 == 0) {
                carMaintenanceEntity.setState("待处理");
            } else {
                carMaintenanceEntity.setState("已处理");
            }
            carMaintenanceEntity.setPartsName("配件名称" + i);
            carMaintenanceEntity.setCarNumber("车牌号" + i);
            carMaintenanceEntity.setReason("嘻嘻嘻嘻嘻嘻下" + i);
            carMaintenanceEntities.add(carMaintenanceEntity);
        }
        carMaintenanceRecordAdapter.notifyDataSetChanged();
    }


    private void initView() {
        setTitle("维修记录");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
        initPop_condition();
        if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) ==1) {//机械师

        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 4) {//项目经理

        } else if (SpUtils.getInstance().getInt(SpUtils.OPTYPE, -1) == 5) {//总经理

        }
    }

    private int condition;

    @OnClick({R.id.rl_handleState, R.id.rl_carState})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_handleState://处理状态
                condition = 1;
                for (int i = 0; i < 2; i++) {
                    ConditionEntity conditionEntity = new ConditionEntity();
                    if (i == 0) {
                        conditionEntity.setName("待处理");
                        conditionEntity.setId(i);
                    } else {
                        conditionEntity.setName("已处理");
                        conditionEntity.setId(i);
                    }
                    conditions.add(conditionEntity);
                }
                conditionAdapter.notifyDataSetChanged();
                if (conditions.size() >= 4) {
                    //动态设置listView的高度
                    View listItem = conditionAdapter.getView(0, null, lvCondition);
                    listItem.measure(0, 0);
                    int totalHei = (listItem.getMeasuredHeight() + lvCondition.getDividerHeight()) * 4;
                    lvCondition.getLayoutParams().height = totalHei;
                    ViewGroup.LayoutParams params = lvCondition.getLayoutParams();
                    params.height = totalHei;
                    lvCondition.setLayoutParams(params);
                }
                popupWindowCondition.showAsDropDown(rlHandleState, 0, 0, Gravity.LEFT);
                break;
            case R.id.rl_carState://车辆状态
                condition = 2;
                if (conditions.size() >= 4) {
                    //动态设置listView的高度
                    View listItem = conditionAdapter.getView(0, null, lvCondition);
                    listItem.measure(0, 0);
                    int totalHei = (listItem.getMeasuredHeight() + lvCondition.getDividerHeight()) * 4;
                    lvCondition.getLayoutParams().height = totalHei;
                    ViewGroup.LayoutParams params = lvCondition.getLayoutParams();
                    params.height = totalHei;
                    lvCondition.setLayoutParams(params);
                }
                popupWindowCondition.showAsDropDown(rlCarState, 0, 0, Gravity.LEFT);
                break;
        }
    }

    private void initPop_condition() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCondition = new PopupWindow(contentview, UIUtils.getWidth(this) / 2 - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCondition.setFocusable(true);
        popupWindowCondition.setOutsideTouchable(true);
        //设置消失监听
        popupWindowCondition.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCondition.dismiss();
                    return true;
                }
                return false;
            }
        });
        lvCondition = contentview.findViewById(R.id.pop_list);
        conditions = new ArrayList<>();
        conditionAdapter = new ConditionAdapter(conditions);
        lvCondition.setAdapter(conditionAdapter);
        lvCondition.setOnItemClickListener(this);
        popupWindowCondition.setAnimationStyle(R.style.anim_upPop);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (condition == 1) {//处理状态
            tvHandleState.setText(conditions.get(i).getName());
        } else if (condition == 2) {//车辆状态
            tvCarState.setText(conditions.get(i).getName());
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, MaintenanceRecordDetailActivity.class));
    }
}
