package com.tilk.models;

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
    private int statDay;
    private int statWeek;
    private int statMonth;
    private int statYear;


    public WaterLoad(int id,String name, int status, int currentFlow,boolean inRoom) {
        this.id=id;
        this.name = name;
        this.status = status;
        this.currentFlow = currentFlow;
        this.inRoom=inRoom;
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

    public int getStatDay() {
        return statDay;
    }

    public void setStatDay(int statDay) {
        this.statDay = statDay;
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
