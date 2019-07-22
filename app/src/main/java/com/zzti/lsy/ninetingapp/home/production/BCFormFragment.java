package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
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
 * @author lsy
 * @create 2019/4/10 13:52
 * @Describe 泵车表格
 */
public class BCFormFragment extends BaseFragment {
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.iv_clearStartTime)
    ImageView ivClearStartTime;
    @BindView(R.id.iv_clearEndTime)
    ImageView ivClearEndTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.smartTable)
    SmartTable mSmartTable;

    private String whereStr;
    private List<StatisticalList> statisticalLists = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bc_form;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        myIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && getActivity() != null && statisticalLists.size() == 0) {
            if (UIUtils.isNetworkConnected()) {
                showDia();
                myIsVisibleToUser = false;
                getRecordList();
            }
        }
    }

    private boolean myIsVisibleToUser;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(myIsVisibleToUser);
    }

    @Override
    protected void initView() {
        whereStr = "";
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
        TableConfig tableConfig = mSmartTable.getConfig();
        tableConfig.setVerticalPadding(DensityUtils.dp2px(8));
        tableConfig.setShowTableTitle(false);//显示表格标题
        tableConfig.setShowXSequence(false);//不显示顶部序号列
        tableConfig.setShowYSequence(false);//不显示左侧序号列
        tableConfig.setFixedYSequence(true);//固定左侧
        tableConfig.setFixedYSequence(true);//固定顶部
        tableConfig.setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.color_f5f5f5)));//设置列标题背景
        FontStyle fontStyle = new FontStyle(mActivity, 15, ContextCompat.getColor(mActivity, R.color.color_333333));
        //设置标题的字体样式
        LineStyle lineStyle = new LineStyle();
        lineStyle.setColor(ContextCompat.getColor(mActivity, R.color.color_333333));
        lineStyle.setEffect(new DashPathEffect(new float[]{5, 5}, 0));
        tableConfig.setColumnTitleStyle(fontStyle);

        tableConfig.setColumnTitleVerticalPadding(DensityUtils.dp2px(8));   //设置标题的间距  列标题上下
        tableConfig.setColumnTitleHorizontalPadding(DensityUtils.dp2px(10));   //设置标题的间距  列标题左右

    }

    @Override
    protected void initData() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                whereStr = "";
                tvStartTime.setText("");
                ivClearStartTime.setVisibility(View.GONE);
                tvEndTime.setText("");
                ivClearEndTime.setVisibility(View.GONE);
                statisticalLists.clear();
                getRecordList();
            }
        });
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
                getRecordList();
                break;
        }
    }

    private void getRecordList() {
        statisticalLists.clear();
        whereStr = "";
        if (!StringUtil.isNullOrEmpty(tvStartTime.getText().toString()) && !StringUtil.isNullOrEmpty(tvEndTime.getText().toString())) {
            whereStr = "'" + tvStartTime.getText().toString() + "'<slDateTime and slDateTime<'" + tvEndTime.getText().toString() + "'";
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(0));
        params.put("wherestr", whereStr);
        params.put("size", "5");
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
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                StatisticalList statisticalList = ParseUtils.parseJson(jsonArray.getString(i), StatisticalList.class);
                                statisticalList.setSlDateTime(statisticalList.getSlDateTime().split("T")[0]);
                                if (statisticalList.getVehicleTypeName().contains("泵车")) {
                                    statisticalLists.add(statisticalList);
                                }
                            }
                        }
                        if (statisticalLists.size() == 0) {
                            UIUtils.showT("暂无数据");
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
                endRefresh(smartRefreshLayout);
            }
        });
    }

    private void setTable() {
        //普通列
        Column<String> column1 = new Column<>("车牌号", "plateNumber");
        column1.setFixed(true);
        Column<String> column2 = new Column<>("日期", "slDateTime");
        Column<String> column3 = new Column<>("项目部", "projectName");
        Column<String> column4 = new Column<>("经手人", "staffName");
        Column<String> column5 = new Column<>("车辆类型", "vehicleTypeName");
        Column<String> column6 = new Column<>("施工地点", "workSite");
        Column<String> column7 = new Column<>("施工部位", "workPart");
        Column<String> column8 = new Column<>("泵送单价", "price");
        Column<String> column9 = new Column<>("加油升数", "qilWear");
        Column<String> column10 = new Column<>("油价", "wearPrice");
        Column<String> column11 = new Column<>("加油金额", "wearCount");
        Column<String> column12 = new Column<>("泵送方量", "inOrderqua");
        Column<String> column13 = new Column<>("结算方量", "acountQua");
        Column<String> column14 = new Column<>("泵送金额", "inOrderpriceCount");
        Column<String> column15 = new Column<>("外单方量", "onOrderqua");
        Column<String> column16 = new Column<>("外单单价", "onOrderprice");
        Column<String> column17 = new Column<>("外单金额", "onOrderpriceCount");
        Column<String> column18 = new Column<>("外单合同", "pactName");
        Column<String> column19 = new Column<>("合计方量", "quantityCount");
        Column<String> column20 = new Column<>("加油负责人", "wearUser");
        Column<String> column21 = new Column<>("备注", "remark");


        //表格数据 datas是需要填充的数据
        TableData<StatisticalList> tableData = new TableData<>("每日方量流水明细", statisticalLists, column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11, column12, column13, column14, column15, column16, column17,column18, column19, column20, column21);
        mSmartTable.setTableData(tableData);
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
        column15.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column16.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column17.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column18.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column19.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column20.setOnColumnItemClickListener(new MyColumnItemClickListener());
        column21.setOnColumnItemClickListener(new MyColumnItemClickListener());
    }

    public static BCFormFragment newInstance() {
        return new BCFormFragment();
    }

    class MyColumnItemClickListener implements OnColumnItemClickListener<String> {

        @Override
        public void onClick(Column<String> column, String value, String o, int position) {
            Intent intent = new Intent(mActivity, OneCarProDetailActivity.class);
            intent.putExtra("plateNumber", statisticalLists.get(position).getPlateNumber());
            intent.putExtra("projectName", statisticalLists.get(position).getProjectName());
            startActivity(intent);
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int type) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
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
}
