package com.zzti.lsy.ninetingapp.home.pm;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.repair.RepairRecordDetailActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修报表
 */
public class MaintenanceReportActivity extends BaseActivity {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.iv_clearStartTime)
    ImageView ivClearStartTime;
    @BindView(R.id.iv_clearEndTime)
    ImageView ivClearEndTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.table)
    SmartTable mSmartTable;
    private List<RepairinfoEntity> repairinfoEntities;
    private String whereStr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_report;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }


    private void initData() {
        repairinfoEntities = new ArrayList<>();
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                hideSoftInput(etSearch);
                etSearch.setText("");
                whereStr = "";
                tvStartTime.setText("");
                tvEndTime.setText("");
                ivClearEndTime.setVisibility(View.GONE);
                ivClearStartTime.setVisibility(View.GONE);
                getRecord();
            }
        });
        showDia();
        getRecord();
    }

    /**
     * 获取数据
     */
    private void getRecord() {
        repairinfoEntities.clear();
        whereStr = "status = 0";
        if (!StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
            whereStr += " plateNumber like \'%" + etSearch.getText().toString() + "%\')";
        }
        if (!StringUtil.isNullOrEmpty(tvStartTime.getText().toString()) && !StringUtil.isNullOrEmpty(tvEndTime.getText().toString())) {
            whereStr += " and '" + tvStartTime.getText().toString() + "'<repairApplyTime and repairApplyTime<'" + tvEndTime.getText().toString() + "'";
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", "0");
        params.put("wherestr", whereStr);
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRLIST, params, mSmartTable, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(mSmartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RepairinfoEntity repairinfoEntity = ParseUtils.parseJson(jsonArray.getString(i), RepairinfoEntity.class);
                            repairinfoEntities.add(repairinfoEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                setTable();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    private void setTable() {
        //普通列
        Column<String> column1 = new Column<>("车牌号", "plateNumber");
        column1.setFixed(true);
        Column<String> column2 = new Column<>("项目部", "projectName");
        Column<String> column3 = new Column<>("维修配件金额", "repairParts");
        Column<String> column4 = new Column<>("维修费用", "repairMoney");
        Column<String> column5 = new Column<>("维修总费用", "repairAllMoney");
        Column<String> column6 = new Column<>("维修厂商", "repairFactory");
        Column<String> column7 = new Column<>("申请时间", "repairApplyTime");
        Column<String> column8 = new Column<>("申请人", "staffName");
        //表格数据 datas是需要填充的数据
        TableData<RepairinfoEntity> tableData = new TableData<>("维修记录", repairinfoEntities, column1, column2, column3, column4, column5, column6, column7, column8);
        //table.setZoom(true,3);是否缩放
        mSmartTable.setTableData(tableData);
        column1.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column2.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column3.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column4.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column5.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column6.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column7.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column8.setOnColumnItemClickListener(new MyColumnItemClickListener());
    }

    class MyColumnItemClickListener implements OnColumnItemClickListener<String> {

        @Override
        public void onClick(Column<String> column, String value, String o, int position) {
            Intent intent = new Intent(MaintenanceReportActivity.this, RepairRecordDetailActivity.class);
            intent.putExtra("RepairinfoEntity", repairinfoEntities.get(position));
            startActivity(intent);
        }
    }

    private void initView() {
        setTitle("表格查看");
        mSmartRefreshLayout.setEnableLoadMore(false);
        TableConfig tableConfig = mSmartTable.getConfig();
        tableConfig.setVerticalPadding(DensityUtils.dp2px(8));
        tableConfig.setShowTableTitle(false);//不显示表格标题
        tableConfig.setShowXSequence(false);//不显示顶部序号列
        tableConfig.setShowYSequence(false);//不显示左侧序号列
        tableConfig.setFixedYSequence(true);//固定左侧
        tableConfig.setFixedYSequence(true);//固定顶部
        tableConfig.setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.color_f5f5f5)));//设置列标题背景
        FontStyle fontStyle = new FontStyle(this, 15, ContextCompat.getColor(this, R.color.color_333333));
        //设置标题的字体样式
        LineStyle lineStyle = new LineStyle();
        lineStyle.setColor(ContextCompat.getColor(this, R.color.color_333333));
        lineStyle.setEffect(new DashPathEffect(new float[]{5, 5}, 0));
        tableConfig.setColumnTitleStyle(fontStyle);

        tableConfig.setColumnTitleVerticalPadding(DensityUtils.dp2px(8));   //设置标题的间距  列标题上下
        tableConfig.setColumnTitleHorizontalPadding(DensityUtils.dp2px(10));   //设置标题的间距  列标题左右

        tableConfig.setMinTableWidth(UIUtils.getWidth(this));
    }


    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int type) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(MaintenanceReportActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (type == 1) {
                    tvStartTime.setText(DateUtil.getDate(date));
                    ivClearStartTime.setVisibility(View.VISIBLE);
                } else {
                    tvEndTime.setText(DateUtil.getDate(date));
                    ivClearEndTime.setVisibility(View.VISIBLE);
                }
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }


    @OnClick({R.id.tv_startTime, R.id.iv_clearStartTime, R.id.tv_endTime, R.id.iv_clearEndTime, R.id.iv_search})
    public void viewClick(View view) {
        hideSoftInput(etSearch);
        switch (view.getId()) {
            case R.id.tv_startTime:
                showCustomTime(1);
                break;
            case R.id.iv_clearStartTime:
                tvStartTime.setText("");
                ivClearStartTime.setVisibility(View.GONE);
                break;
            case R.id.tv_endTime:
                showCustomTime(2);
                break;
            case R.id.iv_clearEndTime:
                tvEndTime.setText("");
                ivClearEndTime.setVisibility(View.GONE);
                break;
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(tvStartTime.getText().toString()) && !StringUtil.isNullOrEmpty(tvEndTime.getText().toString())) {
                    UIUtils.showT("请输入开始时间");
                    return;
                }
                if (!StringUtil.isNullOrEmpty(tvStartTime.getText().toString()) && StringUtil.isNullOrEmpty(tvEndTime.getText().toString())) {
                    UIUtils.showT("请输入结束时间");
                    return;
                }
                showDia();
                getRecord();
                break;
        }
    }
}
