package com.zzti.lsy.ninetingapp.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.view.MyMarkerView;

import java.util.List;

import static com.zzti.lsy.ninetingapp.utils.DateUtil.getCurrentDay;

/**
 * 工具类
 */
public class ChartUtils {
    public static final int oneCar = 1;
    public static final int allCar = 2;

    /**
     * 初始化图表
     *
     * @param mLineChart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart mLineChart, int type, int count) {
        mLineChart.setLogEnabled(true);//打印日志
        //取消描述文字
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setNoDataText("没有数据");//没有数据时显示的文字
        mLineChart.setNoDataTextColor(Color.WHITE);//没有数据时显示文字的颜色
        mLineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        mLineChart.setDrawBorders(false);//是否禁止绘制图表边框的线
        mLineChart.setBorderColor(Color.WHITE); //设置 chart 边框线的颜色。
        mLineChart.setBorderWidth(1f); //设置 chart 边界线的宽度，单位 dp。
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(true);   //能否拖拽
        mLineChart.setScaleEnabled(true);  //能否缩放
        mLineChart.animateX(1000);//绘制动画 从左到右
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）


        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setTextSize(12f);//设置文字大小
        if (type == allCar) {
            xAxis.setAxisMaximum(12f);//设置最大值 //
        }
        xAxis.setLabelCount(count);  //设置X轴的显示个数
        xAxis.setAxisMinimum(0f);//设置x轴的最小值 //`
        mLineChart.setDragEnabled(true);   //能否拖拽
        mLineChart.setScaleEnabled(true);  //能否缩放
        xAxis.setAvoidFirstLastClipping(false);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘

        xAxis.setTextColor(Color.WHITE);//设置字体颜色
        xAxis.setAxisLineColor(Color.WHITE);//设置x轴线颜色
        xAxis.setAxisLineWidth(1.5f);//设置x轴线宽度
        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis axisRight = mLineChart.getAxisRight();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);  //设置Y轴网格线条的虚线，参1 实线长度，参2 虚线长度 ，参3 周期
        leftAxis.setLabelCount(10, false);
        leftAxis.setAxisMinimum(0);
        axisRight.setEnabled(false);   //设置是否使用 Y轴右边的
        leftAxis.setEnabled(true);     //设置是否使用 Y轴左边的
        leftAxis.setGridColor(Color.parseColor("#7189a9"));  //网格线条的颜色
        leftAxis.setDrawLabels(true);        //是否显示Y轴刻度
        leftAxis.setDrawGridLines(true);      //是否使用 Y轴网格线条
        leftAxis.setTextSize(12f);            //设置Y轴刻度字体
        leftAxis.setTextColor(Color.WHITE);   //设置字体颜色
        leftAxis.setAxisLineColor(Color.WHITE); //设置Y轴颜色
        leftAxis.setAxisLineWidth(1.5f);
        leftAxis.setDrawAxisLine(true);//是否绘制轴线
        leftAxis.setMinWidth(0f);
        leftAxis.setMaxWidth(200f);
        leftAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        Legend l = mLineChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例

        mLineChart.invalidate();


        MyMarkerView mv = new MyMarkerView(App.get(),
                R.layout.custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv);

        return mLineChart;


    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, "");
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#FF66C1"));
            // 线宽
            lineDataSet.setLineWidth(1.75f);
            // 设置平滑曲线
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // 不显示坐标点的小圆点
            lineDataSet.setDrawCircles(false);
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(false);
            // 不显示定位线
            lineDataSet.setHighlightEnabled(true);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
//    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
//                                            final int valueType) {
//        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xValuesProcess(valueType)[(int) value];
//            }
//        });
//
//        chart.invalidate();
//        setChartData(chart, values);
//    }

    /**
     * x轴数据处理
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
//    private static String[] xValuesProcess(int valueType) {
//        if (valueType == monthValue) { // 本月
//            String[] monthValues = new String[DateUtil.getCurrentDay()];
//            long currentTime = System.currentTimeMillis();
//            if (monthValues.length == 1) {//当月第一天
//                monthValues[0] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_month);
//            } else {
//                for (int i = monthValues.length - 1; i >= 0; i--) {
//                    monthValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_month);
//                    currentTime -= (24 * 60 * 60 * 1000);
//                }
//            }
//            return monthValues;
//        }
//        else if (valueType == quarterValue) { // 本季度
//            String[] monthValues = new String[3];
//            long currentTime = System.currentTimeMillis();
//            for (int i = 2; i >= 0; i--) {
//                monthValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_quarter);
//                currentTime -= (4 * 24 * 60 * 60 * 1000);
//            }
//            return monthValues;
//        }
//        return new String[]{};
//    }
}
