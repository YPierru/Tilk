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
import com.tilk.models.Room;
import com.tilk.models.WaterLoad;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    private class RoomMonitor implements Runnable{

        private ScheduledExecutorService executor;
        private String json;

        public RoomMonitor(){
            executor = Executors.newScheduledThreadPool(1);
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{ \"load_id\" : ");
            jsonBuilder.append("[");
            for(int i=0;i<room.getListWaterLoads().size();i++) {
                jsonBuilder.append("\""+room.getListWaterLoads().get(i).getId()+"\"");

                if(i+1<room.getListWaterLoads().size()){
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            jsonBuilder.append("}");

            json = jsonBuilder.toString();

        }

        public void startMonitor(){
            executor.scheduleAtFixedRate(this, 0, 2, TimeUnit.SECONDS);
        }

        public void stopMonitor(){
            executor.shutdown();
        }

        @Override
        public void run() {
            Logger.logI("je monitor le flux de la pièce "+room.getName());

            try {


                String received = HttpPostManager.sendPost("load_id_array=" + json, Constants.URL_GET_CURRENTFLOW);

                JSONObject jsonObject = new JSONObject(received);
                JSONArray array = jsonObject.getJSONArray("response");

                WaterLoad waterLoad;
                for(int i=0;i<array.length();i++){
                    waterLoad = room.getWaterLoadById(array.getJSONObject(i).getInt("id"));

                    waterLoad.setCurrentFlow(array.getJSONObject(i).getInt("current_flow"));
                    waterLoad.setStatDay(array.getJSONObject(i).getInt("per_day"));
                    waterLoad.setStatWeek(array.getJSONObject(i).getInt("per_week"));
                    waterLoad.setStatMonth(array.getJSONObject(i).getInt("per_month"));
                    waterLoad.setStatYear(array.getJSONObject(i).getInt("per_year"));
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDebitValue.setText(String.valueOf(room.getTotalFlow()));
                        tvStatDay.setText(String.valueOf(room.getTotalStatDay()));
                        tvStatWeek.setText(String.valueOf(room.getTotalStatWeek()));
                        tvStatMonth.setText(String.valueOf(room.getTotalStatMonth()));
                        tvStatYear.setText(String.valueOf(room.getTotalStatYear()));
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
