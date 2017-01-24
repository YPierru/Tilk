package com.tilk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tilk.R;
import com.tilk.models.ProfilTilkeur;
import com.tilk.models.Room;
import com.tilk.models.WaterLoad;

import java.util.ArrayList;
import java.util.List;

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

    public void setUserEmail(String email){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(Constants.SESSION_EMAIL_USER,email);
        editor.apply();
    }

    public String getEmailUser(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getString(Constants.SESSION_EMAIL_USER,"");
    }

    public void setUserSurname(String surname){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(Constants.SESSION_SURNAME_USER,surname);
        editor.apply();
    }

    public String getUserSurname(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getString(Constants.SESSION_SURNAME_USER,"");
    }

    public void setUserId(int id){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(Constants.SESSION_ID_USER,id);
        editor.apply();
    }

    public int getUserId(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getInt(Constants.SESSION_ID_USER,-1);
    }

    public void setCommunautilkStatus(boolean status){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.SESSION_CT_STATUS,status);
        editor.apply();
    }

    public boolean getFirstCommunautilk(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getBoolean(Constants.SESSION_CT_FIRSTUSE,true);
    }

    public void setFirstCommunautilk(boolean firstCommunauTilk){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.SESSION_CT_FIRSTUSE,firstCommunauTilk);
        editor.apply();
    }

    public void setProfilTilkeur(ProfilTilkeur profil){
        SharedPreferences.Editor editor = getEditor();
        String json=gson.toJson(profil);
        editor.putString(Constants.SESSION_CT_PROFIL,json);
        editor.apply();
    }

    public ProfilTilkeur getProfilTilkeur(){
        SharedPreferences prefs = getSharedPref();
        String json=prefs.getString(Constants.SESSION_CT_PROFIL,"");
        return gson.fromJson(json,ProfilTilkeur.class);
    }

    public void setTilkId(int id){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(Constants.SESSION_ID_TILK,id);
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

    public void saveRooms(List<Room> listRooms){
        SharedPreferences.Editor editor = getEditor();
        String json;

        for(int i=0;i<listRooms.size();i++){
            json=gson.toJson(listRooms.get(i));
            editor.putString(Constants.SESSION_ROOM+""+i,json);
            editor.apply();
        }

        editor.putInt(Constants.SESSION_NUMBER_ROOMS, listRooms.size());
        editor.apply();
    }

    public ArrayList<Room> getRooms(){
        SharedPreferences prefs = getSharedPref();
        ArrayList<Room> listRooms = new ArrayList<>();
        String json;
        int size = prefs.getInt(Constants.SESSION_NUMBER_ROOMS,-1);

        for(int i=0;i<size;i++){
            json = prefs.getString(Constants.SESSION_ROOM+""+i,"");
            listRooms.add(gson.fromJson(json,Room.class));
        }

        return listRooms;
    }

    public boolean isRoomOrganisation(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getBoolean(context.getString(R.string.session_checkbox_rooms),false);
    }

    public void setMustBeRestarted(boolean mustBeRestarted){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean("must_be_restarted",mustBeRestarted);
        editor.apply();
    }

    public boolean mustBeRestarted(){
        SharedPreferences prefs = getSharedPref();
        return prefs.getBoolean("must_be_restarted",false);
    }

    private SharedPreferences.Editor getEditor(){

        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    private SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
