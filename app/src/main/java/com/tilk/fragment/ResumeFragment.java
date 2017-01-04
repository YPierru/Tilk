package com.tilk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tilk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YPierru on 03/01/2017.
 */

public class ResumeFragment extends Fragment{

    private IAxisValueFormatter formatter;
    private String[] quarters = new String[] { "0","1","2","Mars","4","5","Juin","7","8","Sept.","10","11", "Déc." };


    public ResumeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resume, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        LineChart chart = (LineChart) getView().findViewById(R.id.chart_resume_conso);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDescription(null);
        chart.getAxisRight().setEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);


        YAxis yAxis=chart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);

        // the labels that should be drawn on the XAxis
        formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }

        };

        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(3f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);


        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1,20));
        entries.add(new Entry(2,35));
        entries.add(new Entry(3,50));
        entries.add(new Entry(4,70));
        entries.add(new Entry(5,85));
        entries.add(new Entry(6,100));
        entries.add(new Entry(7,120));
        entries.add(new Entry(8,140));
        entries.add(new Entry(9,165));
        entries.add(new Entry(10,185));
        entries.add(new Entry(11,200));
        entries.add(new Entry(12,220));


        LineDataSet dataSet = new LineDataSet(entries, "2015");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.RED);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setLineWidth(3f);


        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(1,10));
        entries2.add(new Entry(2,45));
        entries2.add(new Entry(3,60));
        entries2.add(new Entry(4,80));
        entries2.add(new Entry(5,95));
        entries2.add(new Entry(6,105));
        entries2.add(new Entry(7,110));
        entries2.add(new Entry(8,130));
        entries2.add(new Entry(9,155));
        entries2.add(new Entry(10,170));
        entries2.add(new Entry(11,190));
        entries2.add(new Entry(12,205));


        LineDataSet dataSet2 = new LineDataSet(entries2, "2016");
        dataSet2.setColor(Color.BLUE);
        dataSet2.setValueTextColor(Color.BLUE);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawValues(false);
        dataSet2.setHighlightEnabled(false);
        dataSet2.setLineWidth(3f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);

        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);

        chart.invalidate();
    }
}
