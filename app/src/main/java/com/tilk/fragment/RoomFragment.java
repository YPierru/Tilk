package com.tilk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tilk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YPierru on 03/01/2017.
 */

public class RoomFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }



    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        LineChart chart = (LineChart) getView().findViewById(R.id.chart_evolution);
        List<Entry> entries = new ArrayList<>();


        entries.add(new Entry(0,0));
        entries.add(new Entry(1,1));
        entries.add(new Entry(2,2));
        entries.add(new Entry(3,2));

        LineDataSet dataSet = new LineDataSet(entries, "Evolution");
        dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}
