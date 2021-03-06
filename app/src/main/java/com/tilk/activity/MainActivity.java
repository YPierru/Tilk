package com.tilk.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.tilk.R;
import com.tilk.activity.communautilk.CompareActivity;
import com.tilk.activity.communautilk.FriendActivity;
import com.tilk.activity.communautilk.MessagesActivity;
import com.tilk.activity.communautilk.ProfilActivity;
import com.tilk.adapter.ViewPagerAdapter;
import com.tilk.fragment.ResumeFragment;
import com.tilk.fragment.RoomFragment;
import com.tilk.fragment.WaterLoadFragment;
import com.tilk.models.Room;
import com.tilk.models.UserProfil;
import com.tilk.models.WaterLoad;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    private Bundle savedInstanceState;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private UserProfil userProfil;

    private ArrayList<WaterLoad> listWaterLoads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        setContentView(R.layout.activity_main);
        Logger.enableLog();

        userProfil =new UserProfil(MainActivity.this);

        //Logger.logI(""+userProfil.getProfilTilkeur().getListFriends().size());

        if(userProfil.getFirstRun()){
            RetrieveLoads retrieveLoads = new RetrieveLoads();
            try {
                listWaterLoads=retrieveLoads.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            userProfil.saveWaterLoads(listWaterLoads);
            userProfil.setFirstRunKO();
        }else{
            listWaterLoads = userProfil.getWaterLoads();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);
        initNavigationDrawer();

    }

    private void initNavigationDrawer(){
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.banner)
                .withSavedInstance(savedInstanceState)
                .addProfiles(
                        new ProfileDrawerItem().withName(userProfil.getUserSurname()).withEmail(userProfil.getEmailUser())
                )
                .withSelectionListEnabled(false)
                .build();


        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header.
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Accueil")
                                .withIcon(R.drawable.ic_logo_tilk)
                                .withIdentifier(Constants.ID_ITEM_ACCUEIL),
                        new PrimaryDrawerItem()
                                .withName("Préférences")
                                .withIcon(R.drawable.ic_settings_black)
                                .withSelectable(false)
                                .withIdentifier(Constants.ID_ITEM_SETTINGS),
                        new PrimaryDrawerItem()
                                .withName("Déconnexion")
                                .withIcon(R.drawable.ic_logout_black)
                                .withSelectable(false)
                                .withIdentifier(Constants.ID_ITEM_LOGOUT),
                        new SectionDrawerItem()
                                .withName("CommunauTilk"),
                        new SwitchDrawerItem()
                                .withName("Activer/Désactiver")
                                .withSelectable(false)
                                .withOnCheckedChangeListener(onCheckedChangeListener)
                                .withIdentifier(Constants.ID_ITEM_SWITCH_CT),
                        new PrimaryDrawerItem()
                                .withName("Mon profil")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_my_profil)
                                .withIdentifier(Constants.ID_ITEM_PROFIL),
                        new PrimaryDrawerItem()
                                .withName("Mes amis Tilkeurs")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_tilkeurs)
                                .withIdentifier(Constants.ID_ITEM_TILKEURS),
                        new PrimaryDrawerItem()
                                .withName("Se comparer")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_compare)
                                .withIdentifier(Constants.ID_ITEM_COMPARE),
                        new PrimaryDrawerItem()
                                .withName("Messagerie")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_messages)
                                .withIdentifier(Constants.ID_ITEM_MESSAGES),
                        new PrimaryDrawerItem()
                                .withName("Badges")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_badges)
                                .withIdentifier(Constants.ID_ITEM_BADGES),
                        new PrimaryDrawerItem()
                                .withName("Confidentialité")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_privacy)
                                .withIdentifier(Constants.ID_ITEM_PRIVACY)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable && drawerItem != null) {
                            if(drawerItem.getIdentifier()==Constants.ID_ITEM_SETTINGS){
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            }
                            else if(drawerItem.getIdentifier()==Constants.ID_ITEM_LOGOUT){
                                userProfil.setUserOffline();
                                finish();
                            }
                            else if(drawerItem.getIdentifier()==Constants.ID_ITEM_PROFIL){
                                Intent intent = new Intent(MainActivity.this,ProfilActivity.class);
                                intent.putExtra("edit_mode",false);
                                startActivity(intent);
                            }
                            else if(drawerItem.getIdentifier()==Constants.ID_ITEM_TILKEURS){
                                startActivity(new Intent(MainActivity.this,FriendActivity.class));
                            }
                            else if(drawerItem.getIdentifier()==Constants.ID_ITEM_COMPARE){
                                startActivity(new Intent(MainActivity.this,CompareActivity.class));
                            }
                            else if(drawerItem.getIdentifier()==Constants.ID_ITEM_MESSAGES){
                                startActivity(new Intent(MainActivity.this,MessagesActivity.class));
                            }
                        }

                        return false;
                    }
                })
                .withSelectedItem(Constants.ID_ITEM_ACCUEIL)
                .withSavedInstance(savedInstanceState)
                .build();

        SwitchDrawerItem sdi = (SwitchDrawerItem)result.getDrawerItem(Constants.ID_ITEM_SWITCH_CT);
        sdi.withChecked(false);
        result.updateItem(sdi);

        setCommunauTilk(false);

        DrawerLayout drawer = result.getDrawerLayout();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {


            if(userProfil.getFirstCommunautilk()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Bienvenue dans la CommunauTilk !")
                        .setMessage("C'est votre première venue dans la CommunauTilk, avant tout il vous faut créer un profil de Tilkeur !")
                        .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setCommunauTilk(false);
                                userProfil.setCommunautilkStatus(false);
                            }
                        })
                        .setPositiveButton("Créer mon profil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setCommunauTilk(true);
                                userProfil.setCommunautilkStatus(true);
                                userProfil.setFirstCommunautilk(false);
                                Intent intent = new Intent(MainActivity.this,ProfilActivity.class);
                                intent.putExtra("edit_mode",true);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_help)
                        .show();
            }else{
                setCommunauTilk(isChecked);
                userProfil.setCommunautilkStatus(isChecked);
            }


        }
    };

    private void setCommunauTilk(boolean enable){
        PrimaryDrawerItem pdi;
        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_PROFIL);
        pdi.withEnabled(enable);
        result.updateItem(pdi);

        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_TILKEURS);
        pdi.withEnabled(enable);
        result.updateItem(pdi);

        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_COMPARE);
        pdi.withEnabled(enable);
        result.updateItem(pdi);

        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_MESSAGES);
        pdi.withEnabled(enable);
        result.updateItem(pdi);

        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_BADGES);
        pdi.withEnabled(enable);
        result.updateItem(pdi);

        pdi = (PrimaryDrawerItem) result.getDrawerItem(Constants.ID_ITEM_PRIVACY);
        pdi.withEnabled(enable);
        result.updateItem(pdi);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(userProfil.mustBeRestarted()){
            userProfil.setMustBeRestarted(false);
            restartApp();
        }

    }

    private void restartApp(){
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.clearLists();
        ArrayList<Room> listRooms = userProfil.getRooms();
        adapter.addFragment(new ResumeFragment(), getString(R.string.tab_resume));

        if(userProfil.isRoomOrganisation()){
            for(Room room : listRooms){
                adapter.addFragment(RoomFragment.newInstance(room),room.getName());
            }
        }else{
            for(WaterLoad waterLoad : listWaterLoads){
                if(waterLoad.getStatus()==1) {
                    adapter.addFragment(WaterLoadFragment.newInstance(waterLoad), waterLoad.getName());
                }
            }
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_preferences:
            {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }

            case R.id.menu_logout:
            {
                userProfil.setUserOffline();
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }


    private class RetrieveLoads extends AsyncTask<Void,Void,ArrayList<WaterLoad>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.progressdialog_label_init));
            progressDialog.show();
        }

        @Override
        protected ArrayList<WaterLoad> doInBackground(Void... args) {
            ArrayList<WaterLoad> listWaterLoads = new ArrayList<>();
            try {

                String response = HttpPostManager.sendPost("tilk_id="+userProfil.getTilkId(),Constants.URL_GET_LOADS);

                JSONObject jsonObject = new JSONObject(response);
                JSONArray arrayLoads = jsonObject.getJSONArray("response");

                String name;
                int status;
                int id;

                for(int i=0;i<arrayLoads.length();i++){
                    id = arrayLoads.getJSONObject(i).getInt("id");
                    name = arrayLoads.getJSONObject(i).getString("name");
                    status = Integer.parseInt(arrayLoads.getJSONObject(i).getString("status"));

                    listWaterLoads.add(new WaterLoad(id,name,status,0,false));
                }


            }catch(Exception e){
                e.printStackTrace();
            }

            return listWaterLoads;
        }

        @Override
        protected void onPostExecute(ArrayList<WaterLoad> result) {
            progressDialog.dismiss();
        }
    }

}
