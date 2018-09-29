package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.FactoryInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.entity.PartsPurchased;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.event.EventMessage;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.home.adapter.FactoryAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
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
 * author：anxin on 2018/8/8 15:27
 * 配件录入
 */
public class PartsInputActivity extends BaseActivity implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    @BindView(R.id.et_partsName)
    EditText etPartsName;
    @BindView(R.id.et_model)
    EditText etModel;
    @BindView(R.id.tv_factory)
    TextView tvFactory;
    @BindView(R.id.et_way)
    EditText etWay;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_priceTitle)
    TextView tvPriceTitle;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_moneyTitle)
    TextView tvMoneyTitle;
    @BindView(R.id.rl_money)
    RelativeLayout rlMoney;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    //生产厂家pop
    private PopupWindow popupWindowFactory;
    private ListView mListViewFactory;
    private FactoryAdapter factoryAdapter;
    private List<FactoryInfoEntity> factoryInfoEntities;

    private PartsInfoEntity partsInfoEntity; //入库选择的配件实体类
    private PartsPurchased partsPurchased;//自己输入的实体类
    private int tag;//1代表录入  2代表入库
    private String factoryID; //配件厂件id

    @Override
    public int getContentViewId() {
        return R.layout.activity_parts_input;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    private void initData() {
        tvOperator.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
        tvTime.setText(DateUtil.getCurrentDate());
        tag = UIUtils.getInt4Intent(this, "TAG");
        partsPurchased = new PartsPurchased();
        if (tag == 2) {//入库
            tvMoneyTitle.setVisibility(View.GONE);
            rlMoney.setVisibility(View.GONE);
            tvPriceTitle.setVisibility(View.GONE);
            rlPrice.setVisibility(View.GONE);
            partsInfoEntity = (PartsInfoEntity) getIntent().getSerializableExtra("PartsInfo");
            etPartsName.setEnabled(false);
            etPartsName.setText(partsInfoEntity.getPartsName());
            etModel.setEnabled(false);
            etModel.setText(partsInfoEntity.getPartsModel());
            tvFactory.setEnabled(false);
            tvFactory.setText(partsInfoEntity.getFactoryName());
            factoryID = partsInfoEntity.getFactoryID();
            partsPurchased.setPartsID(partsInfoEntity.getPartsID());
            partsPurchased.setPurchasedPrice(partsInfoEntity.getPurchasedPrice());
        } else {//录入
            etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!StringUtil.isNullOrEmpty(etAmount.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                        int amount = Integer.parseInt(etAmount.getText().toString());
                        double price = Double.parseDouble(editable.toString());
                        tvTotalMoney.setText(String.valueOf(amount * price));
                    } else {
                        tvTotalMoney.setText("");
                    }
                }
            });
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!StringUtil.isNullOrEmpty(etPrice.getText().toString()) && !StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                        double price = Double.parseDouble(etPrice.getText().toString());
                        int amount = Integer.parseInt(editable.toString());
                        tvTotalMoney.setText(String.valueOf(amount * price));
                    } else {
                        tvTotalMoney.setText("");
                    }
                }
            });
            showDia();
            getPartsFactory();
            partsInfoEntity = new PartsInfoEntity();
        }

    }

    /**
     * 获取配件的生产厂家
     */
    private void getPartsFactory() {
        OkHttpManager.postFormBody(Urls.POST_GETPARTSFACTORY, null, tvFactory, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FactoryInfoEntity factoryInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), FactoryInfoEntity.class);
                            factoryInfoEntities.add(factoryInfoEntity);
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


    private void initView() {
        setTitle("配件录入");
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        initFactoryPop();
    }

    private void initFactoryPop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowFactory = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowFactory.setFocusable(true);
        popupWindowFactory.setOutsideTouchable(true);
        //设置消失监听
        popupWindowFactory.setOnDismissListener(this);
        popupWindowFactory.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowFactory.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewFactory = contentview.findViewById(R.id.pop_list);
        factoryInfoEntities = new ArrayList<>();
        factoryAdapter = new FactoryAdapter(factoryInfoEntities);
        mListViewFactory.setAdapter(factoryAdapter);
        mListViewFactory.setOnItemClickListener(this);
        popupWindowFactory.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.tv_factory, R.id.btn_submit})
    public void viewClick(View view) {
        hideSoftInput(etAmount);
        switch (view.getId()) {
            case R.id.tv_factory:
                if (factoryInfoEntities.size() > 0) {
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (factoryInfoEntities.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = factoryAdapter.getView(0, null, mListViewFactory);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListViewFactory.getDividerHeight()) * 5;
                        mListViewFactory.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListViewFactory.getLayoutParams();
                        params.height = totalHei;
                        mListViewFactory.setLayoutParams(params);
                    }
                    setBackgroundAlpha(0.5f);
                    popupWindowFactory.showAtLocation(tvFactory, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(C.Constant.NODATA);
                }
                break;
            case R.id.btn_submit:
                if (StringUtil.isNullOrEmpty(etPartsName.getText().toString())) {
                    UIUtils.showT("名称不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etModel.getText().toString())) {
                    UIUtils.showT("型号不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvFactory.getText().toString())) {
                    UIUtils.showT("生产厂家不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etWay.getText().toString())) {
                    UIUtils.showT("进货渠道不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                    UIUtils.showT("数量不能为空");
                    return;
                }
                if (StringUtil.isNullOrEmpty(tvOperator.getText().toString())) {
                    UIUtils.showT("经手人不能为空");
                    return;
                }

                if (tag == 1) {//录入
                    if (StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
                        UIUtils.showT("单价不能为空");
                        return;
                    }
                    if (StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                        UIUtils.showT("总金额不能为空");
                        return;
                    }
                    partsInfoEntity.setPartsName(etPartsName.getText().toString());
                    partsInfoEntity.setPartsModel(etModel.getText().toString());
                    partsInfoEntity.setFactoryID(factoryID);

                    partsPurchased.setPurchasedPrice(etPrice.getText().toString());
                }
                partsPurchased.setPurchasedChanel(etWay.getText().toString());
                partsPurchased.setNumber(etAmount.getText().toString());
                partsPurchased.setUserID(spUtils.getString(SpUtils.USERID, ""));
                hideSoftInput(etAmount);
                if (UIUtils.isNetworkConnected()) {
                    showDia();
                    submitInputData();
                }
                break;
        }

    }


    private void submitInputData() {
        HashMap<String, String> params = new HashMap<>();
        if (tag == 2) {//入库
            params.put("partsJosn", "");
        } else {
            params.put("partsJosn", new Gson().toJson(partsInfoEntity));
        }
        params.put("StorageJson", new Gson().toJson(partsPurchased));
        OkHttpManager.postFormBody(Urls.POST_PARTSSTORAGE, params, tvFactory, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    if (tag == 1) {//录入
                        etPartsName.getText().clear();
                        etModel.getText().clear();
                        etPrice.getText().clear();
                        etAmount.getText().clear();
                        tvTotalMoney.setText("");
                    } else {//入库
                        finish();
                    }
                    UIUtils.showT("提交成功");
                    Intent intent = new Intent(PartsInputActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 2);
                    if (tag == 1) {//录入
                        intent.putExtra("TYPE", 1);
                    } else if (tag == 2) {//入库
                        intent.putExtra("TYPE", 2);
                    }
                    startActivity(intent);
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

    @Override
    protected void onEventComing(EventMessage paramEventCenter) {
        super.onEventComing(paramEventCenter);
        if (paramEventCenter.getEventCode() == C.EventCode.A && (Boolean) paramEventCenter.getData()) {
            finish();
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tvFactory.setText(factoryInfoEntities.get(i).getFactoryName());
        factoryID = factoryInfoEntities.get(i).getFactoryID();
        popupWindowFactory.dismiss();
    }
}
