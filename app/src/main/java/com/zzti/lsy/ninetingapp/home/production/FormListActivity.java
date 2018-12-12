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
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
public class FormListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.smartTable)
    SmartTable mSmartTable;

    //    private int pageIndex = 0;
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
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                whereStr = "";
                data.clear();
                setTitle("每日方量流水明细");
                getRecordList();
            }
        });
        showDia();
        getRecordList();
    }

    private List<StatisticalList> data = new ArrayList<>();

    private void getRecordList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(0));
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
                                statisticalList.setSlDateTime(statisticalList.getSlDateTime().split("T")[0]);
                                statisticalLists.add(statisticalList);
                                data.add(statisticalList);
                            }
                        } else {
                            UIUtils.showT("暂无数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
//                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
//                    }
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
        //普通列
        Column<String> column1 = new Column<>("车牌号", "plateNumber");
        column1.setFixed(true);
        Column<String> column2 = new Column<>("日期", "slDateTime");
        Column<String> column3 = new Column<>("方量", "squareQuantity");
        Column<String> column4 = new Column<>("8方以下", "eightBelow");
        Column<String> column5 = new Column<>("6方以下", "sixBelow");
        Column<String> column6 = new Column<>("补方", "addQuantity");
        Column<String> column7 = new Column<>("剩料", "remainder");
        Column<String> column8 = new Column<>("洗料耗时", "washTime");
        Column<String> column9 = new Column<>("加油升数", "qilWear");
        Column<String> column10 = new Column<>("距离基地里程", "distance");
        Column<String> column11 = new Column<>("加班时长", "addWorkTime");
        Column<String> column12 = new Column<>("加班趟数", "addWorkCount");
        Column<String> column13 = new Column<>("耗时", "timeConsuming");
        Column<String> column14 = new Column<>("备注", "remark");
//        if (pageIndex == 1) {
        //表格数据 datas是需要填充的数据
        TableData<StatisticalList> tableData = new TableData<>("每日方量流水明细", statisticalLists, column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11, column12, column13, column14);
        //table.setZoom(true,3);是否缩放
        mSmartTable.setTableData(tableData);
//        } else {
//            mSmartTable.addData(statisticalLists, true);
//        }
        column1.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column2.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column3.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column4.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column5.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column6.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column7.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column8.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column9.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column10.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column11.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column12.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column13.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column14.setOnColumnItemClickListener(new MyColumnItemClickListener());
    }

    class MyColumnItemClickListener implements OnColumnItemClickListener<String> {

        @Override
        public void onClick(Column<String> column, String value, String o, int position) {
            Intent intent = new Intent(FormListActivity.this, OneCarProDetailActivity.class);
            intent.putExtra("plateNumber", data.get(position).getPlateNumber());
            intent.putExtra("projectName", data.get(position).getProjectName());
            startActivity(intent);
        }
    }


    private void initView() {
        setTitle("每日方量流水明细" + DateUtil.getCurYear());
        whereStr = "";
        ivToolbarMenu.setVisibility(View.VISIBLE);
        ivToolbarMenu.setOnClickListener(this);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
        //使上拉加载具有弹性效果：
//        smartRefreshLayout.setEnableAutoLoadMore(false);


        TableConfig tableConfig = mSmartTable.getConfig();
        tableConfig.setVerticalPadding(DensityUtils.dp2px(8));
        tableConfig.setShowTableTitle(false);//显示表格标题
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
                whereStr = "slDateTime = \'" + DateUtil.getDate(date) + "\'";
                setTitle("每日方量流水明细" + DateUtil.getDate(date));
//                pageIndex = 1;
                showDia();
                data.clear();
                getRecordList();
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }
}
