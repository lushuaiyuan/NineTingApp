package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/8/7 15:59
 * 生产表格
 */
public class FormListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.smartTable)
    SmartTable mSmartTable;

    private int pageIndex = 1;
    private String whereStr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_form_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getRecordList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                setTitle(DateUtil.getCurrentDate());
                whereStr = "slDateTime = \'" + tvToolbarTitle.getText().toString() + "\'";
                getRecordList();
            }
        });
        showDia();
        getRecordList();
    }

    private void getRecordList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("wherestr", whereStr);
        OkHttpManager.postFormBody(Urls.RECORD_GETRECORDLIST, params, mSmartTable, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(smartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                List<StatisticalList> statisticalLists = new ArrayList<>();
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                StatisticalList statisticalList = ParseUtils.parseJson(jsonArray.getString(i), StatisticalList.class);
                                statisticalList.setSlDateTime(statisticalList.getSlDateTime().split("T")[1]);
                                statisticalLists.add(statisticalList);
                            }
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                setTable(statisticalLists);
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }

    private void setTable(List<StatisticalList> statisticalLists) {
        if (pageIndex == 1) {
            //普通列
            Column<String> column1 = new Column<>("车牌号", "plateNumber");
            column1.setFixed(true);
            Column<Integer> column2 = new Column<>("日期", "slDateTime");
            Column<Long> column3 = new Column<>("方量", "squareQuantity");
            Column<String> column4 = new Column<>("油耗", "qilWear");
            Column<String> column5 = new Column<>("距离基地里程", "distance");
            Column<String> column6 = new Column<>("耗时", "timeConsuming");
            Column<String> column7 = new Column<>("备注", "remark");
            //表格数据 datas是需要填充的数据
            TableData<StatisticalList> tableData = new TableData<>("每日方量流水明细", statisticalLists, column1, column2, column3, column4, column5, column6, column7);
            //table.setZoom(true,3);是否缩放
            mSmartTable.setTableData(tableData);
        } else {
            mSmartTable.addData(statisticalLists, true);
        }

    }

    private void initView() {
        setTitle(DateUtil.getCurrentDate());
        whereStr = "slDateTime = \'" + tvToolbarTitle.getText().toString() + "\'";
        ivToolbarMenu.setVisibility(View.VISIBLE);
        ivToolbarMenu.setOnClickListener(this);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
        smartRefreshLayout.setEnableAutoLoadMore(false);


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
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, OneCarProDetailActivity.class));
    }

    @Override
    public void onClick(View view) {
        showCustomTime();
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(FormListActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                setTitle(DateUtil.getDate(date));
                whereStr = "slDateTime = \'" + tvToolbarTitle.getText().toString() + "\'";
                pageIndex = 1;
                showDia();
                getRecordList();
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }
}
