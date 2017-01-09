package com.tilk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tilk.R;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by YPierru on 03/01/2017.
 */

public class WaterLoadFragment extends Fragment {

    private String name;
    private WaterLoadMonitor waterLoadMonitor;
    private int currentFlow;

    private TextView tvDebitValue;

    public static WaterLoadFragment newInstance(String name){
        WaterLoadFragment waterLoadFragment = new WaterLoadFragment();

        Bundle args = new Bundle();
        args.putString("load_name",name);
        waterLoadFragment.setArguments(args);

        return waterLoadFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("load_name");

    }

    public void startMonitor(){
        Logger.logI("********************* START MONITORING *********************");
        waterLoadMonitor = new WaterLoadMonitor();
        waterLoadMonitor.startMonitor();
    }

    public void stopMonitor(){
        Logger.logI("********************* STOP MONITORING *********************");
        if(waterLoadMonitor!=null) {
            waterLoadMonitor.stopMonitor();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMonitor();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poste, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tvDebitValue = (TextView)getView().findViewById(R.id.tv_debit_value);

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

    private class WaterLoadMonitor implements Runnable{

        private ScheduledExecutorService executor;

        public WaterLoadMonitor(){
            executor = Executors.newScheduledThreadPool(1);
        }

        public void startMonitor(){
            executor.scheduleAtFixedRate(this, 0, 2, TimeUnit.SECONDS);
        }

        public void stopMonitor(){
            executor.shutdown();
        }

        @Override
        public void run() {
            Logger.logI("je monitor le flux du poste "+name);

            try {
                String received=HttpPostManager.sendPost("load="+name, Constants.URL_GET_CURRENTFLOW);

                JSONObject jsonObject = new JSONObject(received);

                currentFlow = jsonObject.getInt("current_flow");


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDebitValue.setText(""+currentFlow);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
