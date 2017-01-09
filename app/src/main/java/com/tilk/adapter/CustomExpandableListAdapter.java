package com.tilk.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tilk.R;
import com.tilk.models.Room;
import com.tilk.models.WaterLoad;
import com.tilk.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YPierru on 06/01/2017.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listRoomsName;
    private HashMap<String, List<String>> mapRoomsNameLoadsName;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button btnAddLoad;

    public CustomExpandableListAdapter(Context context, SharedPreferencesManager sharedPreferencesManager, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.listRoomsName = expandableListTitle;
        this.mapRoomsNameLoadsName = expandableListDetail;
        this.sharedPreferencesManager=sharedPreferencesManager;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.mapRoomsNameLoadsName.get(this.listRoomsName.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.mapRoomsNameLoadsName.get(this.listRoomsName.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.listRoomsName.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listRoomsName.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        btnAddLoad = (Button)convertView.findViewById(R.id.btn_add_load);

        btnAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogListLoads(listTitle);
            }
        });

        return convertView;
    }

    private void alertDialogListLoads(final String listTitle){
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //List of all the water loads
        final ArrayList<WaterLoad> listWaterLoads = sharedPreferencesManager.getWaterLoads();

        //Name of the water loads in the current room
        List<String> listWaterLoadsNameInRoom= mapRoomsNameLoadsName.get(listTitle);

        //Array used to store the water loads checked in the current room
        final boolean[] checkedWaterLoads = new boolean[listWaterLoads.size()];



        //Building the boolean array
        //for each water loads, if its name is in the list of loads in the current room, then it's checked
        //else, it's not checked
        for(int i=0;i<listWaterLoads.size();i++){
            for(int j=0;j<listWaterLoadsNameInRoom.size();j++){

                if(listWaterLoads.get(i).getName().equals(listWaterLoadsNameInRoom.get(j))){
                    checkedWaterLoads[i]=true;
                    break;
                }

                if(j+1>=listWaterLoadsNameInRoom.size()){
                    checkedWaterLoads[i]=false;
                    break;
                }
            }
        }

        //Method setMultiChoiceItems of DialogAlert Builder must use only array
        final String[] arrayWaterLoadsName = new String[listWaterLoads.size()];
        for(int i=0;i<listWaterLoads.size();i++){
            arrayWaterLoadsName[i]=listWaterLoads.get(i).getName();
        }


        builder.setMultiChoiceItems(arrayWaterLoadsName,checkedWaterLoads, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedWaterLoads[which]=isChecked;
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Poste(s) de la pièce "+listTitle);

        // Set the positive/yes button click listener
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Room> listRooms = sharedPreferencesManager.getRooms();

                Room currentRoom=null;
                int indexCurrentRoom=-1;

                //Retrieving the current room
                for(int i=0;i<listRooms.size();i++){
                    if(listRooms.get(i).getName().equals(listTitle)){
                        currentRoom=listRooms.get(i);
                        indexCurrentRoom=i;
                        break;
                    }
                }

                //Removing all water loads
                currentRoom.clearWaterLoads();

                //Adding water loads to the current room
                for (int i = 0; i < checkedWaterLoads.length; i++) {
                    listWaterLoads.get(i).setInRoom(checkedWaterLoads[i]);
                    if(listWaterLoads.get(i).isInRoom()){
                        currentRoom.addWaterLoad(listWaterLoads.get(i));
                    }
                }

                //Replacing the current room in the list
                listRooms.remove(indexCurrentRoom);
                listRooms.add(indexCurrentRoom,currentRoom);

                //Saving water loads and rooms
                sharedPreferencesManager.saveWaterLoads(listWaterLoads);
                sharedPreferencesManager.saveRooms(listRooms);
                refresh();
            }
        });

        builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    private void refresh(){
        this.notifyDataSetChanged();
    }

    public void showButton(){
        btnAddLoad.setVisibility(View.VISIBLE);
    }

    public void hideButton(){
        btnAddLoad.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }




}