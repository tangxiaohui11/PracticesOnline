package net.lzzy.practicesonline.activities.activities.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.models.QuestionResult;
import net.lzzy.practicesonline.activities.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.activities.models.WrongType;
import net.lzzy.practicesonline.activities.activities.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/5/13
 * Description:
 */
public class ChartFragment extends BaseFragment {
    public static final String ARGS_RESULT = "result";
    List<QuestionResult> results;
    private OnChartSkipListener listener;
    private PieChart pChart;
    private LineChart lChart;
    private BarChart bChart;
    private Chart[] charts;
    private String[] titles =new String[]{"正确错误比例（单位%）","题目阅读量统计","题目错误类型统计"};
    private int rightCount =0;
    private float touchX1;
    private View[] dots;
    private int chartIndex=0;
    private static final int MIN_DISTANCE = 100;



    /** 静态方法传参数 **/
    public static ChartFragment newInstance(List<QuestionResult> results){
        ChartFragment fragment=new ChartFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(ARGS_RESULT, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results = getArguments().getParcelableArrayList(ARGS_RESULT);
        }
    }

    @Override
    protected void populate() {
        TextView tvView=find(R.id.fragment_chart_tv_view);
        tvView.setOnClickListener(v -> listener.gotoGrid());
        for (QuestionResult questionResult:results){
            if (questionResult.isRight()){
                rightCount++;
            }
        }
        initCharts();
        configPieChart();
        displayPieChart();
        pChart.setVisibility(View.VISIBLE);

        configBarLineChart(bChart);


        configBarLineChart(lChart);
        configBarLineChart(bChart);

        /** 折线图 **/
        displayLineChart();

        /** 柱状图 **/
        displayBarChart();

        //region  导航点滑动切换
        View dot1=find(R.id.fragment_chart_dot1);
        View dot2=find(R.id.fragment_chart_dot2);
        View dot3=find(R.id.fragment_chart_dot3);
        dots = new View[]{dot1,dot2,dot3};
        find(R.id.fragment_chart_container).setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
            @Override
            public boolean handleTouch(MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    touchX1 = event.getX();
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    float touchX2=event.getX();
                    if (Math.abs(touchX2-touchX1)>MIN_DISTANCE){
                        if (touchX2<touchX1){
                            if (chartIndex<charts.length-1){
                                chartIndex++;
                            }else {
                                chartIndex=0;
                            }
                        }else {
                            if (chartIndex>0){
                                chartIndex--;
                            }else {
                                chartIndex=charts.length-1;
                            }
                        }
                        switchChart();
                    }
                }
                return true;
            }

            private void switchChart() {
                for (int i=0;i<charts.length;i++){
                    if (chartIndex==i){
                        charts[i].setVisibility(View.VISIBLE);
                        dots[i].setBackgroundResource(R.drawable.dot_fill_style);
                    }else {
                        charts[i].setVisibility(View.GONE);
                        dots[i].setBackgroundResource(R.drawable.dot_style);
                    }
                }
            }
        });
        //endregion



    }



    private void displayLineChart(){
        ValueFormatter xFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Q."+(int)value;

            }
        };
        lChart.getXAxis().setValueFormatter(xFormatter);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0;i<results.size();i++){
            int times= UserCookies.getInstance().getReadCount(results.get(i).getQuestionId().toString());
            entries.add(new Entry(i+1,times));
        }

        LineDataSet dataSet=new LineDataSet(entries,"查看次数");
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setValueTextSize(9f);
        LineData data = new LineData(dataSet);
        lChart.setData(data);
        lChart.invalidate();
    }

    private void displayBarChart(){
        ValueFormatter xFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return WrongType.getInstance((int)value).toString();
            }
        };
        bChart.getXAxis().setValueFormatter(xFormatter);


        int right=0,miss=0,extra=0,wrong=0;
        for (QuestionResult questionResults:results){
            switch (questionResults.getType()){
                case RIGHT_OPTIONS:
                    right++;
                    break;
                case MISS_OPTIONS:
                    miss++;
                    break;
                case EXTTRA_OPTIONS:
                    extra++;
                    break;
                case WRONG_OPTIONS:
                    wrong++;
                    break;
                default:
                    break;
            }
        }
        List<BarEntry> entries=new ArrayList<>();
        entries.add(new BarEntry(0,right));
        entries.add(new BarEntry(1,miss));
        entries.add(new BarEntry(2,extra));
        entries.add(new BarEntry(3,wrong));

        BarDataSet dataSet=new BarDataSet(entries,"查看类型");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        bChart.setData(data);
        bChart.invalidate();

    }

    private void configBarLineChart(BarLineChartBase chart) {
        /**  X 轴 **/
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8f);
        xAxis.setGranularity(1f);

        /**  Y 轴 **/
        YAxis yAxis=chart.getAxisLeft();
        yAxis.setLabelCount(8,false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setTextSize(8f);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0);

        /** chart属性 **/
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPinchZoom(false);


        // // Create Limit Lines // //
        LimitLine llXAxis = new LimitLine(9f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);


        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(0f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);


        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(false);

        // add limit lines
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);
        //xAxis.addLimitLine(llXAxis);


    }

    private void displayPieChart() {
        List<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry((float) rightCount /results.size(),"正确"));
        entries.add(new PieEntry((float)( results.size()-rightCount)/results.size(),"错误"));
        PieDataSet dataSet = new PieDataSet(entries, "Right And Wrong ");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.DKGRAY);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();


    }

    private void configPieChart() {
        pChart.setUsePercentValues(true);
        pChart.setDrawHoleEnabled(false);
        pChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    }

    private void initCharts() {
        pChart = find(R.id.fragment_chart_pie);
        lChart = find(R.id.fragment_char_line);
        bChart = find(R.id.fragment_chart_bar);
        charts = new Chart[]{pChart,lChart,bChart};
        int i=0;
        for (Chart chart:charts){
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc = new Description();
            desc.setText(titles[i]);
            chart.setDescription(desc);
            chart.setNoDataText("数据获取中.....");
            chart.setExtraOffsets(5, 10, 5, 5);
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chart;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnChartSkipListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnChartSkipListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }


    public interface OnChartSkipListener {
        /**
         * 跳转到GridFragment
         */
        void gotoGrid();
    }

}
