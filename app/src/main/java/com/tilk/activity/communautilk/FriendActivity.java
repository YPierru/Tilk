package com.tilk.activity.communautilk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.tilk.R;
import com.tilk.adapter.FriendCardAdapter;
import com.tilk.models.ProfilTilkeur;
import com.tilk.models.UserProfil;
import com.tilk.utils.Logger;

public class FriendActivity extends AppCompatActivity {

    private FriendCardAdapter friendCardAdapter;
    private ListView lvFriends;
    private UserProfil userProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends_activity);
        toolbar.setTitle("CommunauTilk - Mes amis");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userProfil = new UserProfil(this);
        ProfilTilkeur profilTilkeur = userProfil.getProfilTilkeur();
        lvFriends = (ListView)findViewById(R.id.lv_friends);
        Logger.logI(""+profilTilkeur.getListFriends().size());
        friendCardAdapter = new FriendCardAdapter(this,R.layout.item_card_friend,profilTilkeur.getListFriends());
        lvFriends.setAdapter(friendCardAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
