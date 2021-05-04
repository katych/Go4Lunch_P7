package com.example.go4lunch.language;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.go4lunch.R;

public class LanguageFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.language_settings, rootKey);



    }
}
