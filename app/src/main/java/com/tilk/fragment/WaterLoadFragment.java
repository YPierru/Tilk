package com.tilk.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.tilk.R;
import com.tilk.activity.FlowDetailActivity;
import com.tilk.models.UserProfil;
import com.tilk.models.WaterLoad;
import com.tilk.utils.ChartBuilder;
import com.tilk.utils.Constants;
import com.tilk.utils.DateTimeUtils;
import com.tilk.utils.EStatsTypes;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.PreviousHistoric;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private LineChart chart_day,chart_week,chart_month, chart_year;
    private ChartBuilder chartBuilder;
    private UserProfil userProfil;
    private int idUser=-1;

    private EStatsTypes selectedStatType=EStatsTypes.none;


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
        userProfil = new UserProfil(getContext());
        idUser = userProfil.getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMonitor();
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Logger.logI("onActivityCreated");
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "trebuc_bold.ttf");

        tvDebitValue = (TextView)getView().findViewById(R.id.tv_debit_value);
        tvDebitValue.setTypeface(custom_font);
        tvStatDay = (TextView)getView().findViewById(R.id.tv_conso_jour_value);
        tvStatDay.setTypeface(custom_font);
        tvStatWeek = (TextView)getView().findViewById(R.id.tv_conso_hebdo_value);
        tvStatWeek.setTypeface(custom_font);
        tvStatMonth = (TextView)getView().findViewById(R.id.tv_conso_mois_value);
        tvStatMonth.setTypeface(custom_font);
        tvStatYear = (TextView)getView().findViewById(R.id.tv_conso_annee_value);
        tvStatYear.setTypeface(custom_font);
        chart_day = (LineChart)getView().findViewById(R.id.chart_evolution_day);
        chart_week = (LineChart)getView().findViewById(R.id.chart_evolution_week);
        chart_month = (LineChart)getView().findViewById(R.id.chart_evolution_month);
        chart_year = (LineChart)getView().findViewById(R.id.chart_evolution_year);
        Button btnShowGraphDay = (Button) getView().findViewById(R.id.btn_graph_day);
        Button btnShowGraphWeek = (Button) getView().findViewById(R.id.btn_graph_week);
        Button btnShowGraphMonth = (Button) getView().findViewById(R.id.btn_graph_month);
        Button btnShowGraphYear = (Button) getView().findViewById(R.id.btn_graph_year);
        Button btnDetail = (Button) getView().findViewById(R.id.btn_details);
        btnDetail.setTypeface(custom_font);

        TextView tv = (TextView)getView().findViewById(R.id.tv_debit_title);
        tv.setTypeface(custom_font);

        tv = (TextView)getView().findViewById(R.id.tv_conso_title);
        tv.setTypeface(custom_font);

        tv = (TextView)getView().findViewById(R.id.tv_evolution_title);
        tv.setTypeface(custom_font);

        tv = (TextView)getView().findViewById(R.id.tv_jours_conso);
        tv.setTypeface(custom_font);

        tv = (TextView)getView().findViewById(R.id.tv_semaine_conso);
        tv.setTypeface(custom_font);

        TextView tvConsoMonth = (TextView)getView().findViewById(R.id.tv_mois_conso);
        tvConsoMonth.setText(DateTimeUtils.getCurrentMonthName().toUpperCase());
        tvConsoMonth.setTypeface(custom_font);

        TextView tvConsoYear = (TextView)getView().findViewById(R.id.tv_annee_conso);
        tvConsoYear.setText(DateTimeUtils.getCurrentYear());
        tvConsoYear.setTypeface(custom_font);


        btnShowGraphDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStatType=EStatsTypes.day;
                actionButtonGraph();
            }
        });

        btnShowGraphWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStatType=EStatsTypes.week;
                actionButtonGraph();
            }
        });

        btnShowGraphMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStatType=EStatsTypes.month;
                actionButtonGraph();
            }
        });

        btnShowGraphYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStatType=EStatsTypes.year;
                actionButtonGraph();
            }
        });

        btnShowGraphDay.performClick();

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FlowDetailActivity.class);
                intent.putExtra("load",waterLoad);
                startActivity(intent);

            }
        });
    }

    private void buildChart(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ArrayList<Entry> listPrevious;
                if(idUser==1){
                    PreviousHistoric previousHistoric = new PreviousHistoric();
                    listPrevious=previousHistoric.getListEntry(selectedStatType);
                }else{
                    listPrevious=waterLoad.getStats(selectedStatType).getListHistoricPreviousEntry();
                }

                chartBuilder.buildGraph(waterLoad.getStats(selectedStatType).getListHistoricEntry(),listPrevious,selectedStatType);
            }
        });
    }

    private void setVisibilityAllChartGone(){
        chart_day.setVisibility(View.GONE);
        chart_week.setVisibility(View.GONE);
        chart_month.setVisibility(View.GONE);
        chart_year.setVisibility(View.GONE);
    }

    private void actionButtonGraph(){
        setVisibilityAllChartGone();
        if(chartBuilder!=null){
            chartBuilder=null;
        }
        if(selectedStatType==EStatsTypes.day) {
            chart_day.setVisibility(View.VISIBLE);
            chartBuilder = new ChartBuilder(chart_day);
        }else if(selectedStatType==EStatsTypes.week){
            chart_week.setVisibility(View.VISIBLE);
            chartBuilder = new ChartBuilder(chart_week);
        }else if(selectedStatType==EStatsTypes.month){
            chart_month.setVisibility(View.VISIBLE);
            chartBuilder = new ChartBuilder(chart_month);
        }else if(selectedStatType==EStatsTypes.year){
            chart_year.setVisibility(View.VISIBLE);
            chartBuilder = new ChartBuilder(chart_year);
        }
        new RetrieveGraphValues().execute();
    }

    private void updateTextView(){
        tvDebitValue.setText(String.valueOf(waterLoad.getCurrentFlow()));

        tvStatDay.setText(String.valueOf(waterLoad.getStats(EStatsTypes.day).getLastEntryInt()));
        tvStatWeek.setText(String.valueOf(waterLoad.getStats(EStatsTypes.week).getLastEntryInt()));
        tvStatMonth.setText(String.valueOf(waterLoad.getStats(EStatsTypes.month).getLastEntryInt()));
        tvStatYear.setText(String.valueOf(waterLoad.getStats(EStatsTypes.year).getLastEntryInt()));
    }

    private void updateChart(){
        //Logger.logI(""+waterLoad.getLastHistoricDayEntry());

        if(selectedStatType!=EStatsTypes.none && waterLoad.getStats(selectedStatType).isEntryAdded()){
            chartBuilder.addEntry(waterLoad.getStats(selectedStatType).getLastEntry());
        }
    }

    private class RetrieveGraphValues extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(selectedStatType==EStatsTypes.day) {
                dayStatProcess();
            }else if(selectedStatType==EStatsTypes.week){
                weekStatProcess();
            }else if(selectedStatType==EStatsTypes.month){
                monthStatProcess();
            }else if(selectedStatType==EStatsTypes.year){
                yearStatProcess();
            }

            buildChart();

            return null;
        }

        private void dayStatProcess(){
            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_STATS_DAY);

                //Logger.logI(received);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                int minutes,cumul;

                for(int i=0;i<array.length();i++){
                    minutes=array.getJSONObject(i).getInt("minutes");
                    cumul=array.getJSONObject(i).getInt("cumul")/1000;

                    waterLoad.getStats(selectedStatType).addEntry(new Entry(minutes,cumul));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void weekStatProcess(){
            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_STATS_WEEK);

                //Logger.logI(received);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                int hours,cumul;

                for(int i=0;i<array.length();i++){
                    hours=array.getJSONObject(i).getInt("hours");
                    cumul=array.getJSONObject(i).getInt("cumul")/1000;

                    waterLoad.getStats(selectedStatType).addEntry(new Entry(hours,cumul));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void monthStatProcess(){
            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_STATS_MONTH);

                //Logger.logI(received);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                int hours6,cumul;

                for(int i=0;i<array.length();i++){
                    hours6=array.getJSONObject(i).getInt("6Hours");
                    cumul=array.getJSONObject(i).getInt("cumul")/1000;

                    waterLoad.getStats(selectedStatType).addEntry(new Entry(hours6,cumul));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void yearStatProcess(){

            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_STATS_YEAR);

                //Logger.logI(received);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                int minutes,cumul;

                for(int i=0;i<array.length();i++){
                    minutes=array.getJSONObject(i).getInt("days");
                    cumul=array.getJSONObject(i).getInt("cumul")/1000;

                    waterLoad.getStats(selectedStatType).addEntry(new Entry(minutes,cumul));
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class WaterLoadMonitor implements Runnable{

        private ScheduledExecutorService executor;

        public WaterLoadMonitor(){
            executor = Executors.newScheduledThreadPool(1);
        }

        public void startMonitor(){
            executor.scheduleAtFixedRate(this, 0, Constants.MONITOR_SECONDS, TimeUnit.SECONDS);
        }

        public void stopMonitor(){
            executor.shutdown();
        }

        @Override
        public void run() {
            //Logger.logI("je monitor le flux du poste "+waterLoad.getName());

            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_CURRENTFLOW);
                //Logger.logI("dzjeidozjei"+received);
                JSONObject jsonObject = new JSONObject(received);
                DecimalFormat df = new DecimalFormat("#.##");
                double flow = jsonObject.getDouble("current_flow")*60/1000;
                flow = Double.parseDouble(df.format(flow).replace(",","."));

                //Logger.logI(""+flow);

                waterLoad.setCurrentFlow(flow);

                waterLoad.getStats(EStatsTypes.day).addEntry(new Entry(jsonObject.getInt("dMinutes"), jsonObject.getInt("dCumul")/1000));
                waterLoad.getStats(EStatsTypes.week).addEntry(new Entry(jsonObject.getInt("wHours"), jsonObject.getInt("wCumul")/1000));
                waterLoad.getStats(EStatsTypes.month).addEntry(new Entry(jsonObject.getInt("m6Hours"), jsonObject.getInt("mCumul")/1000));
                waterLoad.getStats(EStatsTypes.year).addEntry(new Entry(jsonObject.getInt("yDays"), jsonObject.getInt("yCumul")/1000));

                //Logger.logI(""+waterLoad.getStats(EStatsTypes.day).getLastEntryInt());

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
