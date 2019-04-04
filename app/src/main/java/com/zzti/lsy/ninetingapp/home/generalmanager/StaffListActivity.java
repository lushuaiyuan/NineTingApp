package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.StaffEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.StaffAdapter;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.RuntimeRationale;
import com.zzti.lsy.ninetingapp.utils.UIUtils;
import com.zzti.lsy.ninetingapp.view.MAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.yanzhenjie.permission.PermissionActivity.requestPermission;

/**
 * author：anxin on 2018/10/12 11:47
 * 员工列表
 */
public class StaffListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_projectName)
    TextView tvProjectName;
    private List<StaffEntity> staffEntities;
    private StaffAdapter staffAdapter;
    private int type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_staff_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        final String projectID = UIUtils.getStr4Intent(this, "projectID");
        type = UIUtils.getInt4Intent(this, "TYPE");
        tvProjectName.setText(UIUtils.getStr4Intent(this, "projectName"));
        staffEntities = new ArrayList<>();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        staffAdapter = new StaffAdapter(staffEntities);
        mRecycleView.setAdapter(staffAdapter);
        staffAdapter.setOnItemClickListener(this);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getStaffList(projectID);
            }
        });
        showDia();
        getStaffList(projectID);
    }

    private void getStaffList(String projectID) {
        staffEntities.clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", projectID);
        OkHttpManager.postFormBody(Urls.PARTS_GETSTAFFLIST, params, mRecycleView, new OkHttpManager.OnResponse<String>() {
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
                            StaffEntity staffEntity = ParseUtils.parseJson(jsonArray.optString(i), StaffEntity.class);
                            staffEntities.add(staffEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                staffAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(mSmartRefreshLayout);
            }
        });
    }

    private void initView() {
        setTitle("员工列表");
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (type == 1) {
            Intent intent = new Intent();
            intent.putExtra("wearName", staffEntities.get(position).getStaffName());
            setResult(2,intent);
            finish();
        }else {
            final String phoneNumber = staffEntities.get(position).getStaffPhoneNumber();
            MAlertDialog.show(this, "提示", "是否拨打联系人" + staffEntities.get(position).getStaffName() + "的电话", false, "确定", "取消", new MAlertDialog.OnConfirmListener() {
                @Override
                public void onConfirmClick(String msg) {
                    requestPermission(Permission.CALL_PHONE);
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));//直接拨打电话
                    if (ActivityCompat.checkSelfPermission(StaffListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    StaffListActivity.this.startActivity(dialIntent);
                }

                @Override
                public void onCancelClick() {

                }
            }, true);
        }
    }


}
