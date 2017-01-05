package com.tilk.activity;

import android.content.Intent;
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
import com.tilk.utils.Constants;
import com.tilk.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager=new SessionManager(MainActivity.this);

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
        adapter.addFragment(new PosteFragment(), getString(R.string.tab_douche));
        adapter.addFragment(new PosteFragment(), getString(R.string.tab_lvaisselle));
        adapter.addFragment(new PosteFragment(), getString(R.string.tab_toilettes));
        adapter.addFragment(new PosteFragment(), getString(R.string.tab_machine));


        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem itemSettings = menu.add(getResources().getString(R.string.menu_item_settings)).setIcon(R.drawable.ic_settings);
        itemSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this,SettingsList.class));
                return false;
            }
        });

        MenuItem itemLogout = menu.add(getString(R.string.menu_item_logout)).setIcon(R.drawable.ic_logout);
        itemLogout.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.setPreferences(Constants.SESSION_STATUS,Constants.STATUS_CODE_OFFLINE);
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

}
