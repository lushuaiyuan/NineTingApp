package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.ProductRecordAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
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
 * @create 2018/12/4 22:37
 * @Describe 生产记录的界面
 */
public class ProductRecordActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etCarNumber;
    @BindView(R.id.tv_selectTime)
    TextView tvSelectTime;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    private List<StatisticalList> statisticalLists;
    private ProductRecordAdapter productRecordAdapter;

    private int pageIndex = 1;
    private String wherestr = "";

    @Override
    public int getContentViewId() {
        return R.layout.acitivity_product_record;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        statisticalLists = new ArrayList<>();
        productRecordAdapter = new ProductRecordAdapter(statisticalLists);
        mRecycleView.setAdapter(productRecordAdapter);
        productRecordAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getProductRecord();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                wherestr = "";
                tvSelectTime.setText("");
                etCarNumber.setText("");
                statisticalLists.clear();
                getProductRecord();
            }
        });
        showDia();
        getProductRecord();
    }

    private void initView() {
        setTitle("生产记录");
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        //使上拉加载具有弹性效果：
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @OnClick({R.id.tv_selectTime, R.id.iv_search})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_selectTime:
                showCustomTime();
                break;
            case R.id.iv_search:
                wherestr = "";
                if (!StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
                    wherestr += " and plateNumber like \'" + etCarNumber.getText().toString() + "%\'";
                }
                if (!StringUtil.isNullOrEmpty(tvSelectTime.getText().toString())) {
                    wherestr += " and slDateTime=" + "\"" + tvSelectTime.getText().toString() + "\"";
                }
                showDia();
                pageIndex = 1;
                statisticalLists.clear();
                getProductRecord();
                break;
        }
    }


    /**
     * 获取生产记录的数据
     */
    private void getProductRecord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(pageIndex));
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        OkHttpManager.postFormBody(Urls.RECORD_GETRECORDLIST, params, tvSelectTime, new OkHttpManager.OnResponse<String>() {
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
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                StatisticalList statisticalList = ParseUtils.parseJson(jsonArray.getString(i), StatisticalList.class);
                                statisticalList.setSlDateTime(statisticalList.getSlDateTime().split("T")[0]);
                                statisticalLists.add(statisticalList);
                            }
                        } else {
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
                productRecordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);

            }
        });
    }


    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(ProductRecordActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvSelectTime.setText(DateUtil.getDate(date));
            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ProductInputActivity.class);
        intent.putExtra("TAG", 1);
        intent.putExtra("StatisticalList", statisticalLists.get(position));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            showDia();
            statisticalLists.clear();
            getProductRecord();
        }
    }
}
