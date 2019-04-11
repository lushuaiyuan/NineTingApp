package com.zzti.lsy.ninetingapp.home.parts;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsDelivery;
import com.zzti.lsy.ninetingapp.event.C;
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
 * author：anxin on 2018/10/6 09:58
 * 配件出库记录
 */
public class PartsOutRecordActivity extends BaseActivity {
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.iv_clearStartTime)
    ImageView ivClearStartTime;
    @BindView(R.id.iv_clearEndTime)
    ImageView ivClearEndTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.table)
    SmartTable smartTable;
    private String PartsID;

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_outrecord;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        PartsID = UIUtils.getStr4Intent(this, "PartsID");
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                tvEndTime.setText("");
                tvStartTime.setText("");
                ivClearStartTime.setVisibility(View.GONE);
                ivClearEndTime.setVisibility(View.GONE);
                getOutList();
            }
        });
        showDia();
        getOutList();
    }

    @OnClick({R.id.tv_startTime, R.id.iv_clearStartTime, R.id.tv_endTime, R.id.iv_clearEndTime, R.id.iv_search})
    public void viewClick(View view) {
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
                getOutList();
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int type) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(PartsOutRecordActivity.this, new OnTimeSelectListener() {
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


    private String whereStr;

    /**
     * 出库记录
     */
    private void getOutList() {
        HashMap<String, String> params = new HashMap<>();
        whereStr = "";
        if (!StringUtil.isNullOrEmpty(PartsID)) {
            whereStr = " and PartsID=" + PartsID;
        }
        if (!StringUtil.isNullOrEmpty(tvStartTime.getText().toString()) && !StringUtil.isNullOrEmpty(tvEndTime.getText().toString())) {
            whereStr += " and '" + tvStartTime.getText().toString() + "'<pdDate and pdDate<'" + tvEndTime.getText().toString() + "'";
        }
        if (whereStr.length() > 0) {
            params.put("wherestr", whereStr.substring(5, whereStr.length()));
        } else {
            params.put("wherestr", "");
        }
        params.put("pageIndex", String.valueOf(0));
        OkHttpManager.postFormBody(Urls.PARTS_GETPARTSOUT, params, smartTable, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(mSmartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                List<PartsDelivery> partsDeliveries = new ArrayList<>();
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PartsDelivery partsDelivery = ParseUtils.parseJson(jsonArray.getString(i), PartsDelivery.class);
                            String pdDate = partsDelivery.getPdDate().split("T")[0];
                            partsDelivery.setPdDate(pdDate);
                            if (partsDelivery.getFormProject().equals(partsDelivery.getProjectID())) {//出库项目部ID和目的地项目部ID
                                partsDelivery.setPurpose("维修");
                            } else {
                                partsDelivery.setPurpose("调配");
                            }
                            partsDeliveries.add(partsDelivery);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                setTable(partsDeliveries);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    private void setTable(List<PartsDelivery> partsDeliveries) {
        //普通列
        Column<String> column1 = new Column<>("配件名称", "partsName");
        column1.setFixed(true);
        Column<String> column2 = new Column<>("配件型号", "partsModel");
        Column<String> column3 = new Column<>("出库项目部", "outProject");
        Column<String> column4 = new Column<>("目的项目部", "projectName");
        Column<String> column5 = new Column<>("经手人", "staffName");
        Column<String> column6 = new Column<>("出库时间", "pdDate");
        Column<String> column7 = new Column<>("出库数量", "pdNumber");
        Column<String> column8 = new Column<>("用途", "purpose");
        //表格数据 datas是需要填充的数据
        TableData<PartsDelivery> tableData = new TableData<>("配件出库记录", partsDeliveries, column1, column2, column3, column4, column5, column6, column7, column8);
        //table.setZoom(true,3);是否缩放
        smartTable.setTableData(tableData);
    }

    private void initView() {
        setTitle("出库记录");
        mSmartRefreshLayout.setEnableLoadMore(false);
        TableConfig tableConfig = smartTable.getConfig();
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
}
