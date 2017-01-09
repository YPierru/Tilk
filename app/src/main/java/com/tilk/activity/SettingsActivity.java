package com.tilk.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tilk.fragment.SettingsFragment;
import com.tilk.utils.SharedPreferencesManager;

public class SettingsActivity extends Activity {

    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
