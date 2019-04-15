package com.zzti.lsy.ninetingapp.home.device;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RentCarEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.CarTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RentCarAdapter;
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
import butterknife.OnClick;

/**
 * @author lsy
 * @create 2018/12/9 12:35
 * @Describe 租用车的Fragment
 */
public class RentCarListFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etCarNumber;
    @BindView(R.id.rl_carType)
    RelativeLayout rlCarType;
    @BindView(R.id.tv_carType)
    TextView tvCarType;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    private int pageIndex = 1;
    private String wherestr = "";
    private int flag = -1; //-1默认进入到详情界面 1代表获取车牌号 2代表进入单车统计界面 3代表选中 4代表进入单车生产统计
    private List<RentCarEntity> rentCarEntities;
    private RentCarAdapter rentCarAdapter;
    private String CarTypeID;//类型id
    private String tag;//罐车/泵车

    //车辆类型pop
    private PopupWindow popupWindowCarType;
    private ListView mListViewCarType;
    private CarTypeAdapter carTypeAdapter;
    private List<CarTypeEntity> carTypeEntities;

    public static RentCarListFragment newInstance() {
        RentCarListFragment fragment = new RentCarListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_platenumber;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        myIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && getActivity() != null && rentCarEntities.size() == 0) {
            if (UIUtils.isNetworkConnected()) {
                showDia();
                myIsVisibleToUser = false;
                getCarType();
                getRentCarList();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            flag = arguments.getInt("FLAG");
            tag = arguments.getString("Tag");
        }
    }


    @Override
    protected void initView() {
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        //使上拉加载具有弹性效果：
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        if (!StringUtil.isNullOrEmpty(tag)) {
            tvCarType.setText(tag);
            rlCarType.setEnabled(false);
            tvCarType.setEnabled(false);
            rlCarType.setClickable(false);
        }
        initCarTypePop();
    }

    private void initCarTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCarType = new PopupWindow(contentview, UIUtils.getWidth(mActivity) - DensityUtils.dp2px(16), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCarType.setFocusable(true);
        popupWindowCarType.setOutsideTouchable(true);
        popupWindowCarType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCarType.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewCarType = contentview.findViewById(R.id.pop_list);
        carTypeEntities = new ArrayList<>();
        carTypeAdapter = new CarTypeAdapter(carTypeEntities);
        carTypeAdapter.setTag(1);
        mListViewCarType.setAdapter(carTypeAdapter);
        mListViewCarType.setOnItemClickListener(this);
        popupWindowCarType.setAnimationStyle(R.style.anim_upPop);
    }

    @Override
    protected void initData() {
        rentCarEntities = new ArrayList<>();
        rentCarAdapter = new RentCarAdapter(rentCarEntities);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.setAdapter(rentCarAdapter);
        if (flag == 1) {
            rentCarAdapter.setOnItemClickListener(this);
        }
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getRentCarList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                if (StringUtil.isNullOrEmpty(tag)) {
                    tvCarType.setText("车辆类型");
                    CarTypeID = "";
                }

                pageIndex = 1;
                etCarNumber.setText("");
                rentCarEntities.clear();
                getRentCarList();
            }
        });
    }

    /**
     * 获取外租车的数据列表
     */
    private void getRentCarList() {
        wherestr = "";
        if (!StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
            wherestr += " and plateNumber like \'" + etCarNumber.getText().toString() + "%\'";
        }

        if (!StringUtil.isNullOrEmpty(tag)) {
            wherestr += " and vehicleTypeName like \'" + tag + "%\'";
        }
        if (!StringUtil.isNullOrEmpty(CarTypeID)) {
            wherestr += " and vehicleTypeID=" + CarTypeID;
        }
        HashMap<String, String> params = new HashMap<>();
        if (wherestr.length() > 0) {
            params.put("wherestr", wherestr.substring(5, wherestr.length()));
        } else {
            params.put("wherestr", wherestr);
        }
        params.put("pageIndex", String.valueOf(pageIndex));
        OkHttpManager.postFormBody(Urls.POST_GETRENTCARLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                            RentCarEntity rentCarEntity = ParseUtils.parseJson(jsonArray.getString(i), RentCarEntity.class);
                            rentCarEntities.add(rentCarEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(msgInfo.getMsg()) == pageIndex) {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                rentCarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    @OnClick({R.id.iv_search, R.id.rl_carType})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etCarNumber.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    break;
                }
                pageIndex = 1;
                showDia();
                rentCarEntities.clear();
                getRentCarList();
                break;
            case R.id.rl_carType:
                if (carTypeEntities.size() > 0) {
                    if (carTypeEntities.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = carTypeAdapter.getView(0, null, mListViewCarType);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListViewCarType.getDividerHeight()) * 5;
                        mListViewCarType.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListViewCarType.getLayoutParams();
                        params.height = totalHei;
                        mListViewCarType.setLayoutParams(params);
                    }
                    popupWindowCarType.showAsDropDown(rlCarType, 0, 0, Gravity.LEFT);
                } else {
                    UIUtils.showT("暂无数据");
                }

                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("carNumber", rentCarEntities.get(position).getPlateNumber());
        intent.putExtra("carType", rentCarEntities.get(position).getVehicleTypeName());
        intent.putExtra("carTypeID", rentCarEntities.get(position).getVehicleTypeID());
        mActivity.setResult(2, intent);
        mActivity.finish();
    }

    /**
     * 获取车辆类型
     */
    private void getCarType() {
        OkHttpManager.postFormBody(Urls.POST_GETCARTYPE, null, tvCarType, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    carTypeEntities.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CarTypeEntity carTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), CarTypeEntity.class);
                            carTypeEntities.add(carTypeEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        wherestr = "";
        tvCarType.setText(carTypeEntities.get(i).getVehicleTypeName());
        CarTypeID = carTypeEntities.get(i).getVehicleTypeID();
        popupWindowCarType.dismiss();
        showDia();
        rentCarEntities.clear();
        getRentCarList();
    }
}
