package com.tilk.models;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.tilk.utils.SharedPreferencesManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YPierru on 06/01/2017.
 */

public class WaterLoad implements Serializable{

    private int id;
    private String name;
    private int status;
    private int currentFlow;
    private boolean inRoom;
    //private int statDay;
    private ArrayList<Coordinate> listHistoricStatDay;
    private int statWeek;
    private int statMonth;
    private int statYear;


    public WaterLoad(int id,String name, int status, int currentFlow,boolean inRoom) {
        this.id=id;
        this.name = name;
        this.status = status;
        this.currentFlow = currentFlow;
        this.inRoom=inRoom;

        this.listHistoricStatDay = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(int currentFlow) {
        this.currentFlow = currentFlow;
    }

    public boolean isInRoom(){
        return inRoom;
    }

    public void setInRoom(boolean inRoom){
        this.inRoom=inRoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addHistoricEntryDay(Entry entry){
        Coordinate coo = new Coordinate((int)entry.getX(),(int)entry.getY());

        if(!isInList(coo,listHistoricStatDay)) {
            listHistoricStatDay.add(coo);
        }
    }

    public void saveHistoric(Context context){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        sharedPreferencesManager.saveListHistoric(listHistoricStatDay,name);
    }

    public void retrieveHistoric(Context context){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        listHistoricStatDay=sharedPreferencesManager.getListHistoric(name);
    }

    private boolean isInList(Coordinate cooToTest, ArrayList<Coordinate> listHistoricStat){

        for(Coordinate coo : listHistoricStat){
            if(cooToTest.getX()==coo.getX() && cooToTest.getY()==coo.getY()){
                return true;
            }
        }

        return false;

    }

    public ArrayList<Entry> getListHistoricStatDay() {
        ArrayList<Entry> arrayList = new ArrayList<>();

        for(Coordinate coo: listHistoricStatDay){
            arrayList.add(new Entry(coo.getX(),coo.getY()));
        }

        return arrayList;
    }

    public Entry getLastHistoricDayEntry(){
        Coordinate coo = listHistoricStatDay.get(listHistoricStatDay.size()-1);
        return new Entry(coo.getX(),coo.getY());
    }

    public int getLastHistoricDayInt(){
        Coordinate coo = listHistoricStatDay.get(listHistoricStatDay.size()-1);
        return coo.getY();
    }

    public int getStatWeek() {
        return statWeek;
    }

    public void setStatWeek(int statWeek) {
        this.statWeek = statWeek;
    }

    public int getStatMonth() {
        return statMonth;
    }

    public void setStatMonth(int statMonth) {
        this.statMonth = statMonth;
    }

    public int getStatYear() {
        return statYear;
    }

    public void setStatYear(int statYear) {
        this.statYear = statYear;
    }


    @Override
    public String toString() {
        return "WaterLoad{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", currentFlow=" + currentFlow +
                ", inRoom=" + inRoom+
                '}';
    }
}
