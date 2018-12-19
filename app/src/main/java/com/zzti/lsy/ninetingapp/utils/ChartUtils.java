package com.zzti.lsy.ninetingapp.utils;

import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.view.MyMarkerView;

import java.util.ArrayList;
import java.util.List;

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
    public static LineChart initChart(LineChart mLineChart, int type, int count, int color) {
        //配置基本信息
        mLineChart.setNoDataText("没有数据");//没有数据时显示的文字
        mLineChart.setNoDataTextColor(color);//没有数据时显示文字的颜色
        mLineChart.getDescription().setEnabled(false);   //设置描述
        mLineChart.setTouchEnabled(true);    //设置是否可以触摸
        mLineChart.setBorderColor(color); //设置 chart 边框线的颜色。
        mLineChart.setDragDecelerationFrictionCoef(0.9f);    //设置滚动时的速度快慢
        mLineChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        mLineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(true);   //能否拖拽
        mLineChart.setScaleEnabled(true);  //能否缩放
        mLineChart.animateX(1000);//绘制动画 从左到右
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）

        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        //xAxis.setLabelRotationAngle(25f);  //设置旋转偏移量
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //设置X轴的位置
        //设置标签文本格式
        xAxis.setTextSize(10f);
        //设置标签文本颜色
        xAxis.setTextColor(color);
        xAxis.setAxisLineColor(color);//设置x轴线颜色
        //是否绘制轴线
        xAxis.setDrawAxisLine(true);
        //是否绘制网格线
        xAxis.setDrawGridLines(false);
        //设置是否一个格子显示一条数据，如果不设置这个属性，就会导致X轴数据重复并且错乱的问题
        xAxis.setGranularity(1f);
        xAxis.setAvoidFirstLastClipping(false);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘


        //配置Y轴信息
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);    //设置左边的网格线显示
        leftAxis.setGranularityEnabled(false);//启用在放大时限制y轴间隔的粒度特性。默认值：false。
        leftAxis.setTextColor(color); //设置Y轴文字颜色
        leftAxis.enableGridDashedLine(10f, 10f, 0f);  //设置Y轴网格线条的虚线，参1 实线长度，参2 虚线长度 ，参3 周期
        leftAxis.setLabelCount(10, false);
        leftAxis.setEnabled(true);     //设置是否使用 Y轴左边的
        leftAxis.setGridColor(color);  //网格线条的颜色
        leftAxis.setAxisLineColor(color); //设置Y轴颜色

        YAxis rightAxis = mLineChart.getAxisRight(); //获取右边的Y轴
        rightAxis.setEnabled(false);   //设置右边的Y轴不显示

        //设置图例（也就是曲线的标签）
        Legend legend = mLineChart.getLegend();//设置比例图
        legend.setEnabled(false);   //因为自带的图例太丑，而且操作也不方便，楼主选择自定义，设置不显示比例图


        MyMarkerView mv = new MyMarkerView(App.get(),
                R.layout.custom_marker_view, color);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv);

        return mLineChart;

    }

    public static void setLineChartDate(LineChart mLineChart, final List<String> xData, List<String> yData, int color) {
        List<Entry> mValues = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            Entry entry = new Entry(i, Float.valueOf(yData.get(i)), xData.get(i));
            mValues.add(entry);
        }
        //判断图表中原来是否有数据
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            if (mValues.size() == 0) {
                lineDataSet = new LineDataSet(mValues, "数据");
            } else {
                //获取数据1
                lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            }
            lineDataSet.setValues(mValues);
            //刷新数据
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            //格式化显示数据
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setValueFormatter(new LargeValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    //对X轴上的值进行Format格式化，转成相应的值
                    Log.d("22222", "----->getFormattedValue: " + value);
                    int intValue = (int) value;
                    //筛选出自己需要的值，一般都是这样写没问题，并且一定要加上这个判断，不然会出错
                    if (xData.size() > intValue && intValue >= 0) {
                        return xData.get(intValue);
                    } else {
                        return "";
                    }
                }
            });
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            //设置折线的属性
            lineDataSet = new LineDataSet(mValues, "数据");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);   //设置左右两边Y轴节点描述
            lineDataSet.setValueTextColor(color); //设置节点文字颜色
            lineDataSet.setDrawCircles(true);  //设置是否显示节点的小圆点
            lineDataSet.setDrawValues(false);       //设置是否显示节点的值
            lineDataSet.setHighLightColor(color);//当点击节点时，将会出现与节点水平和垂直的两条线，可以对其进行定制.此方法为设置线条颜色
            lineDataSet.setHighlightEnabled(true);//设置是否显示十字线
            lineDataSet.setColor(color);    //设置线条颜色
            lineDataSet.setCircleColor(color);  //设置节点的圆圈颜色
            lineDataSet.setLineWidth(1f);   //设置线条宽度
            lineDataSet.setCircleRadius(2f);//设置每个坐标点的圆大小
            lineDataSet.setHighlightLineWidth(0.5f);//设置点击交点后显示高亮线宽
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            lineDataSet.setDrawCircleHole(false);//是否定制节点圆心的颜色，若为false，则节点为单一的同色点，若为true则可以设置节点圆心的颜色
            lineDataSet.setValueTextSize(12f);   //设置 DataSets 数据对象包含的数据的值文本的大小（单位是dp）。
            //设置折线图填充
            lineDataSet.setDrawFilled(true);    //Fill填充，可以将折线图以下部分用颜色填满
            lineDataSet.setFillAlpha(65);       ////设置填充区域透明度，默认值为85
            lineDataSet.setFillColor(ColorTemplate.getHoloBlue());//设置填充颜色
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormSize(15.f);
            dataSets.add(lineDataSet);
            LineData data = new LineData(dataSets);
            mLineChart.setData(data);    //添加数据

        }
    }
}
