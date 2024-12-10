package com.cnbsoftware.reciperandomizermobileapp;

import android.os.Bundle;

public class SetMeatPreferencesActivity extends PreferencesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.SetMeatPreferences();
        super.SetFinalPreferencesActivity(true);
        super.onCreate(savedInstanceState);
    }
}