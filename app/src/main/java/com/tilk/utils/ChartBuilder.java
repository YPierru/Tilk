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
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        return lineDataSet;
    }

    private LineDataSet commonLineDataSetCurrentBuild(LineDataSet lineDataSet){

        lineDataSet.setColor(Color.rgb(25,118,210));

        return commonLineDataSetBuild(lineDataSet);
    }

    private LineDataSet commonLineDataSetPreviousBuild(LineDataSet lineDataSet){

        lineDataSet.setColor(Color.rgb(99,207,241));

        return commonLineDataSetBuild(lineDataSet);
    }

    public LineChart buildGraph(ArrayList<Entry> listEntry, ArrayList<Entry> listEntryPrevious, EStatsTypes statsTypes){
        commonBuild();
        int xMax=0,yMax=0;
        float xGranularity=0;
        IAxisValueFormatter formatter=null;
        String labelCurrent=null,labelPrevious=null;

        if(statsTypes==EStatsTypes.day){
            xMax=1440;
            yMax=500;
            xGranularity=60f;
            formatter = getFormatterDay();
            labelCurrent="Aujourd'hui";
            labelPrevious="Hier";
        }else if(statsTypes==EStatsTypes.week){
            xMax=168;
            yMax=1000;
            xGranularity=24f;
            formatter = getFormatterWeek();
            labelCurrent="Cette semaine";
            labelPrevious="Semaine dernière";
        }else if(statsTypes==EStatsTypes.month){
            xMax=DateTimeUtils.getAmountOf6hInCurrentMonth();
            yMax=3000;
            xGranularity=6f;
            formatter = getFormatterMonth();
            labelCurrent=DateTimeUtils.getCurrentMonthName();
            labelPrevious=DateTimeUtils.getPreviousMonthName();
        }else if(statsTypes==EStatsTypes.year){
            xMax=DateTimeUtils.getNumberOfDayInYear();
            yMax=15000;
            xGranularity=60f;
            formatter = getFormatterYear();
            labelCurrent=DateTimeUtils.getCurrentYear();
            labelPrevious=DateTimeUtils.getPreviousYear();
        }


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(yMax);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(xMax);
        xAxis.setGranularity(xGranularity);
        xAxis.setValueFormatter(formatter);

        LineDataSet lineDataSet = new LineDataSet(listEntry, labelCurrent);
        LineDataSet lineDataSetPrevious = new LineDataSet(listEntryPrevious, labelPrevious);

        lineDataSet = commonLineDataSetCurrentBuild(lineDataSet);
        lineDataSetPrevious = commonLineDataSetPreviousBuild(lineDataSetPrevious);

        LineData lineData = new LineData(lineDataSetPrevious);
        lineData.addDataSet(lineDataSet);

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
