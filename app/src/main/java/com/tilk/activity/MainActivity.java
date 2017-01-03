package com.tilk.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tilk.R;
import com.tilk.adapter.ViewPagerAdapter;
import com.tilk.fragment.PosteFragment;
import com.tilk.fragment.ResumeFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ResumeFragment(), "Résumé");
        adapter.addFragment(new PosteFragment(), "Douche");
        adapter.addFragment(new PosteFragment(), "Lave-vaisselle");
        adapter.addFragment(new PosteFragment(), "Toilettes");
        adapter.addFragment(new PosteFragment(), "Machine à laver");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem itemSettings = menu.add(getResources().getString(R.string.menu_item_settings)).setIcon(R.drawable.ic_settings);
        itemSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,"Afficher paramètres",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return true;
    }

}
