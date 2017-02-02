package com.tilk.utils;

import com.github.mikephil.charting.data.Entry;
import com.tilk.models.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by YPierru on 15/01/2017.
 */

public class StatsManager implements Serializable{

    private ArrayList<Coordinate> listHistoricPrevious;
    private ArrayList<Coordinate> listHistoric;
    private boolean entryAdded=false;

    public StatsManager() {
        listHistoric = new ArrayList<Coordinate>();
    }

    public void addEntry(Entry entry){
        Coordinate coo = new Coordinate((int)entry.getX(),(int)entry.getY());

        //new day, reset the list
        if(listHistoric.size()>0 && coo.getX()<getLastEntry().getX()){
            listHistoricPrevious=listHistoric;
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

    public ArrayList<Entry> getListHistoricPreviousEntry() {
        ArrayList<Entry> arrayList = new ArrayList<>();



        if(listHistoricPrevious!=null){
            for(Coordinate coo: listHistoricPrevious){
                arrayList.add(new Entry(coo.getX(),coo.getY()));
            }
            return arrayList;
        }


        return new ArrayList<>();
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

    public ArrayList<Entry> getListHistoricEntryNoDuplicate(){
        ArrayList<Entry> listEntry = getListHistoricEntry();
        ArrayList<Entry> listNoDuplicateEntry = new ArrayList<>();
        Entry tmpEntry;

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
        return listNoDuplicateEntry;
    }

    public ArrayList<Entry> getListHistoricPreviousEntryNoDuplicate(){
        ArrayList<Entry> listEntry = getListHistoricPreviousEntry();
        ArrayList<Entry> listNoDuplicateEntry = new ArrayList<>();
        Entry tmpEntry;

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
        return listNoDuplicateEntry;
    }

    public int getLastEntryInt(){
        Coordinate coo = listHistoric.get(listHistoric.size()-1);
        return coo.getY();
    }

    public boolean isEntryAdded() {
        return entryAdded;
    }
}
