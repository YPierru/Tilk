package com.tilk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.tilk.models.WaterLoad;
import com.tilk.utils.Constants;
import com.tilk.utils.DateTimeUtils;
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
    private LineChart chart;



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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poste, container, false);
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
        waterLoad.retrieveHistoric(getActivity());
        if(chart!=null){
            buildChart();
        }
        //Logger.logI("bonjour, de "+waterLoad.getName());

    }

    @Override
    public void onPause() {
        super.onPause();
        stopMonitor();
        waterLoad.saveHistoric(getActivity());
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tvDebitValue = (TextView)getView().findViewById(R.id.tv_debit_value);
        tvStatDay = (TextView)getView().findViewById(R.id.tv_conso_jour_value);
        tvStatWeek = (TextView)getView().findViewById(R.id.tv_conso_hebdo_value);
        tvStatMonth = (TextView)getView().findViewById(R.id.tv_conso_mois_value);
        tvStatYear = (TextView)getView().findViewById(R.id.tv_conso_annee_value);
        chart = (LineChart) getView().findViewById(R.id.chart_evolution);

    }

    private void buildChart(){
        //Chart general settings
        chart.setBackgroundColor(Color.WHITE);
        chart.setDescription(null);
        chart.getAxisRight().setEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);


        //General YAxis (L) settings
        YAxis yAxis=chart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(500);

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

        /*final String[] quarters = new String[61];

        for(int i=0;i<quarters.length;i++){
            quarters[i]=i+"mn";
        }
        quarters[60]="1h";

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }

        };*/

        //General XAxis settings
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(1440);
        //xAxis.setAxisMaximum(60);
        xAxis.setGranularity(60f);
        //xAxis.setGranularity(15f);
        xAxis.setValueFormatter(formatter);

        Logger.logI(""+waterLoad.getListHistoricStatDay().size());

        /*ArrayList<Entry> listEntry = new ArrayList<>();

        Random rand = new Random();
        for(int i=0;i<=1440;i++){
            if(i%20==0) {
                listEntry.add(new Entry(i, rand.nextInt((450 - 50) + 1) + 50));
            }
        }*/


        LineDataSet dataSet = new LineDataSet(waterLoad.getListHistoricStatDay(), "2015");
        //LineDataSet dataSet = new LineDataSet(listEntry, "2015");
        dataSet.setColor(Color.rgb(25,118,210));
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setLineWidth(3f);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);

        chart.invalidate();
    }

    private void updateTextView(){
        tvDebitValue.setText(String.valueOf(waterLoad.getCurrentFlow()));
        tvStatDay.setText(String.valueOf(waterLoad.getLastHistoricDayInt()));
        tvStatWeek.setText(String.valueOf(waterLoad.getStatWeek()));
        tvStatMonth.setText(String.valueOf(waterLoad.getStatMonth()));
        tvStatYear.setText(String.valueOf(waterLoad.getStatYear()));
    }

    private void updateChart(){
        Logger.logI(""+waterLoad.getLastHistoricDayEntry());
        chart.getLineData().addEntry(waterLoad.getLastHistoricDayEntry(), 0);
        chart.notifyDataSetChanged(); // let the chart know it's data changed
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
            //Logger.logI("je monitor le flux du poste "+waterLoad.getName());

            try {
                String received=HttpPostManager.sendPost("load_id="+waterLoad.getId(), Constants.URL_GET_CURRENTFLOW);
                //Logger.logI(received);
                JSONObject jsonObject = new JSONObject(received);

                waterLoad.setCurrentFlow(jsonObject.getInt("current_flow"));
                waterLoad.addHistoricEntryDay(new Entry(DateTimeUtils.getMinuteSinceMidnight(), jsonObject.getInt("per_day")));

                waterLoad.setStatWeek(jsonObject.getInt("per_week"));
                waterLoad.setStatMonth(jsonObject.getInt("per_month"));
                waterLoad.setStatYear(jsonObject.getInt("per_year"));


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTextView();
                        updateChart();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
