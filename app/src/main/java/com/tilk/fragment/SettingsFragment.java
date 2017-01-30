package com.tilk.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.tilk.R;
import com.tilk.utils.Logger;
import com.tilk.models.UserProfil;


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
                UserProfil userProfil = new UserProfil(getActivity());
                if(count%2==0) {
                    userProfil.setMustBeRestarted(true);
                    Logger.logI(""+userProfil.mustBeRestarted());
                }
                count++;

                return false;
            }
        });

    }

}
