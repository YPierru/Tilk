package com.tilk.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by YPierru on 12/01/2017.
 */

public class ChartBuilder {

    private LineChart chart;


    public ChartBuilder(LineChart chart) {
        this.chart = chart;
    }

    private void commonBuild() {
        //Chart general settings
        chart.setBackgroundColor(Color.WHITE);
        chart.setDescription(null);
        chart.getAxisRight().setEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        //General YAxis (L) settings
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0);

        //General XAxis (Time) settings
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
    }

    private LineDataSet commonLineDataSetBuild(LineDataSet lineDataSet){

        lineDataSet.setColor(Color.rgb(25,118,210));
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        return lineDataSet;
    }


    public LineChart buildGraphDay(ArrayList<Entry> listEntry){

        commonBuild();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(500);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(1440);
        xAxis.setGranularity(60f);
        xAxis.setValueFormatter(getFormatterDay());

        LineDataSet lineDataSet = new LineDataSet(listEntry, "Aujourd'hui");
        lineDataSet = commonLineDataSetBuild(lineDataSet);

        LineData lineData = new LineData(lineDataSet);

        chart.setData(lineData);
        refresh();

        return chart;
    }

    public void addDayEntry(Entry entry){
        if(chart.getLineData()!=null) {
            chart.getLineData().addEntry(entry, 0);
            refresh();
        }
    }

    public void refresh(){
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    private IAxisValueFormatter getFormatterDay(){
        //We only display hours, even if the XAxis use minutes
        final String[] quarters = new String[25];

        for(int i=0;i<quarters.length;i++){
            quarters[i]=i+"h";
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value/60];
            }

        };

        return formatter;
    }

    public LineChart getChart() {
        return chart;
    }
}
