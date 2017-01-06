package com.tilk.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tilk.R;
import com.tilk.adapter.ViewPagerAdapter;
import com.tilk.fragment.PosteFragment;
import com.tilk.fragment.ResumeFragment;
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

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SharedPreferencesManager sessionManager;

    private ArrayList<WaterLoad> listWaterLoads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ResumeFragment(), getString(R.string.tab_resume));

        for(WaterLoad waterLoad : listWaterLoads){
            if(waterLoad.getStatus()==1) {
                adapter.addFragment(new PosteFragment(), waterLoad.getName());
            }
        }

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem itemSettings = menu.add(getResources().getString(R.string.menu_item_settings)).setIcon(R.drawable.ic_settings);
        itemSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this,SettingsListActivity.class));
                return false;
            }
        });

        MenuItem itemLogout = menu.add(getString(R.string.menu_item_logout)).setIcon(R.drawable.ic_logout);
        itemLogout.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.setUserOffline();
                finish();
                return false;
            }
        });

        return true;
    }

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

                for(int i=0;i<arrayLoads.length();i++){
                    name = arrayLoads.getJSONObject(i).getString("name");
                    status = Integer.parseInt(arrayLoads.getJSONObject(i).getString("status"));

                    listWaterLoads.add(new WaterLoad(name,status,0));
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
