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

    private final int Y_MAX_DAY=500;
    private final int X_MAX_DAY=1440;
    private final float X_GRANULARITY_DAY=60f;

    private final int Y_MAX_WEEK=1000;
    private final int X_MAX_WEEK=168;
    private final float X_GRANULARITY_WEEK=24f;

    private final int Y_MAX_MONTH=3000;
    private final int X_MAX_MONTH=DateTimeUtils.getAmountOf6hInCurrentMonth();
    private final float X_GRANULARITY_MONTH=6f;

    private final int Y_MAX_YEAR=15000;
    private final int X_MAX_YEAR=DateTimeUtils.getNumberOfDayInYear();
    private final float X_GRANULARITY_YEAR=60f;


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

        /*for(Entry entry : listEntry){
            Logger.logI(""+entry);
        }*/


        commonBuild();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(Y_MAX_DAY);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(X_MAX_DAY);
        xAxis.setGranularity(X_GRANULARITY_DAY);
        xAxis.setValueFormatter(getFormatterDay());

        LineDataSet lineDataSet = new LineDataSet(listEntry, "Aujourd'hui");
        lineDataSet = commonLineDataSetBuild(lineDataSet);

        LineData lineData = new LineData(lineDataSet);

        chart.setData(lineData);
        refresh();

        return chart;
    }


    public LineChart buildGraphWeek(ArrayList<Entry> listEntry){

        commonBuild();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(Y_MAX_WEEK);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(X_MAX_WEEK);
        xAxis.setGranularity(X_GRANULARITY_WEEK);
        xAxis.setValueFormatter(getFormatterWeek());

        LineDataSet lineDataSet = new LineDataSet(listEntry, "Cette semaine");
        lineDataSet = commonLineDataSetBuild(lineDataSet);

        LineData lineData = new LineData(lineDataSet);

        chart.setData(lineData);
        refresh();

        return chart;
    }


    public LineChart buildGraphMonth(ArrayList<Entry> listEntry){

        commonBuild();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(Y_MAX_MONTH);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(X_MAX_MONTH);
        xAxis.setGranularity(X_GRANULARITY_MONTH);
        xAxis.setValueFormatter(getFormatterMonth());

        LineDataSet lineDataSet = new LineDataSet(listEntry, DateTimeUtils.getCurrentMonthName());
        lineDataSet = commonLineDataSetBuild(lineDataSet);

        LineData lineData = new LineData(lineDataSet);

        chart.setData(lineData);
        refresh();

        return chart;
    }


    public LineChart buildGraphYear(ArrayList<Entry> listEntry){

        commonBuild();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(Y_MAX_YEAR);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(X_MAX_YEAR);
        xAxis.setGranularity(X_GRANULARITY_YEAR);
        xAxis.setValueFormatter(getFormatterYear());

        LineDataSet lineDataSet = new LineDataSet(listEntry, DateTimeUtils.getCurrentYear());
        lineDataSet = commonLineDataSetBuild(lineDataSet);

        LineData lineData = new LineData(lineDataSet);

        chart.setData(lineData);
        refresh();

        return chart;
    }

    public void addEntry(Entry entry){
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
                //return "";
            }

        };

        return formatter;
    }


    private IAxisValueFormatter getFormatterWeek(){
        //We only display days, even if the XAxis use minutes
        final String[] quarters = new String[7];
        quarters[0]="L.";
        quarters[1]="Ma.";
        quarters[2]="Me.";
        quarters[3]="J.";
        quarters[4]="V.";
        quarters[5]="S.";
        quarters[6]="D.";

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                /*Logger.logI("COUCOU !!");
                Logger.logI(""+value);
                return "";*/
                return quarters[(int) value/24];
            }

        };

        return formatter;
    }

    private IAxisValueFormatter getFormatterMonth(){
        //We only display days, even if the XAxis use minutes
        final String[] quarters = new String[7];

        String month = DateTimeUtils.getCurrentMonth();

        quarters[0]="01/"+month;
        quarters[1]="05/"+month;
        quarters[2]="09/"+month;
        quarters[3]="14/"+month;
        quarters[4]="19/"+month;
        quarters[5]="24/"+month;
        quarters[6]="29/"+month;

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value/18];
            }

        };

        return formatter;
    }

    private IAxisValueFormatter getFormatterYear(){
        //We only display days, even if the XAxis use minutes
        final String[] quarters = new String[6];

        quarters[0]="Jan.";
        quarters[1]="Mars";
        quarters[2]="Mai";
        quarters[3]="Juil.";
        quarters[4]="Sep.";
        quarters[5]="Nov.";


        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                /*Logger.logI("COUCOU !!");
                Logger.logI(""+value);
                return "";*/
                return quarters[(int) value/(30*2)];
            }

        };

        return formatter;
    }
}
