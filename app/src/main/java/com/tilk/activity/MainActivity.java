package com.tilk.activity;

import android.app.ProgressDialog;
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
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.tilk.R;
import com.tilk.adapter.ViewPagerAdapter;
import com.tilk.fragment.ResumeFragment;
import com.tilk.fragment.RoomFragment;
import com.tilk.fragment.WaterLoadFragment;
import com.tilk.models.Room;
import com.tilk.models.WaterLoad;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;

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

    private SharedPreferencesManager sessionManager;

    private ArrayList<WaterLoad> listWaterLoads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        setContentView(R.layout.activity_main);
        Logger.enableLog();

        sessionManager=new SharedPreferencesManager(MainActivity.this);

        if(sessionManager.getFirstRun()){
            RetrieveLoads retrieveLoads = new RetrieveLoads();
            try {
                listWaterLoads=retrieveLoads.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            sessionManager.saveWaterLoads(listWaterLoads);
            sessionManager.setFirstRunKO();
        }else{
            listWaterLoads = sessionManager.getWaterLoads();
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
                .build();


        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header.
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Accueil")
                                .withIcon(R.drawable.ic_logo_tilk)
                                .withIdentifier(Constants.ID_ITEM_ACCUEIL),
                        new PrimaryDrawerItem()
                                .withName("Préférences")
                                .withIcon(R.drawable.ic_settings_black)
                                .withIdentifier(Constants.ID_ITEM_SETTINGS),
                        new PrimaryDrawerItem()
                                .withName("Déconnexion")
                                .withIcon(R.drawable.ic_logout_black)
                                .withIdentifier(Constants.ID_ITEM_LOGOUT),
                        new SectionDrawerItem()
                                .withName("CommunauTilk"),
                        new SwitchDrawerItem()
                                .withName("Actvier/Désactiver")
                                .withOnCheckedChangeListener(onCheckedChangeListener)
                                .withIdentifier(Constants.ID_ITEM_SWITCH_CT),
                        new PrimaryDrawerItem()
                                .withName("Mon profil")
                                .withIcon(R.drawable.ic_my_profil)
                                .withIdentifier(Constants.ID_ITEM_PROFIL),
                        new PrimaryDrawerItem()
                                .withName("Mes amis Tilkeurs")
                                .withIcon(R.drawable.ic_tilkeurs)
                                .withIdentifier(Constants.ID_ITEM_TILKEURS),
                        new PrimaryDrawerItem()
                                .withName("Se comparer")
                                .withIcon(R.drawable.ic_compare)
                                .withIdentifier(Constants.ID_ITEM_COMPARE),
                        new PrimaryDrawerItem()
                                .withName("Messagerie")
                                .withIcon(R.drawable.ic_messages)
                                .withIdentifier(Constants.ID_ITEM_MESSAGES),
                        new PrimaryDrawerItem()
                                .withName("Badges")
                                .withIcon(R.drawable.ic_badges)
                                .withIdentifier(Constants.ID_ITEM_BADGES),
                        new PrimaryDrawerItem()
                                .withName("Confidentialité")
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
                                sessionManager.setUserOffline();
                                finish();
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
            setCommunauTilk(isChecked);
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

        if(sessionManager.mustBeRestarted()){
            sessionManager.setMustBeRestarted(false);
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
        ArrayList<Room> listRooms = sessionManager.getRooms();
        adapter.addFragment(new ResumeFragment(), getString(R.string.tab_resume));

        if(sessionManager.isRoomOrganisation()){
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
                sessionManager.setUserOffline();
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

                String response = HttpPostManager.sendPost("",Constants.URL_GET_LOADS);

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
