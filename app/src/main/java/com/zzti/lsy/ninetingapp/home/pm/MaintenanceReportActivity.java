package com.zzti.lsy.ninetingapp.home.pm;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.DeviceEntity;
import com.zzti.lsy.ninetingapp.home.machinery.MaintenanceRecordActivity;
import com.zzti.lsy.ninetingapp.home.production.FormListActivity;
import com.zzti.lsy.ninetingapp.utils.DateUtil;
import com.zzti.lsy.ninetingapp.utils.DensityUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 维修报表
 */
public class MaintenanceReportActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.table)
    SmartTable mSmartTable;


    @Override
    public int getContentViewId() {
        return R.layout.activity_maintenance_report;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCarNumber("豫A5555" + i);
            deviceEntity.setAddress("存放地点" + i);
            deviceEntity.setCarType("罐车" + i);
            deviceEntity.setReason("原因" + i);
            deviceEntity.setParts("配件" + i);
            deviceEntities.add(deviceEntity);
        }

        //普通列
        Column<String> column1 = new Column<>("车牌号", "carNumber");
        column1.setFixed(true);
        Column<Integer> column2 = new Column<>("存放地点", "address");
        Column<Long> column3 = new Column<>("车辆类型", "carType");
        Column<String> column4 = new Column<>("维修内容", "reason");
        Column<String> column5 = new Column<>("配件", "parts");
        //表格数据 datas是需要填充的数据
        TableData<DeviceEntity> tableData = new TableData<>("设备", deviceEntities, column1, column2, column3, column4, column5);
        //table.setZoom(true,3);是否缩放
        mSmartTable.setTableData(tableData);
    }

    private void initView() {
        setTitle("表格查看");
        ivToolbarMenu.setVisibility(View.VISIBLE);
        ivToolbarMenu.setOnClickListener(this);

        TableConfig tableConfig = mSmartTable.getConfig();
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

    @Override
    public void onClick(View view) {
        showCustomTime();
    }

    /**
     * 显示时间选择器
     */
    private void showCustomTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(DateUtil.getCurYear(), DateUtil.getCurMonth(), DateUtil.getCurDay());
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(MaintenanceReportActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //TODO  查询数据

            }
        }).setDate(instance).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel(" 年", " 月", " 日", "", "", "")
                .isCenterLabel(false).build();
        pvTime.show();

    }

    @OnClick(R.id.iv_search)
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (StringUtil.isNullOrEmpty(etSearch.getText().toString())) {
                    UIUtils.showT("车牌号不能为空");
                    break;
                }
                //TODO
                break;
        }
    }
}
