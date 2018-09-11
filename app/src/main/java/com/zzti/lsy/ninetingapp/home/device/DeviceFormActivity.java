package com.zzti.lsy.ninetingapp.home.device;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.CarInfoEntity;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 设备表格
 */
public class DeviceFormActivity extends BaseActivity {
    @BindView(R.id.table)
    SmartTable smartTable;
    private List<CarInfoEntity> carInfoEntities;

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_form;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        carInfoEntities = new ArrayList<>();
        showDia();
        getCarList();
    }


    private void initView() {
        setTitle("表格查看");
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

    private void getCarList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("wherestr", "");
        params.put("pageIndex", "0");
        OkHttpManager.postFormBody(Urls.POST_GETCARLIST, params, smartTable, new OkHttpManager.OnResponse<String>() {
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
                            CarInfoEntity carInfoEntity = ParseUtils.parseJson(jsonArray.getString(i), CarInfoEntity.class);
                            String AVEDate = carInfoEntity.getAVEDate().split("T")[0];
                            carInfoEntity.setAVEDate(AVEDate);
                            String IPDate = carInfoEntity.getIPDate().split("T")[0];
                            carInfoEntity.setIPDate(IPDate);
                            carInfoEntities.add(carInfoEntity);
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
            }
        });
    }

    private void setTable() {
        //普通列
        Column<String> column1 = new Column<>("车牌号", "plateNumber");
        column1.setFixed(true);
        Column<Integer> column2 = new Column<>("存放地点", "projectName");
        Column<Long> column3 = new Column<>("车辆类型", "vehicleTypeName");
        Column<String> column4 = new Column<>("年检日期", "AVEDate");
        Column<String> column5 = new Column<>("保险购买日期", "IPDate");
        Column<String> column6 = new Column<>("识别码", "VIN");
        Column<String> column7 = new Column<>("发动机号", "engineNumber");
        //表格数据 datas是需要填充的数据
        TableData<CarInfoEntity> tableData = new TableData<>("设备", carInfoEntities, column1, column2, column3, column4, column5, column6, column7);
        //table.setZoom(true,3);是否缩放
        smartTable.setTableData(tableData);
    }
}
