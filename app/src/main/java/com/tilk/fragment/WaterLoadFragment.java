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
import com.tilk.models.WaterLoad;
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

    private WaterLoad waterLoad;
    private WaterLoadMonitor waterLoadMonitor;

    private TextView tvDebitValue;
    private TextView tvStatDay;
    private TextView tvStatWeek;
    private TextView tvStatMonth;
    private TextView tvStatYear;


    public static WaterLoadFragment newInstance(WaterLoad waterLoad){
        WaterLoadFragment waterLoadFragment = new WaterLoadFragment();

        Bundle args = new Bundle();
        args.putSerializable("waterload",waterLoad);
        waterLoadFragment.setArguments(args);

        return waterLoadFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waterLoad = (WaterLoad)getArguments().getSerializable("waterload");
    }

    public void startMonitor(){
        if(waterLoadMonitor==null) {
            Logger.logI("********************* START MONITORING " + waterLoad.getName() + " *********************");
            waterLoadMonitor = new WaterLoadMonitor();
            waterLoadMonitor.startMonitor();
        }
    }

    public void stopMonitor(){
        if(waterLoadMonitor!=null) {
            Logger.logI("********************* STOP MONITORING "+waterLoad.getName()+" *********************");
            waterLoadMonitor.stopMonitor();
            waterLoadMonitor=null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startMonitor();
        //Logger.logI("bonjour, de "+waterLoad.getName());

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
        tvStatDay = (TextView)getView().findViewById(R.id.tv_conso_jour_value);
        tvStatWeek = (TextView)getView().findViewById(R.id.tv_conso_hebdo_value);
        tvStatMonth = (TextView)getView().findViewById(R.id.tv_conso_mois_value);
        tvStatYear = (TextView)getView().findViewById(R.id.tv_conso_annee_value);

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
            Logger.logI("je monitor le flux du poste "+waterLoad.getName());

            try {
                String received=HttpPostManager.sendPost("load_id="+waterLoad.getId(), Constants.URL_GET_CURRENTFLOW);
                //Logger.logI(received);
                JSONObject jsonObject = new JSONObject(received);

                waterLoad.setCurrentFlow(jsonObject.getInt("current_flow"));
                waterLoad.setStatDay(jsonObject.getInt("per_day"));
                waterLoad.setStatWeek(jsonObject.getInt("per_week"));
                waterLoad.setStatMonth(jsonObject.getInt("per_month"));
                waterLoad.setStatYear(jsonObject.getInt("per_year"));


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDebitValue.setText(String.valueOf(waterLoad.getCurrentFlow()));
                        tvStatDay.setText(String.valueOf(waterLoad.getStatDay()));
                        tvStatWeek.setText(String.valueOf(waterLoad.getStatWeek()));
                        tvStatMonth.setText(String.valueOf(waterLoad.getStatMonth()));
                        tvStatYear.setText(String.valueOf(waterLoad.getStatYear()));
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
