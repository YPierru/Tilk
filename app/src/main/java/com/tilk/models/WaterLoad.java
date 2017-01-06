package com.tilk.models;

/**
 * Created by YPierru on 06/01/2017.
 */

public class WaterLoad {

    private String name;
    private int status;
    private int currentFlow;

    public WaterLoad(String name, int status, int currentFlow) {
        this.name = name;
        this.status = status;
        this.currentFlow = currentFlow;
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

    @Override
    public String toString() {
        return "WaterLoad{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", currentFlow=" + currentFlow +
                '}';
    }
}
