package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairCoutEntity;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ChartUtils;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修统计
 */
public class MaintenanceStatistic extends BaseActivity {
    @BindView(R.id.tv_toolbarTitle)
    TextView tvTitle;
    @BindView(R.id.tv_repairAmount)
    TextView tvRepairAmount;
    @BindView(R.id.tv_repairMoney)
    TextView tvRepairMoney;
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_statistic;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        intData();
    }

    private void intData() {
        showDia();
        getRepaircount();
    }

    private void getRepaircount() {
        HashMap<String, String> params = new HashMap<>();
        params.put("plateNumber", "");
        OkHttpManager.postFormBody(Urls.REPAIR_GETREPAIRCOUNT, params, tvRepairAmount, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    tvRepairAmount.setText(msgInfo.getMsg());
                    List<RepairCoutEntity.RepairStatisticInfo> repairStatisticInfos = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData().toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RepairCoutEntity.RepairStatisticInfo repairStatisticInfo = ParseUtils.parseJson(jsonArray.getString(i), RepairCoutEntity.RepairStatisticInfo.class);
                            repairStatisticInfos.add(repairStatisticInfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ChartUtils.initChart(chart);
                    ChartUtils.notifyDataSetChanged(chart, getData(repairStatisticInfos), ChartUtils.monthValue);
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    private List<Entry> getData(List<RepairCoutEntity.RepairStatisticInfo> repairStatisticInfos) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < repairStatisticInfos.size(); i++) {
            Entry entry = new Entry();
            entry.setX(Integer.parseInt(repairStatisticInfos.get(i).getTime().split("-")[1]));
            entry.setY(Float.parseFloat(repairStatisticInfos.get(i).getMoney()));
            entries.add(entry);
        }
        return entries;
    }

    private void initView() {
        tvTitle.setText("维修统计");
    }


    @OnClick({R.id.iv_toolbarBack, R.id.tv_lookOneCar_maintenanceRecord, R.id.tv_toolbarMenu})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarBack:
                finish();
                break;
            case R.id.tv_lookOneCar_maintenanceRecord://查看单车维修统计
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("TAG", 1);
                intent.putExtra("FLAG", 2);
                startActivity(intent);
                break;
            case R.id.tv_toolbarMenu://查看报表
                startActivity(new Intent(this, MaintenanceReportActivity.class));
                break;
        }
    }
}
