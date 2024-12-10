package com.cnbsoftware.reciperandomizermobileapp.models;

import java.util.ArrayList;

public class ChangedUserPreferencesModel {
    public ChangedUserPreferencesModel(ArrayList<Integer> selectedPreferences, ArrayList<Integer> deselectedPreferences) {
        SelectedPreferences = selectedPreferences;
        DeselectedPreferences = deselectedPreferences;
    }

    private ArrayList<Integer> SelectedPreferences;
    private ArrayList<Integer> DeselectedPreferences;
}
