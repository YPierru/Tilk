package com.tilk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tilk.R;

public class SettingsList extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] settings;
    private ListView listSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_settings_list);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       settings=new String[]{getString(R.string.settings_sensors),getString(R.string.settings_tilk)};

        listSettings = (ListView)findViewById(R.id.list_types_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsList.this,android.R.layout.simple_list_item_1, settings);
        listSettings.setAdapter(adapter);

        listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = settings[i];

                if(selected.equals(settings[0])){
                    startActivity(new Intent(SettingsList.this,SettingsSensor.class));
                }else if(selected.equals(settings[1])){
                    startActivity(new Intent(SettingsList.this,SettingsTilk.class));
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
