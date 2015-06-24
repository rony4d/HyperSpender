package com.example.ugochukwu.hyperspender;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by ugochukwu on 6/18/2015.
 */
public class ActivitySettings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
