package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairCauseEntity;
import com.zzti.lsy.ninetingapp.entity.RepairTypeEntity;
import com.zzti.lsy.ninetingapp.entity.RepairinfoEntity;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.RepairCauseAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RepairTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.parts.PartsListActivity;
import com.zzti.lsy.ninetingapp.network.Constant;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.CustomHelper;
import com.zzti.lsy.ninetingapp.photo.PhotoActivity;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.photo.TakePhotoActivity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ImageUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 维修申请
 */
public class RepairRequestActivity extends TakePhotoActivity implements PopupWindow.OnDismissListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;//车牌号
    @BindView(R.id.tv_carType)
    TextView tvCarType;//车辆类型
    @BindView(R.id.tv_address)
    TextView tvAddress;//地址
    @BindView(R.id.recycleView_detail)
    RecyclerView recycleViewDetail;//配件
    @BindView(R.id.et_receiptNo)
    EditText etReceiptNo;//维修单号
    @BindView(R.id.et_money)
    EditText etMoney;//维修金额
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;//维修总金额
    @BindView(R.id.et_repairFactory)
    EditText etRepairFactory;//维修厂商
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;//照片
    @BindView(R.id.et_remark)
    EditText etRemark;//维修原因


    //照片
    private CustomHelper customHelper;
    private View view;
    private PopupWindow popupWindowPic;
    private TextView tvPhoto;
    private TextView tvSelectPhoto;
    private TextView tvCancel;
    private PhotoAdapter photoAdapter;
    private List<String> pics;
    //维修明细
    private RequiredPartsAdapter requiredPartsAdapter;
    private List<RequiredParts> requiredPartsList;

    //维修申请的实体类
    private RepairinfoEntity repairinfoEntity;
    //参数
    private String repairCauseID;//维修原因ID 下拉选择
    private String repairTypeID;//维修类型ID 下拉选择


    @Override
    public int getContentViewId() {
        view = LayoutInflater.from(this).
                inflate(R.layout.activity_maintenance_request, null);
        return R.layout.activity_maintenance_request;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        RequiredParts requiredParts = new RequiredParts();
        requiredParts.setRpNumber("1");
        requiredParts.setType(0);
        recycleViewDetail.setLayoutManager(new LinearLayoutManager(this));
        requiredPartsList = new ArrayList<>();
        requiredPartsList.add(requiredParts);
        requiredPartsAdapter = new RequiredPartsAdapter(requiredPartsList);
        requiredPartsAdapter.setGroupSelectListener(new MyGroupSelectListener());
        requiredPartsAdapter.setPartNameClickListener(new MyPartNameClickListener());
        requiredPartsAdapter.setType(1);
        recycleViewDetail.setAdapter(requiredPartsAdapter);
        requiredPartsAdapter.setOnItemChildClickListener(this);

        pics = new ArrayList<>();
        pics.add("");
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        photoAdapter = new PhotoAdapter(pics);
        recyclerViewPhoto.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(this);
        photoAdapter.setOnItemLongClickListener(this);
        repairinfoEntity = new RepairinfoEntity();
    }

    private void initView() {
        setTitle("维修申请");
        tvAddress.setText(spUtils.getString(SpUtils.PROJECT, ""));
        customHelper = CustomHelper.of(view);
        //解决卡顿问题
        recycleViewDetail.setHasFixedSize(true);
        recycleViewDetail.setNestedScrollingEnabled(false);
        recyclerViewPhoto.setHasFixedSize(true);
        recyclerViewPhoto.setNestedScrollingEnabled(false);
        initPopPic();
        etMoney.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etMoney.addTextChangedListener(new MyTextChangedListener());
    }

    class MyTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            double partMoney = 0;
            for (int i = 0; i < requiredPartsList.size(); i++) {
                RequiredParts requiredParts = requiredPartsList.get(i);
                String rpNumber = requiredParts.getRpNumber();
                String purchasedPrice = requiredParts.getPurchasedPrice();
                double money = Integer.parseInt(rpNumber) * Double.parseDouble(purchasedPrice);
                partMoney += money;
            }
            if (!StringUtil.isNullOrEmpty(editable.toString())) {
                double money = Double.parseDouble(editable.toString());
                tvTotalMoney.setText(String.valueOf(partMoney + money));
            } else {
                tvTotalMoney.setText(String.valueOf(partMoney + 0));
            }

        }
    }

    private String partsMoney;

    private void initPopPic() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_photo, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowPic = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowPic.setFocusable(true);
        popupWindowPic.setOutsideTouchable(true);
        //设置消失监听
        popupWindowPic.setOnDismissListener(this);
        popupWindowPic.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowPic.dismiss();
                    return true;
                }
                return false;
            }
        });
        tvPhoto = contentview.findViewById(R.id.tv_photo);
        tvSelectPhoto = contentview.findViewById(R.id.tv_selectPhoto);
        tvCancel = contentview.findViewById(R.id.tv_cancel);
        tvPhoto.setOnClickListener(this);
        tvSelectPhoto.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        popupWindowPic.setAnimationStyle(R.style.anim_bottomPop);
    }

    @OnClick({R.id.ll_carNumber, R.id.tv_addDetail, R.id.btn_submit})
    public void viewClick(View view) {
        hideSoftInput(etMoney);
        switch (view.getId()) {
            case R.id.ll_carNumber://选择车辆
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("FLAG", 1);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_addDetail://增加明细
                etMoney.clearFocus();
                if (requiredPartsList.size() >= 5) {
                    UIUtils.showT("最多添加5条明细");
                    break;
                }
                RequiredParts requiredParts = requiredPartsList.get(requiredPartsList.size() - 1);
                EditText etPartsName = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, requiredPartsList.size() - 1, R.id.et_partsName);
                EditText etPrice = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, requiredPartsList.size() - 1, R.id.et_price);
                if (StringUtil.isNullOrEmpty(etPartsName.getText().toString()) || StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
                    UIUtils.showT("配件和金额不能为空");
                    break;
                } else {
                    if (etPartsName.getText().toString().split("#").length == 2) {
                        requiredParts.setPartsName(etPartsName.getText().toString().split("#")[1]);
                        requiredParts.setPartsModel(etPartsName.getText().toString().split("#")[0]);
                        requiredParts.setPurchasedPrice(etPrice.getText().toString());
                        etPartsName.clearFocus();
                        etPrice.clearFocus();
                    } else {
                        UIUtils.showT("请输入正确配件名称格式");
                        etPartsName.requestFocus();
                        etPrice.clearFocus();
                        break;
                    }
                }
                RequiredParts requiredParts2 = new RequiredParts();
                requiredParts2.setRpNumber("1");
                requiredParts2.setType(0);
                requiredPartsList.add(requiredParts2);
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_submit://提交
                submitData();
                break;
        }
    }


    class MyOnFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                RequiredParts requiredParts = requiredPartsList.get(requiredPartsList.size() - 1);
                EditText etPartsName = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, requiredPartsList.size() - 1, R.id.et_partsName);
                EditText etPrice = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, requiredPartsList.size() - 1, R.id.et_price);
                if (!StringUtil.isNullOrEmpty(etPartsName.getText().toString()) || !StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
                    if (requiredParts.getType() == 1) {
                        if (etPartsName.getText().toString().split("#").length == 2) {
                            requiredParts.setPartsName(etPartsName.getText().toString().split("#")[1]);
                            requiredParts.setPartsModel(etPartsName.getText().toString().split("#")[0]);
                            requiredParts.setPurchasedPrice(etPrice.getText().toString());
                        } else {
                            etPartsName.requestFocus();
                            view.clearFocus();
                            UIUtils.showT("请输入正确配件名称格式");
                        }
                    }
                } else {
                    view.clearFocus();
                    UIUtils.showT("配件和金额不能为空");
                }
            }
        }
    }

    /**
     * 提交数据
     */
    private void submitData() {
        if (StringUtil.isNullOrEmpty(tvCarNumber.getText().toString())) {
            UIUtils.showT("车牌号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvCarType.getText().toString())) {
            UIUtils.showT("车辆类型不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvAddress.getText().toString())) {
            UIUtils.showT("项目不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etReceiptNo.getText().toString())) {
            UIUtils.showT("维修单号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etMoney.getText().toString())) {
            UIUtils.showT("维修金额不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
            UIUtils.showT("维修总金额不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etRepairFactory.getText().toString())) {
            UIUtils.showT("维修厂商不能为空");
            return;
        }
        repairinfoEntity.setReceiptNo(etReceiptNo.getText().toString());//维修单号
        repairinfoEntity.setPlateNumber(tvCarNumber.getText().toString());//车牌号
        repairinfoEntity.setVehicleTypeID(vehicleTypeID);//车辆类型
        repairinfoEntity.setRepairParts(String.valueOf(Double.parseDouble(tvTotalMoney.getText().toString()) - Double.parseDouble(etMoney.getText().toString())));//维修配件金额
        repairinfoEntity.setRepairMoney(etMoney.getText().toString());//维修金额
        repairinfoEntity.setRepairAllMoney(tvTotalMoney.getText().toString());//维修总费用
        repairinfoEntity.setProjectID(spUtils.getString(SpUtils.PROJECTID, ""));//项目部ID
        repairinfoEntity.setUserID(spUtils.getString(SpUtils.USERID, ""));//申请人ID
        repairinfoEntity.setRepairFactory(etRepairFactory.getText().toString());//维修厂商
        if (StringUtil.isNullOrEmpty(etRemark.getText().toString())) {
            repairinfoEntity.setRemark("");//备注
        } else {
            repairinfoEntity.setRemark(etRemark.getText().toString());
        }
        repairinfoEntity.setStaffName(spUtils.getString(SpUtils.StAFFNAME, ""));
        if (pics.size() >= 2) {//有图片
            String devPicture = "";
            for (int i = 0; i < pics.size(); i++) {
                if (!StringUtil.isNullOrEmpty(pics.get(i))) {
                    devPicture += ImageUtils.bitmapToString(pics.get(i)) + "|";
                }
            }
            devPicture.substring(0, devPicture.length() - 1);
            repairinfoEntity.setDevPicture(devPicture);
        } else {
            repairinfoEntity.setDevPicture("");
        }
        new MAlertDialog().show(this, "温馨提示", "是否提交？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick(String msg) {
                submitNet();
            }

            @Override
            public void onCancelClick() {

            }
        }, true);

    }

    /**
     * 提交网络
     */
    private void submitNet() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        params.put("repairJson", new Gson().toJson(repairinfoEntity));
        if (requiredPartsList.size() == 1) {//判断是否为空
            if (StringUtil.isNullOrEmpty(requiredPartsList.get(0).getPartsName())) {
                params.put("partsJson", "");
            } else {
                params.put("partsJson", new Gson().toJson(requiredPartsList));
            }
        } else {//判断最后一个是否为空
            if (StringUtil.isNullOrEmpty(requiredPartsList.get(requiredPartsList.size() - 1).getPartsNumber())) {
                requiredPartsList.remove(requiredPartsList.size() - 1);
                requiredPartsAdapter.notifyDataSetChanged();
            }
            params.put("partsJson", new Gson().toJson(requiredPartsList));
        }
        OkHttpManager.postFormBody(Urls.POST_ADDREPAIR, params, tvAddress, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    UIUtils.showT("提交成功");
                    finish();
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


    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_photo:
            case R.id.tv_selectPhoto:
                customHelper.onClick(1, false, view, getTakePhoto());
                popupWindowPic.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindowPic.dismiss();
                break;
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
        if (pics.get(0).equals("")) {
            pics.remove(0);
        }
        for (int i = 0; i < images.size(); i++) {
            if (!StringUtil.isNullOrEmpty(images.get(i).getOriginalPath())) {
                pics.add(images.get(i).getOriginalPath());
            } else {
                pics.add(images.get(i).getCompressPath());
            }
        }
        if (pics.size() < 4) {
            if (!pics.contains("")) {
                if (pics.size() == 3) {
                    photoAdapter.notifyDataSetChanged();
                    return;
                } else {
                    pics.add(pics.size(), "");
                }
            } else {
                pics.remove("");
                pics.add(pics.size(), "");
            }
        } else {
            if (pics.contains("")) {
                pics.remove("");
            }
        }
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        hideSoftInput(etMoney);
        if (StringUtil.isNullOrEmpty(pics.get(position))) {
            setBackgroundAlpha(0.5f);
            popupWindowPic.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("url", pics.get(position));
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        if (!StringUtil.isNullOrEmpty(pics.get(position))) {
            MAlertDialog.show(this, "提示", "是否删除？", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                @Override
                public void onConfirmClick(String msg) {
                    pics.remove(position);
                    if (!pics.contains("")) {
                        pics.add(pics.size(), "");
                    }
                    photoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelClick() {

                }
            }, true);
        }
        return false;
    }

    private int selectPosition;

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_delete:
                RequiredParts requiredParts = requiredPartsList.get(position);
                String rpNumber = requiredParts.getRpNumber();
                String purchasedPrice = requiredParts.getPurchasedPrice();
                double deleteMoney = Integer.parseInt(rpNumber) * Double.parseDouble(purchasedPrice);
                requiredPartsList.remove(position);
                requiredPartsAdapter.notifyDataSetChanged();
                double totalMoney1 = Double.parseDouble(tvTotalMoney.getText().toString());
                tvTotalMoney.setText(String.valueOf(totalMoney1 - deleteMoney));
                break;
            case R.id.ib_sub:
                RequiredParts requiredParts1 = requiredPartsList.get(position);
                int amount1 = Integer.parseInt(requiredParts1.getRpNumber());
                if (amount1 == 1)
                    break;
                amount1--;
                requiredParts1.setRpNumber(String.valueOf(amount1));
                requiredPartsAdapter.notifyItemChanged(position);
                if (!StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                    //TODO
                    double totalMoney = Double.parseDouble(tvTotalMoney.getText().toString());
                    double purchasePrice = Double.parseDouble(requiredParts1.getPurchasedPrice());
                    tvTotalMoney.setText(String.valueOf(totalMoney - purchasePrice));
                } else {

                }
                break;
            case R.id.ib_add:
                RequiredParts requiredParts2 = requiredPartsList.get(position);
                int amount2 = Integer.parseInt(requiredParts2.getRpNumber());
                amount2++;
                if (requiredParts2.getType() == 0) {
                    if (StringUtil.isNullOrEmpty(requiredParts2.getPartsName())) {
                        UIUtils.showT("请先选择配件");
                        break;
                    }
                    if (amount2 > Integer.parseInt(requiredParts2.getPartsNumber())) {
                        UIUtils.showT("配件所需数量不能大于库存数量");
                        break;
                    }
                } else {
                    EditText etPartsName = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, position, R.id.et_partsName);
                    EditText etPrice = (EditText) requiredPartsAdapter.getViewByPosition(recycleViewDetail, position, R.id.et_price);
                    if (StringUtil.isNullOrEmpty(etPartsName.getText().toString()) || StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
                        UIUtils.showT("配件和金额不能为空");
                        break;
                    } else {
                        if (etPartsName.getText().toString().split("#").length == 2) {
                            requiredParts2.setPartsName(etPartsName.getText().toString().split("#")[1]);
                            requiredParts2.setPartsModel(etPartsName.getText().toString().split("#")[0]);
                            requiredParts2.setPurchasedPrice(etPrice.getText().toString());
                            etPartsName.clearFocus();
                            etPrice.clearFocus();
                        } else {
                            UIUtils.showT("请输入正确配件名称格式");
                            etPartsName.requestFocus();
                            etPrice.clearFocus();
                            break;
                        }
                    }
                }
                requiredParts2.setRpNumber(String.valueOf(amount2));
                requiredPartsAdapter.notifyItemChanged(position);
                if (!StringUtil.isNullOrEmpty(etMoney.getText().toString())) {
                    //TODO
                    double money = Double.parseDouble(etMoney.getText().toString());
                    double purchasePrice = Double.parseDouble(requiredParts2.getPurchasedPrice());
                    if (!StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                        double totalMoney = Double.parseDouble(tvTotalMoney.getText().toString());
                        tvTotalMoney.setText(String.valueOf(totalMoney + money + purchasePrice));
                    } else {
                        tvTotalMoney.setText(String.valueOf(money + purchasePrice));
                    }
                } else {
                    if (!StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                        double purchasePrice = Double.parseDouble(requiredParts2.getPurchasedPrice());
                        double totalMoney = Double.parseDouble(tvTotalMoney.getText().toString());
                        tvTotalMoney.setText(String.valueOf(totalMoney + purchasePrice));
                    } else {
                        tvTotalMoney.setText(requiredParts2.getPurchasedPrice());
                    }
                }
                break;

        }
    }

    class MyPartNameClickListener implements RequiredPartsAdapter.PartNameClickListener {

        @Override
        public void partNameClick(int position) {
            selectPosition = position;
            Intent intent = new Intent(RepairRequestActivity.this, PartsListActivity.class);
            intent.putExtra("TAG", 1);
            startActivityForResult(intent, 2);
        }
    }

    class MyGroupSelectListener implements RequiredPartsAdapter.GroupSelectListener {
        @Override
        public void radioSelect(int checkedId, int position) {
            int type = -1;
            switch (checkedId) {
                case R.id.rb_exist:
                    type = 0;
                    break;
                case R.id.rb_purchase:
                    type = 1;
                    break;

            }
            RequiredParts requiredPart = requiredPartsList.get(position);
            requiredPart.setPartsID("0");//配件id
            requiredPart.setPartsName("");//配件名称
            requiredPart.setPurchasedPrice("");//价格
            requiredPart.setRpNumber("1");//配件数量
            requiredPart.setPartsNumber("");//配件库存
            requiredPart.setType(type);
            requiredPartsAdapter.notifyItemChanged(position);
        }

    }

    private String vehicleTypeID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                tvCarNumber.setText(data.getStringExtra("carNumber"));
                tvCarType.setText(data.getStringExtra("carType"));
                vehicleTypeID = data.getStringExtra("carTypeID");
            }
        }
        if (requestCode == 2 && requestCode == 2) {
            if (data != null) {
                for (int i = 0; i < requiredPartsList.size(); i++) {
                    if (!StringUtil.isNullOrEmpty(requiredPartsList.get(i).getPartsName())) {
                        if (requiredPartsList.get(i).getPartsName().equals(data.getStringExtra("partsName"))) {
                            UIUtils.showT("您已选择过" + data.getStringExtra("partsName"));
                            return;
                        }
                    }
                }
                requiredPartsList.get(selectPosition).setPartsID(data.getStringExtra("partsID"));
                requiredPartsList.get(selectPosition).setPartsName(data.getStringExtra("partsName"));
                requiredPartsList.get(selectPosition).setPartsModel(data.getStringExtra("partsModel"));
                requiredPartsList.get(selectPosition).setPurchasedPrice(data.getStringExtra("partsPrice"));
                requiredPartsList.get(selectPosition).setPartsNumber(data.getStringExtra("partsNumber"));
                requiredPartsList.get(selectPosition).setType(0);
                requiredPartsAdapter.notifyItemChanged(selectPosition);
                if (!StringUtil.isNullOrEmpty(etMoney.getText().toString())) {
                    double money = Double.parseDouble(etMoney.getText().toString());
                    double purchasePrice = Double.parseDouble(requiredPartsList.get(selectPosition).getPurchasedPrice());
                    if (!StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                        double totalMoney = Double.parseDouble(tvTotalMoney.getText().toString());
                        tvTotalMoney.setText(String.valueOf(totalMoney + money + purchasePrice));
                    } else {
                        tvTotalMoney.setText(String.valueOf(money + purchasePrice));
                    }
                } else {
                    if (!StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                        double purchasePrice = Double.parseDouble(requiredPartsList.get(selectPosition).getPurchasedPrice());
                        double totalMoney = Double.parseDouble(tvTotalMoney.getText().toString());
                        tvTotalMoney.setText(String.valueOf(totalMoney + purchasePrice));
                    } else {
                        tvTotalMoney.setText(requiredPartsList.get(selectPosition).getPurchasedPrice());
                    }
                }
            }
        }
    }

}
