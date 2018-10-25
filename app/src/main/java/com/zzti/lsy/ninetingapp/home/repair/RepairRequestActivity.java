package com.zzti.lsy.ninetingapp.home.repair;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * 维修申请
 */
public class RepairRequestActivity extends TakePhotoActivity implements PopupWindow.OnDismissListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemChildClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.tv_carNumber)
    TextView tvCarNumber;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_constructionAddress)
    EditText etConstructionAddress;
    @BindView(R.id.tv_maintenanceType)
    TextView tvMaintenanceType;
    @BindView(R.id.tv_maintenanceReason)
    TextView tvMaintenanceReason;
    @BindView(R.id.recycleView_detail)
    RecyclerView recycleViewDetail;
    @BindView(R.id.tv_maintenanceStartTime)
    TextView tvMaintenanceStartTime;//计划开始维修时间
    @BindView(R.id.tv_maintenanceEndTime)
    TextView tvMaintenanceEndTime;//计划结束维修时间
    @BindView(R.id.et_inputContent)
    EditText etInputContent;//维修内容
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;
    @BindView(R.id.et_inputRemark)
    EditText etInputRemark;//维修原因
    @BindView(R.id.et_money)
    EditText etMoney;//维修金额


    //照片
    private CustomHelper customHelper;
    private View view;
    private PopupWindow popupWindowPic;
    private TextView tvPhoto;
    private TextView tvSelectPhoto;
    private TextView tvCancel;
    private PhotoAdapter photoAdapter;
    private List<String> pics;
    //维修类型
    private PopupWindow popupWindowType;
    private ListView mListViewType;
    private RepairTypeAdapter repairTypeAdapter;
    private List<RepairTypeEntity> repairTypeEntities;

    //维修原因
    private PopupWindow popupWindowCause;
    private ListView mListViewCause;
    private RepairCauseAdapter repairCauseAdapter;
    private List<RepairCauseEntity> repairCauseEntities;

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

        recycleViewDetail.setLayoutManager(new LinearLayoutManager(this));
        requiredPartsList = new ArrayList<>();
        requiredPartsList.add(requiredParts);
        requiredPartsAdapter = new RequiredPartsAdapter(requiredPartsList);
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


        getRepairType();
        getRepairCause();

        repairinfoEntity = new RepairinfoEntity();
    }

    /**
     * 获取维修原因
     */
    private void getRepairCause() {
        showDia();
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRTYPE, null, tvAddress, new OkHttpManager.OnResponse<String>() {
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
                            RepairTypeEntity repairTypeEntity = ParseUtils.parseJson(jsonArray.getString(i), RepairTypeEntity.class);
                            repairTypeEntities.add(repairTypeEntity);
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

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
    }

    /**
     * 获取维修类型
     */
    private void getRepairType() {
        showDia();
        HashMap<String, String> params = new HashMap<>();
        OkHttpManager.postFormBody(Urls.POST_GETREPAIRCAUSE, params, tvAddress, new OkHttpManager.OnResponse<String>() {
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
                            RepairCauseEntity repairCauseEntity = ParseUtils.parseJson(jsonArray.getString(i), RepairCauseEntity.class);
                            repairCauseEntities.add(repairCauseEntity);
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

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });
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
        initRepairTypePop();
        initRepairCausePop();

    }

    private void initRepairCausePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowCause = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowCause.setFocusable(true);
        popupWindowCause.setOutsideTouchable(true);
        //设置消失监听
        popupWindowCause.setOnDismissListener(this);
        popupWindowCause.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowCause.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewCause = contentview.findViewById(R.id.pop_list);
        repairCauseEntities = new ArrayList<>();
        repairCauseAdapter = new RepairCauseAdapter(repairCauseEntities);
        mListViewCause.setAdapter(repairCauseAdapter);
        mListViewCause.setOnItemClickListener(this);
        popupWindowCause.setAnimationStyle(R.style.anim_bottomPop);
    }

    private void initRepairTypePop() {
        View contentview = getLayoutInflater().inflate(R.layout.popup_list, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindowType = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowType.setFocusable(true);
        popupWindowType.setOutsideTouchable(true);
        //设置消失监听
        popupWindowType.setOnDismissListener(this);
        popupWindowType.setBackgroundDrawable(new ColorDrawable(0x00000000));
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowType.dismiss();
                    return true;
                }
                return false;
            }
        });
        mListViewType = contentview.findViewById(R.id.pop_list);
        repairTypeEntities = new ArrayList<>();
        repairTypeAdapter = new RepairTypeAdapter(repairTypeEntities);
        repairTypeAdapter.setTag(1);//背景色为白色
        mListViewType.setAdapter(repairTypeAdapter);
        mListViewType.setOnItemClickListener(this);
        popupWindowType.setAnimationStyle(R.style.anim_bottomPop);
    }

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

    @OnClick({R.id.ll_carNumber, R.id.ll_maintenanceType, R.id.ll_maintenanceReason, R.id.tv_addDetail, R.id.ll_maintenanceStartTime, R.id.ll_maintenanceEndTime, R.id.btn_submit})
    public void viewClick(View view) {
        hideSoftInput(etInputContent);
        switch (view.getId()) {
            case R.id.ll_carNumber://选择车辆
                Intent intent = new Intent(this, DeviceListActivity.class);
                intent.putExtra("FLAG", 1);
                startActivityForResult(intent, 1);
                break;
            case R.id.ll_maintenanceType://维修类型
                tag = 1;
                if (repairTypeEntities.size() > 0) {
                    hideSoftInput(tvAddress);
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (repairTypeEntities.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = repairTypeAdapter.getView(0, null, mListViewType);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListViewType.getDividerHeight()) * 5;
                        mListViewType.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListViewType.getLayoutParams();
                        params.height = totalHei;
                        mListViewType.setLayoutParams(params);
                    }
                    popupWindowType.showAtLocation(tvAddress, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(Constant.NONDATA);
                }
                break;
            case R.id.ll_maintenanceReason://维修原因
                tag = 2;
                if (repairCauseEntities.size() > 0) {
                    hideSoftInput(tvAddress);
                    //设置背景色
                    setBackgroundAlpha(0.5f);
                    if (repairCauseEntities.size() >= 5) {
                        //动态设置listView的高度
                        View listItem = repairCauseAdapter.getView(0, null, mListViewCause);
                        listItem.measure(0, 0);
                        int totalHei = (listItem.getMeasuredHeight() + mListViewCause.getDividerHeight()) * 5;
                        mListViewCause.getLayoutParams().height = totalHei;
                        ViewGroup.LayoutParams params = mListViewCause.getLayoutParams();
                        params.height = totalHei;
                        mListViewCause.setLayoutParams(params);
                    }
                    popupWindowCause.showAtLocation(tvAddress, Gravity.BOTTOM, 0, 0);
                } else {
                    UIUtils.showT(Constant.NONDATA);
                }
                break;
            case R.id.tv_addDetail://增加明细
                if (requiredPartsList.size() >= 5) {
                    UIUtils.showT("最多添加5条明细");
                    break;
                }
                if (StringUtil.isNullOrEmpty(requiredPartsList.get(requiredPartsList.size() - 1).getPartsName())) {
                    UIUtils.showT("请先完善上一条信息");
                    break;
                }
                RequiredParts requiredParts = new RequiredParts();
                requiredParts.setRpNumber("1");
                requiredPartsList.add(requiredParts);
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_maintenanceStartTime://计划开始维修时间
                showCustomTime(1);
                break;
            case R.id.ll_maintenanceEndTime://计划结束维修时间
                showCustomTime(2);
                break;
            case R.id.btn_submit://提交
                submitData();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submitData() {
        if (StringUtil.isNullOrEmpty(tvCarNumber.getText().toString())) {
            UIUtils.showT("请选择车牌号");
            return;
        }
        if (StringUtil.isNullOrEmpty(etConstructionAddress.getText().toString())) {
            UIUtils.showT("请输入施工地点");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvMaintenanceType.getText().toString())) {
            UIUtils.showT("请选择维修类型");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvMaintenanceReason.getText().toString())) {
            UIUtils.showT("请选择维修原因");
            return;
        }
        if (StringUtil.isNullOrEmpty(etMoney.getText().toString())) {
            UIUtils.showT("请输入维修金额");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvMaintenanceStartTime.getText().toString())) {
            UIUtils.showT("请选择计划开始维修时间");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvMaintenanceEndTime.getText().toString())) {
            UIUtils.showT("请选择计划结束维修时间");
            return;
        }
        if (StringUtil.isNullOrEmpty(etInputContent.getText().toString())) {
            UIUtils.showT("请输入维修内容");
            return;
        }
        repairinfoEntity.setPlateNumber(tvCarNumber.getText().toString());//车牌号
        repairinfoEntity.setRepairContent(etInputContent.getText().toString());//维修内容
        repairinfoEntity.setRepairMoney(etMoney.getText().toString());//维修金额
        repairinfoEntity.setRepairCauseID(repairCauseID);//维修原因ID
        repairinfoEntity.setRepairTypeID(repairTypeID);//维修类型
        repairinfoEntity.setProjectID(spUtils.getString(SpUtils.PROJECTID, ""));//项目部ID
        repairinfoEntity.setJobLocation(etConstructionAddress.getText().toString());//地址
        repairinfoEntity.setRepairBeginTime(tvMaintenanceStartTime.getText().toString());//计划开始维修时间
        repairinfoEntity.setRepairOverTime(tvMaintenanceEndTime.getText().toString());//计划结束维修时间
        repairinfoEntity.setUserID(spUtils.getString(SpUtils.USERID, ""));//申请人ID
        if (StringUtil.isNullOrEmpty(etInputRemark.getText().toString())) {
            repairinfoEntity.setRemark("");//备注
        } else {
            repairinfoEntity.setRemark(etInputRemark.getText().toString());
        }
        repairinfoEntity.setCauseName(tvMaintenanceReason.getText().toString());
        repairinfoEntity.setTypeName(tvMaintenanceType.getText().toString());
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
        submitNet();
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

    /**
     * 显示时间选择器
     */
    private void showCustomTime(final int tag) {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(RepairRequestActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (tag == 1) {
                    tvMaintenanceStartTime.setText(DateUtil.getDate(date));
                } else if (tag == 2) {
                    tvMaintenanceEndTime.setText(DateUtil.getDate(date));
                }

            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

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
        hideSoftInput(etInputContent);
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
                requiredPartsList.remove(position);
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_sub:
                RequiredParts requiredParts1 = requiredPartsList.get(position);
                int amount1 = Integer.parseInt(requiredParts1.getRpNumber());
                if (amount1 == 1)
                    break;
                amount1--;
                requiredParts1.setRpNumber(String.valueOf(amount1));
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_add:
                RequiredParts requiredParts2 = requiredPartsList.get(position);
                if (StringUtil.isNullOrEmpty(requiredParts2.getPartsName())) {
                    UIUtils.showT("请先选择配件");
                    break;
                }
                int amount2 = Integer.parseInt(requiredParts2.getRpNumber());
                amount2++;
                if (amount2 > Integer.parseInt(requiredParts2.getPartsNumber())) {
                    UIUtils.showT("配件所需数量不能大于库存数量");
                    break;
                }
                requiredParts2.setRpNumber(String.valueOf(amount2));
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_partsName:
                selectPosition = position;
                Intent intent = new Intent(this, PartsListActivity.class);
                intent.putExtra("TAG", 1);
                startActivityForResult(intent, 2);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            if (data != null)
                tvCarNumber.setText(data.getStringExtra("carNumber"));
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
                requiredPartsList.get(selectPosition).setPartsNumber(data.getStringExtra("partsNumber"));
                requiredPartsAdapter.notifyItemChanged(selectPosition);
            }
        }
    }

    private int tag;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (tag == 1) {
            tvMaintenanceType.setText(repairTypeEntities.get(i).getTypeName());
            repairTypeID = repairTypeEntities.get(i).getTypeID();
            popupWindowType.dismiss();
        } else if (tag == 2) {
            tvMaintenanceReason.setText(repairCauseEntities.get(i).getCauseName());
            repairCauseID = repairCauseEntities.get(i).getCauseID();
            popupWindowCause.dismiss();
        }
    }
}
