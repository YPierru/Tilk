package com.tilk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by YPierru on 05/01/2017.
 */

public class SessionManager {

    private Context context;

    public SessionManager(Context cont){
        context=cont;
    }

    public void setPreferences(String key, int value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("Tilk", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public int getPreferences(String key) {

        SharedPreferences prefs = context.getSharedPreferences("Tilk", Context.MODE_PRIVATE);
        return prefs.getInt(key, -1);
    }
}
