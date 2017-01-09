package com.tilk.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.tilk.R;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;


/**
 * Created by YPierru on 06/01/2017.
 */

public class SettingsFragment extends PreferenceFragment  {



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);
        final Preference pref = findPreference("checkbox_rooms");

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            int count=0;
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getActivity());
                if(count%2==0) {
                    sharedPreferencesManager.setMustBeRestarted(true);
                    Logger.logI(""+sharedPreferencesManager.mustBeRestarted());
                }
                count++;

                return false;
            }
        });

    }

    /*private class MyOnPreferenceClickListener implements Preference.OnPreferenceClickListener{

        int count=0;


        @Override
        public boolean onPreferenceClick(Preference preference) {
            return false;
        }
    }*/




}
