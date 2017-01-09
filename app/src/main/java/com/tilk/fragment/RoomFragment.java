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
            Logger.logI("je monitor le flux de la pièce "+room.getName());

            try {
                for(WaterLoad waterLoad : room.getListWaterLoads()) {
                    String received = HttpPostManager.sendPost("load=" + waterLoad.getName(), Constants.URL_GET_CURRENTFLOW);

                    JSONObject jsonObject = new JSONObject(received);

                    waterLoad.setCurrentFlow(jsonObject.getInt("current_flow"));
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDebitValue.setText(""+room.getTotalFlow());
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
