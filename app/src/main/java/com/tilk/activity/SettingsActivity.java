package com.tilk.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tilk.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
