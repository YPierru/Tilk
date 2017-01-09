package com.tilk.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.tilk.R;


/**
 * Created by YPierru on 06/01/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);
    }
}
