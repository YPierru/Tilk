package com.tilk.models;

import com.tilk.utils.EStatsTypes;
import com.tilk.utils.StatsManager;

import java.io.Serializable;

/**
 * Created by YPierru on 06/01/2017.
 */

public class WaterLoad implements Serializable{

    private int id;
    private String name;
    private int status;
    private double currentFlow;
    private boolean inRoom;
    private StatsManager statsDay,statsWeek,statsMonth,statsYear;


    public WaterLoad(int id,String name, int status, double currentFlow,boolean inRoom) {
        this.id=id;
        this.name = name;
        this.status = status;
        this.currentFlow = currentFlow;
        this.inRoom=inRoom;
        this.statsDay = new StatsManager();
        this.statsWeek = new StatsManager();
        this.statsMonth = new StatsManager();
        this.statsYear = new StatsManager();
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

    public double getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(double currentFlow) {
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

    public StatsManager getStats(EStatsTypes statsTypes){

        if(statsTypes==EStatsTypes.day){
            return statsDay;
        }else if(statsTypes==EStatsTypes.week){
            return statsWeek;
        }else if(statsTypes==EStatsTypes.month){
            return statsMonth;
        }else if(statsTypes==EStatsTypes.year){
            return statsYear;
        }

        return null;
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
