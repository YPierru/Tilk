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
import com.tilk.models.WaterLoad;
import com.tilk.utils.ChartBuilder;
import com.tilk.utils.Constants;
import com.tilk.utils.DateTimeUtils;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Button btnShowGraphDay;
    private Button btnShowGraphWeek;
    private Button btnShowGraphMonth;
    private Button btnShowGraphYear;
    private LineChart chart;
    private ChartBuilder chartBuilder;



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
        //Logger.logI("onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Logger.logI("onCreateView");
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
        //Logger.logI("onStart");

        waterLoad.retrieveHistoric(getActivity());

        startMonitor();
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

        //Logger.logI("onActivityCreated");

        tvDebitValue = (TextView)getView().findViewById(R.id.tv_debit_value);
        tvStatDay = (TextView)getView().findViewById(R.id.tv_conso_jour_value);
        tvStatWeek = (TextView)getView().findViewById(R.id.tv_conso_hebdo_value);
        tvStatMonth = (TextView)getView().findViewById(R.id.tv_conso_mois_value);
        tvStatYear = (TextView)getView().findViewById(R.id.tv_conso_annee_value);
        chart = (LineChart) getView().findViewById(R.id.chart_evolution);
        btnShowGraphDay = (Button) getView().findViewById(R.id.btn_graph_day);
        btnShowGraphWeek = (Button) getView().findViewById(R.id.btn_graph_week);
        btnShowGraphMonth = (Button) getView().findViewById(R.id.btn_graph_month);
        btnShowGraphYear = (Button) getView().findViewById(R.id.btn_graph_year);
        final ScrollView svPoste = (ScrollView)getView().findViewById(R.id.sv_poste);
        chartBuilder = new ChartBuilder(chart);



        btnShowGraphDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.VISIBLE);
                new RetrieveGraphValues().execute();
                svPoste.post(new Runnable() {
                    @Override
                    public void run() {
                        svPoste.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        btnShowGraphWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
            }
        });
        btnShowGraphMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
            }
        });
        btnShowGraphYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
            }
        });
    }

    private void buildChart(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chartBuilder.buildGraphDay(waterLoad.getListHistoricStatDay());
            }
        });
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
        if(chart!=null) {
            chartBuilder.addDayEntry(waterLoad.getLastHistoricDayEntry());
        }
    }

    private class RetrieveGraphValues extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_STATS);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                int minutes,per_day;

                for(int i=0;i<array.length();i++){
                    minutes=array.getJSONObject(i).getInt("minutes");
                    per_day=array.getJSONObject(i).getInt("per_day");

                    waterLoad.addHistoricEntryDay(new Entry(minutes,per_day));
                }

                buildChart();

            }catch(Exception e){
                e.printStackTrace();
            }


            return null;
        }
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
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId(), Constants.URL_GET_CURRENTFLOW);
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
