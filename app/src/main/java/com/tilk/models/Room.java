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

    public ArrayList<String> getListWaterLoadsName() {
        ArrayList<String> listWaterLoadsName = new ArrayList<>();
        for(WaterLoad waterLoad : listWaterLoads){
            listWaterLoadsName.add(waterLoad.getName());
        }
        return listWaterLoadsName;
    }

    public void setListWaterLoads(ArrayList<WaterLoad> listWaterLoads) {
        this.listWaterLoads = listWaterLoads;
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
}
