package com.tilk.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.tilk.R;
import com.tilk.models.Room;
import com.tilk.models.WaterLoad;
import com.tilk.utils.ChartBuilder;
import com.tilk.utils.Constants;
import com.tilk.utils.EStatsTypes;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by YPierru on 03/01/2017.
 */

public class RoomFragment extends Fragment {

    private Room room;
    private RoomMonitor roomMonitor;

    private TextView tvDebitValue;
    private TextView tvStatDay;
    private TextView tvStatWeek;
    private TextView tvStatMonth;
    private TextView tvStatYear;
    private LineChart chart_day,chart_week,chart_month, chart_year;
    private ChartBuilder chartBuilder;
    private EStatsTypes selectedStatType;

    public static RoomFragment newInstance(Room room){
        RoomFragment roomFragment = new RoomFragment();

        Bundle args = new Bundle();
        args.putSerializable("room",room);
        roomFragment.setArguments(args);

        return roomFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = (Room)getArguments().getSerializable("room");

    }

    public void startMonitor(){
        if(roomMonitor==null) {
            Logger.logI("********************* START MONITORING " + room.getName() + " *********************");
            roomMonitor = new RoomMonitor();
            roomMonitor.startMonitor();
        }
    }

    public void stopMonitor(){
        if(roomMonitor!=null) {
            Logger.logI("********************* STOP MONITORING "+room.getName()+" *********************");
            roomMonitor.stopMonitor();
            roomMonitor=null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());
        ArrayList<Room> listRooms = sharedPreferencesManager.getRooms();
        for(Room r : listRooms){
            if(r.getName().equals(room.getName())){
                room=r;
                break;
            }
        }

        startMonitor();
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
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tvDebitValue = (TextView)getView().findViewById(R.id.tv_debit_value);
        tvStatDay = (TextView)getView().findViewById(R.id.tv_conso_jour_value);
        tvStatWeek = (TextView)getView().findViewById(R.id.tv_conso_hebdo_value);
        tvStatMonth = (TextView)getView().findViewById(R.id.tv_conso_mois_value);
        tvStatYear = (TextView)getView().findViewById(R.id.tv_conso_annee_value);
        chart_day = (LineChart)getView().findViewById(R.id.chart_evolution_day);
        chart_week = (LineChart)getView().findViewById(R.id.chart_evolution_week);
        chart_month = (LineChart)getView().findViewById(R.id.chart_evolution_month);
        chart_year = (LineChart)getView().findViewById(R.id.chart_evolution_year);

        Button btnShowGraphDay = (Button) getView().findViewById(R.id.btn_graph_day);
        Button btnShowGraphWeek = (Button) getView().findViewById(R.id.btn_graph_week);
        Button btnShowGraphMonth = (Button) getView().findViewById(R.id.btn_graph_month);
        Button btnShowGraphYear = (Button) getView().findViewById(R.id.btn_graph_year);

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
    }

    private void buildChart(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(selectedStatType==EStatsTypes.day) {
                    chartBuilder.buildGraphDay(room.getEntries(selectedStatType));
                }else if(selectedStatType==EStatsTypes.week){
                    chartBuilder.buildGraphWeek(room.getEntries(selectedStatType));
                }else if(selectedStatType==EStatsTypes.month){
                    chartBuilder.buildGraphMonth(room.getEntries(selectedStatType));
                }else if(selectedStatType==EStatsTypes.year){
                    chartBuilder.buildGraphYear(room.getEntries(selectedStatType));
                }
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
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.sv_room);
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

