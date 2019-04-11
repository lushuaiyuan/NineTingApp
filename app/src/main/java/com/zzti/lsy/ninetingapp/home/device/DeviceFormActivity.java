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
                            String dLDate = carInfoEntity.getDLDate().split("T")[0];
                            carInfoEntity.setDLDate(dLDate);
                            String registerTime = carInfoEntity.getRegisterTime().split("T")[0];
                            carInfoEntity.setRegisterTime(registerTime);
                            String yearTime = carInfoEntity.getRegisterTime().split("T")[0];
                            carInfoEntity.setYearTime(yearTime);
                            String qStartTime = carInfoEntity.getqStartTime().split("T")[0];
                            carInfoEntity.setqStartTime(qStartTime);
                            String qOverTime = carInfoEntity.getqOverTime().split("T")[0];
                            carInfoEntity.setqOverTime(qOverTime);
                            String sStartTime = carInfoEntity.getsStartTime().split("T")[0];
                            carInfoEntity.setsStartTime(sStartTime);
                            String sOverTime = carInfoEntity.getsOverTime().split("T")[0];
                            carInfoEntity.setsOverTime(sOverTime);
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
        Column<Integer> column2 = new Column<>("项目部", "projectName");
        Column<String> column3 = new Column<>("汽车类型", "vehicleTypeName");
        Column<String> column4 = new Column<>("排放量名称", "dischargeName");
        Column<Long> column5 = new Column<>("车辆来源", "CarSource");
        Column<String> column6 = new Column<>("新旧程度", "oldLevel");
        Column<String> column7 = new Column<>("行驶证保存情况", "drivingStatus");
        Column<String> column8 = new Column<>("识别码", "VIN");
        Column<String> column9 = new Column<>("发动机编号", "engineNumber");
        Column<String> column10 = new Column<>("行驶证发放日期", "DLDate");
        Column<String> column11 = new Column<>("登记日期", "registerTime");
        Column<String> column12 = new Column<>("年检日期", "yearTime");
        Column<String> column13 = new Column<>("车辆所属项目部ID", "projectID");
        Column<String> column14 = new Column<>("年检时限", "yearExprie");
        Column<String> column15 = new Column<>("强制保险生效时间", "qStartTime");
        Column<String> column16 = new Column<>("强制保险到期时间", "qOverTime");
        Column<String> column17 = new Column<>("强制保险公司", "qCompany");
        Column<String> column18 = new Column<>("强险保单原件所在地", "qAddress");
        Column<String> column19 = new Column<>("商业保险生效日期", "sStartTime");
        Column<String> column20 = new Column<>("商业保险到期日期", "sOverTime");
        Column<String> column21 = new Column<>("商业保险公司", "sCompany");
        Column<String> column22 = new Column<>("商险保单原件所在地", "sAddress");

        //表格数据 datas是需要填充的数据
        TableData<CarInfoEntity> tableData = new TableData<>("设备", carInfoEntities, column1, column2, column3, column4, column5, column6, column7, column8,
                column9, column10, column11, column12, column13, column14, column15, column16, column17, column18, column19, column20, column21,column22);
        //table.setZoom(true,3);是否缩放
        smartTable.setTableData(tableData);
    }
}
