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
import android.view.MenuInflater;
import android.view.MenuItem;

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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

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

        // today
        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date currentDay = calendar.getTime();
        Date referenceDay = sessionManager.getReferenceDate();

        //
        if(currentDay.compareTo(referenceDay)>0){
            for(WaterLoad waterLoad : listWaterLoads){

            }
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);

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
