package com.tilk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tilk.models.WaterLoad;

import java.util.ArrayList;

/**
 * Created by YPierru on 05/01/2017.
 */

public class SharedPreferencesManager {

    private Context context;
    private Gson gson;

    public SharedPreferencesManager(Context cont){
        context=cont;
        gson = new Gson();
    }

    public void setUserOnline() {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.SESSION_STATUS, true);
        editor.apply();
    }

    public void setUserOffline() {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.SESSION_STATUS, false);
        editor.apply();
    }

    public boolean getUserStatus() {
        SharedPreferences prefs = getSharedPref();
        return prefs.getBoolean(Constants.SESSION_STATUS, false);
    }

    public boolean getFirstRun(){
        SharedPreferences prefs = getSharedPref();
        //return prefs.getBoolean(Constants.SESSION_FIRSTRUN,true);
        return true;
    }

    public void setFirstRunKO(){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.SESSION_FIRSTRUN,false);
        editor.apply();
    }

    public void saveWaterLoads(ArrayList<WaterLoad> listWaterLoads){
        SharedPreferences.Editor editor = getEditor();
        String json;

        for(int i=0;i<listWaterLoads.size();i++){
            json=gson.toJson(listWaterLoads.get(i));
            editor.putString(Constants.SESSION_WATERLOAD+""+i,json);
            editor.apply();
        }

        editor.putInt(Constants.SESSION_NUMBER_WATERLOADS, listWaterLoads.size());
        editor.apply();

    }

    public ArrayList<WaterLoad> getWaterLoads(){
        SharedPreferences prefs = getSharedPref();
        ArrayList<WaterLoad> listWaterLoads = new ArrayList<>();
        String json;
        int size = prefs.getInt(Constants.SESSION_NUMBER_WATERLOADS,-1);

        for(int i=0;i<size;i++){
            json = prefs.getString(Constants.SESSION_WATERLOAD+""+i,"");
            listWaterLoads.add(gson.fromJson(json,WaterLoad.class));
        }

        return listWaterLoads;
    }

    private SharedPreferences.Editor getEditor(){

        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    private SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