        scrollView.post(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void updateTextView(){
        tvDebitValue.setText(String.valueOf(room.getTotalFlow()));

        tvStatDay.setText(String.valueOf(room.getTotalStat(EStatsTypes.day)));
        tvStatWeek.setText(String.valueOf(room.getTotalStat(EStatsTypes.week)));
        tvStatMonth.setText(String.valueOf(room.getTotalStat(EStatsTypes.month)));
        tvStatYear.setText(String.valueOf(room.getTotalStat(EStatsTypes.year)));
    }

    private void updateChart(){
        //Logger.logI(""+waterLoad.getLastHistoricDayEntry());

        if(room.isEntryAdded(selectedStatType)){
            chartBuilder.addEntry(room.getLastEntry(selectedStatType));
        }
    }

    private class RetrieveGraphValues extends AsyncTask<Void,Void,Void> {

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

                for(WaterLoad waterLoad : room.getListWaterLoads()){
                    String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_STATS_DAY);

                    //Logger.logI(received);

                    JSONObject jsonObject = new JSONObject(received);
                    JSONArray array = jsonObject.getJSONArray("response");

                    int minutes,cumul;

                    for(int i=0;i<array.length();i++){
                        minutes=array.getJSONObject(i).getInt("minutes");
                        cumul=array.getJSONObject(i).getInt("cumul");

                        waterLoad.getStatsDay().addEntry(new Entry(minutes,cumul));
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void weekStatProcess(){
            try {

                for(WaterLoad waterLoad : room.getListWaterLoads()){
                    String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_STATS_WEEK);

                    Logger.logI(received);

                    JSONObject jsonObject = new JSONObject(received);
                    JSONArray array = jsonObject.getJSONArray("response");

                    int minutes,cumul;

                    for(int i=0;i<array.length();i++){
                        minutes=array.getJSONObject(i).getInt("hours");
                        cumul=array.getJSONObject(i).getInt("cumul");

                        waterLoad.getStatsWeek().addEntry(new Entry(minutes,cumul));
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void monthStatProcess(){
            try {

                for(WaterLoad waterLoad : room.getListWaterLoads()){
                    String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_STATS_MONTH);

                    //Logger.logI(received);

                    JSONObject jsonObject = new JSONObject(received);
                    JSONArray array = jsonObject.getJSONArray("response");

                    int minutes,cumul;

                    for(int i=0;i<array.length();i++){
                        minutes=array.getJSONObject(i).getInt("6Hours");
                        cumul=array.getJSONObject(i).getInt("cumul");

                        waterLoad.getStatsMonth().addEntry(new Entry(minutes,cumul));
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        private void yearStatProcess(){
            try {

                for(WaterLoad waterLoad : room.getListWaterLoads()){
                    String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_STATS_YEAR);

                    //Logger.logI(received);

                    JSONObject jsonObject = new JSONObject(received);
                    JSONArray array = jsonObject.getJSONArray("response");

                    int minutes,cumul;

                    for(int i=0;i<array.length();i++){
                        minutes=array.getJSONObject(i).getInt("days");
                        cumul=array.getJSONObject(i).getInt("cumul");

                        waterLoad.getStatsYear().addEntry(new Entry(minutes,cumul));
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class RoomMonitor implements Runnable{

        private ScheduledExecutorService executor;

        public RoomMonitor(){
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
            //Logger.logI("je monitor le flux de la pièce "+room.getName());

            try {


                String received = HttpPostManager.sendPost("load_id_array=" + room.getJSONForWaterLoadsID(), Constants.URL_GET_CURRENTFLOW);

                //Logger.logI(received);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                WaterLoad waterLoad;
                for(int i=0;i<array.length();i++){
                    waterLoad = room.getWaterLoadById(array.getJSONObject(i).getInt("id"));

                    waterLoad.setCurrentFlow(array.getJSONObject(i).getInt("current_flow"));

                    waterLoad.getStatsDay().addEntry(new Entry(array.getJSONObject(i).getInt("dMinutes"), array.getJSONObject(i).getInt("dCumul")));
                    waterLoad.getStatsWeek().addEntry(new Entry(array.getJSONObject(i).getInt("wHours"), array.getJSONObject(i).getInt("wCumul")));
                    waterLoad.getStatsMonth().addEntry(new Entry(array.getJSONObject(i).getInt("m6Hours"), array.getJSONObject(i).getInt("mCumul")));
                    waterLoad.getStatsYear().addEntry(new Entry(array.getJSONObject(i).getInt("yDays"), array.getJSONObject(i).getInt("yCumul")));
                }

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
