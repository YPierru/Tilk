package com.tilk.activity.communautilk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tilk.R;
import com.tilk.adapter.FriendCardAdapter;
import com.tilk.models.FriendTilkeur;
import com.tilk.models.ProfilTilkeur;
import com.tilk.models.UserProfil;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {

    private FriendCardAdapter friendCardAdapter;
    private ListView lvFriends;
    private UserProfil userProfil;
    private boolean editMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends_activity);
        toolbar.setTitle("Mes amis");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userProfil = new UserProfil(FriendActivity.this);
        ArrayList<FriendTilkeur> listFriends = new ArrayList<>();
        listFriends.add(new FriendTilkeur(1,"Christophe",20000));
        listFriends.add(new FriendTilkeur(2,"Nils",15000));
        final ProfilTilkeur profilTilkeur = userProfil.getProfilTilkeur();
        profilTilkeur.setListFriends(listFriends);
        userProfil.setProfilTilkeur(profilTilkeur);
        lvFriends = (ListView)findViewById(R.id.lv_friends);

        friendCardAdapter = new FriendCardAdapter(this,R.layout.item_card_friend_view,profilTilkeur.getListFriends(),editMode);
        lvFriends.setAdapter(friendCardAdapter);


        FloatingActionButton fabAddRoom = (FloatingActionButton)findViewById(R.id.fab_add_friend);

        fabAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilTilkeur.addFriend(new FriendTilkeur(3,"Tilkeur",35000));
                userProfil.setProfilTilkeur(profilTilkeur);
                friendCardAdapter.notifyDataSetChanged();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId()==R.id.menu_edit){
            ProfilTilkeur profilTilkeur = userProfil.getProfilTilkeur();
            if(!editMode){
                editMode=true;
                friendCardAdapter = new FriendCardAdapter(this,R.layout.item_card_friend_edit,profilTilkeur.getListFriends(),editMode);
            }else{
                editMode=false;
                friendCardAdapter = new FriendCardAdapter(this,R.layout.item_card_friend_view,profilTilkeur.getListFriends(),editMode);
            }
            lvFriends.setAdapter(friendCardAdapter);

        }

        return super.onOptionsItemSelected(item);
    }
}
