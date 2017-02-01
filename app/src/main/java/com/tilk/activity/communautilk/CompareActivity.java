package com.tilk.activity.communautilk;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tilk.R;

public class CompareActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Comparaison");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "calibri_bold.ttf");

        TextView tv = (TextView)findViewById(R.id.tv_vous);
        tv.setTypeface(custom_font);
        tv = (TextView)findViewById(R.id.tv_christophe);
        tv.setTypeface(custom_font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_compare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId()==R.id.menu_messaging){
            //TODO
        }

        return super.onOptionsItemSelected(item);
    }
}
