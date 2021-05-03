package com.example.go4lunch.notifications;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.go4lunch.R;

public class SettingsHeaders extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_headers, rootKey);
    }
}
