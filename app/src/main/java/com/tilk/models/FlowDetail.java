package com.tilk.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YPierru on 29/01/2017.
 */

public class FlowDetail {

    private long startTime;
    private long endTime;
    private int totalWater;
    private int averageFlow;

    public FlowDetail(long startTime, long endTime, int totalWater, int averageFlow) {
        this.startTime = startTime*1000L;
        this.endTime = endTime*1000L;
        this.totalWater = totalWater;
        this.averageFlow = averageFlow;
    }

    public String getStartTime() {
        SimpleDateFormat format= new SimpleDateFormat("dd/MM à HH:mm:ss");
        return format.format(new Date(startTime));
    }
    public String getEndTime() {
        SimpleDateFormat format= new SimpleDateFormat("dd/MM à HH:mm:ss");
        return format.format(new Date(endTime));
    }

    public int getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(int totalWater) {
        this.totalWater = totalWater;
    }

    public int getAverageFlow() {
        return averageFlow;
    }

    public void setAverageFlow(int averageFlow) {
        this.averageFlow = averageFlow;
    }

}
