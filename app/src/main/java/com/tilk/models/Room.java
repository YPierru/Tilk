package com.tilk.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YPierru on 07/01/2017.
 */

public class Room implements Serializable{

    private String name;
    private ArrayList<WaterLoad> listWaterLoads;

    public Room(String name){
        this.name=name;
        listWaterLoads=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WaterLoad> getListWaterLoads() {
        return listWaterLoads;
    }

    public WaterLoad getWaterLoadById(int id){
        for(WaterLoad wl : listWaterLoads){
            if(wl.getId()==id){
                return wl;
            }
        }
        return null;
    }

    public ArrayList<String> getListWaterLoadsName() {
        ArrayList<String> listWaterLoadsName = new ArrayList<>();
        for(WaterLoad waterLoad : listWaterLoads){
            listWaterLoadsName.add(waterLoad.getName());
        }
        return listWaterLoadsName;
    }

    public void addWaterLoad(WaterLoad load){
        listWaterLoads.add(load);
    }

    public void clearWaterLoads(){
        listWaterLoads.clear();
    }

    public int getTotalFlow(){
        int totalFlow=0;
        for(WaterLoad waterLoad : listWaterLoads){
            totalFlow+=waterLoad.getCurrentFlow();
        }

        return totalFlow;
    }

    public int getTotalStatDay(){
        int total=0;
        for(WaterLoad waterLoad : listWaterLoads){
            total+=waterLoad.getLastHistoricDayInt();
        }
        return total;
    }

    public int getTotalStatWeek(){
        int total=0;
        for(WaterLoad waterLoad : listWaterLoads){
            total+=waterLoad.getStatWeek();
        }
        return total;
    }

    public int getTotalStatMonth(){
        int total=0;
        for(WaterLoad waterLoad : listWaterLoads){
            total+=waterLoad.getStatMonth();
        }
        return total;
    }

    public int getTotalStatYear(){
        int total=0;
        for(WaterLoad waterLoad : listWaterLoads){
            total+=waterLoad.getStatYear();
        }
        return total;
    }
}
