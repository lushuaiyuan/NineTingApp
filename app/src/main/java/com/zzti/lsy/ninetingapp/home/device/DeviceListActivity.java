package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.DeviceListAdapter;
import com.zzti.lsy.ninetingapp.entity.DeviceEntity;
import com.zzti.lsy.ninetingapp.home.pm.OneCarMaintenanceStatisticActivity;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rl_searchCarNumber)
    RelativeLayout rlSearchCarNumber;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_projectAddress)
    TextView tvProjectAddress;
    @BindView(R.id.tv_carState)
    TextView tvCarState;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.btn_contrast)
    Button btnContrast;

    private DeviceListAdapter deviceListAdapter;
    private List<DeviceEntity> deviceListData;
    private int flag = -1; //-1默认进入到详情界面 1代表获取车牌号 2代表进入单车统计界面 3代表选中
    private String carNumber;//对比的基准车牌号

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        deviceListData = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(deviceListData);
        mRecycleView.setAdapter(deviceListAdapter);
        deviceListAdapter.setOnItemClickListener(this);
        deviceListAdapter.setFlag(flag);
        //TODO
        for (int i = 0; i < 6; i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            if (i % 2 == 0) {
                deviceEntity.setCarState("存放中");
            } else if (i % 2 == 1) {
                deviceEntity.setCarState("维修中");
            } else if (i % 3 == 1) {
                deviceEntity.setCarState("工作中");
            }
            deviceEntity.setCarNumber("豫A5555" + i);
            if (!StringUtil.isNullOrEmpty(carNumber) && carNumber.equals(deviceEntity.getCarNumber())) {
                deviceEntity.setIsDefault(true);
            }
            deviceEntity.setCarType("宇通00" + i);
            deviceEntity.setProjectAddress("项目部" + i);
            deviceEntity.setAddress("滁州");
            deviceListData.add(deviceEntity);

        }
        deviceListAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setTitle("设备列表");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);
        int tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 1) {
            tvToolbarMenu.setVisibility(View.GONE);
            rlSearchCarNumber.setVisibility(View.GONE);
            llCondition.setVisibility(View.GONE);
        } else {
            tvToolbarMenu.setVisibility(View.VISIBLE);
            tvToolbarMenu.setText("表格");
            rlSearchCarNumber.setVisibility(View.VISIBLE);
            llCondition.setVisibility(View.VISIBLE);
        }
        flag = UIUtils.getInt4Intent(this, "FLAG");
        carNumber = UIUtils.getStr4Intent(this, "carNumber");
        carSelect.add(carNumber);
        if (flag == 3) {
            btnContrast.setVisibility(View.VISIBLE);
        } else {
            btnContrast.setVisibility(View.GONE);
        }
    }

    private List<String> carSelect = new ArrayList<>();

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (flag == -1) {
            Intent intent = new Intent(this, DeviceDetailActivity.class);
            intent.putExtra("TAG", 0);
            startActivity(intent);
        } else if (flag == 1) {
            Intent intent = new Intent();
            intent.putExtra("carNumber", deviceListData.get(position).getCarNumber());
            setResult(2, intent);
            finish();
        } else if (flag == 2) {
            Intent intent = new Intent(this, OneCarMaintenanceStatisticActivity.class);
            intent.putExtra("carNumber", deviceListData.get(position).getCarNumber());
            startActivity(intent);
        } else if (flag == 3) {
            if (!deviceListData.get(position).getCarNumber().equals(carNumber)) {
                if (deviceListData.get(position).isCheck()) {
                    deviceListData.get(position).setCheck(false);
                    deviceListAdapter.notifyDataSetChanged();
                    carSelect.remove(deviceListData.get(position).getCarNumber());
                } else {
                    if (carSelect.size() < 3) {
                        deviceListData.get(position).setCheck(true);
                        deviceListAdapter.notifyDataSetChanged();
                        carSelect.add(deviceListData.get(position).getCarNumber());
                    } else {
                        UIUtils.showT("每次最多只能对比三个");
                    }
                }

            }
        }
    }

    @OnClick({R.id.iv_search, R.id.rl_projectAddress, R.id.rl_carState, R.id.tv_carType, R.id.tv_toolbarMenu, R.id.btn_contrast})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("请输入车牌号");
                }

                break;
            case R.id.rl_projectAddress:

                break;
            case R.id.rl_carState:

                break;
            case R.id.tv_carType:

                break;
            case R.id.tv_toolbarMenu:
                startActivity(new Intent(this, DeviceFormActivity.class));
                break;
            case R.id.btn_contrast:
                UIUtils.showT("去对比");
                break;
        }
    }


}
