package com.tilk.models;

import com.github.mikephil.charting.data.Entry;
import com.tilk.utils.EStatsTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public int getTotalStat(EStatsTypes statsTypes){
        int total=0;

        for(WaterLoad waterLoad : listWaterLoads){
            total+=waterLoad.getStats(statsTypes).getLastEntryInt();
        }

        return total;
    }

    private ArrayList<Entry> sortListEntry(ArrayList<Entry> listEntry){
        Collections.sort(listEntry, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry1, Entry entry2)
            {
                if(entry1.getX()<entry2.getX()){
                    return -1;
                }

                if(entry1.getX()==entry2.getX() && entry1.getY()<entry2.getY()){
                    return -1;
                }

                if(entry1.getX()==entry2.getX() && entry1.getY()==entry2.getY()){
                    return 0;
                }

                if(entry1.getX()==entry2.getX() && entry1.getY()>entry2.getY()){
                    return 1;
                }

                if(entry1.getX()>entry2.getX()){
                    return 1;
                }

                return 0;
            }
        });

        return listEntry;
    }

    public ArrayList<Entry> getEntries(EStatsTypes statsTypes){
        ArrayList<Entry> listEntry = new ArrayList<>();
        ArrayList<Entry> listNoDuplicateEntry = new ArrayList<>();
        Entry tmpEntry;


        //Merging all stats lists according to stat type
        for(WaterLoad waterLoad : listWaterLoads){
            listEntry.addAll(waterLoad.getStats(statsTypes).getListHistoricEntryNoDuplicate());
        }



        //Sort by X
        listEntry = sortListEntry(listEntry);

        int i=0;
        while(i<listEntry.size()){
            tmpEntry=listEntry.get(i);

            while(i+1<listEntry.size() && listEntry.get(i).getX()==listEntry.get(i+1).getX() && listEntry.get(i+1).getY()>= listEntry.get(i).getY() ){
                tmpEntry=listEntry.get(i+1);
                i++;
            }
            i++;
            listNoDuplicateEntry.add(tmpEntry);
        }

        /*for(Entry entry : listNoDuplicateEntry){
            Logger.logI("coucou "+entry);
        }*/

        return listNoDuplicateEntry;

    }

    public ArrayList<Entry> getPreviousEntries(EStatsTypes statsTypes){
        ArrayList<Entry> listEntry = new ArrayList<>();
        ArrayList<Entry> listNoDuplicateEntry = new ArrayList<>();
        Entry tmpEntry;


        //Merging all stats lists according to stat type
        for(WaterLoad waterLoad : listWaterLoads){
            listEntry.addAll(waterLoad.getStats(statsTypes).getListHistoricPreviousEntryNoDuplicate());
        }



        //Sort by X
        listEntry = sortListEntry(listEntry);

        int i=0;
        while(i<listEntry.size()){
            tmpEntry=listEntry.get(i);

            while(i+1<listEntry.size() && listEntry.get(i).getX()==listEntry.get(i+1).getX() && listEntry.get(i+1).getY()>= listEntry.get(i).getY() ){
                tmpEntry=listEntry.get(i+1);
                i++;
            }
            i++;
            listNoDuplicateEntry.add(tmpEntry);
        }

        /*for(Entry entry : listNoDuplicateEntry){
            Logger.logI("coucou "+entry);
        }*/

        return listNoDuplicateEntry;
    }

    public boolean isEntryAdded(EStatsTypes statsTypes){

        for (WaterLoad waterLoad : listWaterLoads) {
            if (waterLoad.getStats(statsTypes).isEntryAdded()) {
                return true;
            }
        }

        return false;
    }

    public Entry getLastEntry(EStatsTypes statsTypes){
        ArrayList<Entry> listEntry = getEntries(statsTypes);

        return listEntry.get(listEntry.size()-1);
    }

    public String getJSONForWaterLoadsID(){
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{ \"load_id\" : ");
        jsonBuilder.append("[");
        for(int i=0;i<listWaterLoads.size();i++) {
            jsonBuilder.append("\""+listWaterLoads.get(i).getId()+"\"");

            if(i+1<listWaterLoads.size()){
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }
}
