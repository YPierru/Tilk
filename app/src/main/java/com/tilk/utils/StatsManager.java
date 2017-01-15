package com.tilk.utils;

import com.github.mikephil.charting.data.Entry;
import com.tilk.models.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by YPierru on 15/01/2017.
 */

public class StatsManager implements Serializable{

    private ArrayList<Coordinate> listHistoric;
    private boolean entryAdded=false;

    public StatsManager() {
        listHistoric = new ArrayList<Coordinate>();
    }

    public void addEntry(Entry entry){
        Coordinate coo = new Coordinate((int)entry.getX(),(int)entry.getY());

        //new day, reset the list
        if(listHistoric.size()>0 && coo.getX()<getLastEntry().getX()){
            listHistoric.clear();
        }

        if(!isInList(coo)) {
            entryAdded=true;
            listHistoric.add(coo);
        }else{
            entryAdded=false;
        }
    }

    private boolean isInList(Coordinate cooToTest){
        /*for(Coordinate coo : listHistoric){
            if(cooToTest.getX()==coo.getX() && cooToTest.getY()==coo.getY()){
                return true;
            }
        }*/
        Iterator<Coordinate> iter = listHistoric.iterator();
        while(iter.hasNext()){
            Coordinate coo = iter.next();
            if(cooToTest.getX()==coo.getX() && cooToTest.getY()==coo.getY()){
                return true;
            }
        }

        return false;
    }

    public Entry getLastEntry(){
        Coordinate coo = listHistoric.get(listHistoric.size()-1);
        return new Entry(coo.getX(),coo.getY());
    }

    public ArrayList<Entry> getListHistoricEntry() {
        ArrayList<Entry> arrayList = new ArrayList<>();

        for(Coordinate coo: listHistoric){
            arrayList.add(new Entry(coo.getX(),coo.getY()));
        }

        return arrayList;
    }

    public int getLastEntryInt(){
        Coordinate coo = listHistoric.get(listHistoric.size()-1);
        return coo.getY();
    }

    public boolean isEntryAdded() {
        return entryAdded;
    }
}
