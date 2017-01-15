package com.tilk.models;

import com.tilk.utils.StatsManager;

import java.io.Serializable;

/**
 * Created by YPierru on 06/01/2017.
 */

public class WaterLoad implements Serializable{

    private int id;
    private String name;
    private int status;
    private int currentFlow;
    private boolean inRoom;
    private StatsManager statsDay,statsWeek,statsMonth,statsYear;


    public WaterLoad(int id,String name, int status, int currentFlow,boolean inRoom) {
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

    public StatsManager getStatsDay() {
        return statsDay;
    }

    public StatsManager getStatsWeek(){
        return statsWeek;
    }

    public StatsManager getStatsMonth() {
        return statsMonth;
    }

    public StatsManager getStatsYear() {
        return statsYear;
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
