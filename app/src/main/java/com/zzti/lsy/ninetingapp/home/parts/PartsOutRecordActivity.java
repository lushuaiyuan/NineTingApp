package com.zzti.lsy.ninetingapp.home.parts;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.LaobaoDelivery;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsDelivery;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * author：anxin on 2018/10/6 09:58
 * 配件出库记录
 */
public class PartsOutRecordActivity extends BaseActivity {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.table)
    SmartTable smartTable;
    private List<PartsDelivery> partsDeliveries;
    private int pageIndex = 0;
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
        partsDeliveries = new ArrayList<>();
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getOutList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 0;
                partsDeliveries.clear();
                getOutList();
            }
        });
        showDia();
        getOutList();
    }

    /**
     * 出库记录
     */
    private void getOutList() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (!StringUtil.isNullOrEmpty(PartsID)) {
            params.put("wherestr", "PartsID=" + PartsID);
        } else {
            params.put("wherestr", "");
        }
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
        Column<String> column1 = new Column<>("配件名称", "partsName");
        column1.setFixed(true);
        Column<String> column2 = new Column<>("配件型号", "partsModel");
        Column<String> column3 = new Column<>("出库项目部", "outProject");
        Column<String> column4 = new Column<>("目的项目部", "projectName");
        Column<String> column5 = new Column<>("经手人", "userID");
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
    }
}
