package com.tilk.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tilk.R;
import com.tilk.adapter.CustomExpandableListAdapter;
import com.tilk.models.Room;
import com.tilk.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RoomsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ExpandableListView expandableListView;
    private FloatingActionButton fabAddRoom;
    private CustomExpandableListAdapter expandableListAdapter;

    private List<Room> listRooms;
    private List<String> listRoomsName;
    private HashMap<String,List<String>> listRoomsNameLoadsName;
    private SharedPreferencesManager sharedPreferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        toolbar = (Toolbar) findViewById(R.id.toolbar_rooms_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        //Retrieve listRooms
        listRoomsName = new ArrayList<>();
        listRoomsNameLoadsName = new HashMap<>();
        syncLists();

        expandableListView = (ExpandableListView) findViewById(R.id.elv_rooms);
        fabAddRoom = (FloatingActionButton)findViewById(R.id.fab_add_room);

        expandableListAdapter = new CustomExpandableListAdapter(this,sharedPreferencesManager, listRoomsName, listRoomsNameLoadsName);

        expandableListView.setAdapter(expandableListAdapter);

        fabAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAddRoom();
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                expandableListAdapter.showButton();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                expandableListAdapter.hideButton();
            }
        });

        expandableListView.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                    Log.i("TAG",""+groupPosition);


                    alertDialogDeleteRoom(listRooms.get(groupPosition));

                    Toast.makeText(getApplicationContext(),
                            "long click title"+listRoomsName.get(groupPosition),
                            Toast.LENGTH_SHORT)
                            .show();
                    expandableListView.collapseGroup(groupPosition);
                    return true;
                }

                return false;
            }
        });


    }


    private void syncLists(){
        listRooms=sharedPreferencesManager.getRooms();
        listRoomsName.clear();
        listRoomsNameLoadsName.clear();
        for(Room room : listRooms){
            listRoomsName.add(room.getName());
            listRoomsNameLoadsName.put(room.getName(),room.getListWaterLoadsName());
        }
    }

    private void refreshELV(){
        expandableListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rooms_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }

            case R.id.menu_logout:
            {
                alertDialogHelp();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void alertDialogHelp(){
        new AlertDialog.Builder(this)
                .setTitle("Aide")
                .setMessage("On va tilker, sur une étwaaaaaleu sur un oreiyééééééé")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_help_dark)
                .show();
    }

    private void alertDialogDeleteRoom(final Room room){
        new AlertDialog.Builder(this)
                .setTitle("Supprimer la pièce "+room.getName()+" ?")
                .setMessage("Cette action va supprimer la pièce de façon irréversible. Les postes affectés à cette pièce ne seront pas supprimés")
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int index=-1;

                        for(int j=0;j<listRooms.size();j++){
                            if(listRooms.get(j).getName().equals(room.getName())){
                                index=j;
                                break;
                            }
                        }
                        room.clearWaterLoads();
                        listRooms.remove(index);
                        sharedPreferencesManager.saveRooms(listRooms);
                        syncLists();
                        refreshELV();
                    }
                })
                .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_help_dark)
                .show();
    }

    private void alertDialogAddRoom(){
        final EditText edittext = new EditText(this);

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=70;
        params.rightMargin=70;
        edittext.setLayoutParams(params);
        container.addView(edittext);

        new AlertDialog.Builder(this)
                .setTitle("Ajouter une pièce")
                .setMessage("Saisissez le nom de la pièce à ajouter")
                .setView(container)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newLabel=edittext.getText().toString();
                        listRooms.add(new Room(newLabel));
                        sharedPreferencesManager.saveRooms(listRooms);
                        syncLists();
                        refreshELV();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .setIcon(R.drawable.ic_add_room_dark)
                .show();

    }
}
