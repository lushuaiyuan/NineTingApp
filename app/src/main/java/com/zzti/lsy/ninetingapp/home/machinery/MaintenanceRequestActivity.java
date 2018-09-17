package com.zzti.lsy.ninetingapp.home.machinery;

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
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.RepairCauseEntity;
import com.zzti.lsy.ninetingapp.entity.RepairTypeEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.entity.RequiredParts;
import com.zzti.lsy.ninetingapp.home.adapter.RepairCauseAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RepairTypeAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.RequiredPartsAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.network.Constant;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.photo.CustomHelper;
import com.zzti.lsy.ninetingapp.photo.PhotoAdapter;
import com.zzti.lsy.ninetingapp.photo.TakePhotoActivity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

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
public class MaintenanceRequestActivity extends TakePhotoActivity implements PopupWindow.OnDismissListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemChildClickListener, AdapterView.OnItemClickListener {
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
    //    @BindView(R.id.et_servicePersonnel)
//    EditText etServicePersonnel;//保修人
//    @BindView(R.id.et_servicePersonnelTel)
//    EditText etServicePersonnelTel;//保修人电话
    @BindView(R.id.tv_maintenanceTime)
    TextView tvMaintenanceTime;//计划维修时间
    //    @BindView(R.id.et_inputReason)
//    EditText etInputReason;//维修原因
//    @BindView(R.id.et_inputContent)
    EditText etInputContent;//维修内容
    @BindView(R.id.recycleView_photo)
    RecyclerView recyclerViewPhoto;
    @BindView(R.id.et_inputRemark)
    EditText etInputRemark;//维修原因

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
    private RequiredPartsAdapter requiredPartsAdapter; //不能修改
    private List<RequiredParts> requiredPartsList;

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
        requiredParts.setPartsAmount("1");

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

    @OnClick({R.id.ll_carNumber, R.id.ll_maintenanceType, R.id.ll_maintenanceReason, R.id.tv_addDetail, R.id.ll_maintenanceTime, R.id.btn_submit})
    public void viewClick(View view) {
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
                RequiredParts requiredParts = new RequiredParts();
                requiredParts.setPartsAmount("1");
                requiredPartsList.add(requiredParts);
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_maintenanceTime://计划维修时间
                showCustomTime();
                break;
            case R.id.btn_submit://提交
                finish();
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(MaintenanceRequestActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvMaintenanceTime.setText(DateUtil.getDate(date));

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
        if (pics.size() < 5) {
            if (!pics.contains("")) {
                if (pics.size() == 4) {
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
        if (StringUtil.isNullOrEmpty(pics.get(position))) {
            setBackgroundAlpha(0.5f);
            popupWindowPic.showAtLocation(view, Gravity.BOTTOM, 0, 0);
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_delete:
                requiredPartsList.remove(position);
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_sub:
                RequiredParts requiredParts1 = requiredPartsList.get(position);
                int amount1 = Integer.parseInt(requiredParts1.getPartsAmount());
                amount1--;
                requiredParts1.setPartsAmount(String.valueOf(amount1));
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_add:
                RequiredParts requiredParts2 = requiredPartsList.get(position);
                int amount2 = Integer.parseInt(requiredParts2.getPartsAmount());
                amount2++;
                requiredParts2.setPartsAmount(String.valueOf(amount2));
                requiredPartsAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_partsName:

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
    }

    private int tag;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (tag == 1) {
            tvMaintenanceType.setText(repairTypeEntities.get(i).getTypeName());
            popupWindowType.dismiss();
        } else if (tag == 2) {
            tvMaintenanceReason.setText(repairCauseEntities.get(i).getCauseName());
            popupWindowCause.dismiss();
        }
    }
}
