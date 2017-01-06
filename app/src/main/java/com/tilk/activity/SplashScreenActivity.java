package com.tilk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tilk.R;
import com.tilk.utils.SharedPreferencesManager;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferencesManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sessionManager=new SharedPreferencesManager(SplashScreenActivity.this);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(2000);

                    // After 5 seconds redirect to another intent
                    boolean userStatus=sessionManager.getUserStatus();

                    if (userStatus){
                        Intent i=new Intent(SplashScreenActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(SplashScreenActivity.this,LoginActivity.class);
                        startActivity(i);
                    }


                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
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
