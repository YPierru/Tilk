package com.tilk.models;

/**
 * Created by YPierru on 06/01/2017.
 */

public class WaterLoad {

    private String name;
    private int status;
    private int currentFlow;
    private boolean inRoom;

    public WaterLoad(String name, int status, int currentFlow,boolean inRoom) {
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
