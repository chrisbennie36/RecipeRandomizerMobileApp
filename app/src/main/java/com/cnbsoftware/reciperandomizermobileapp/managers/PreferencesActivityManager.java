package com.cnbsoftware.reciperandomizermobileapp.managers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cnbsoftware.reciperandomizermobileapp.SetMeatPreferencesActivity;
import com.cnbsoftware.reciperandomizermobileapp.SetSeafoodPreferencesActivity;
import com.cnbsoftware.reciperandomizermobileapp.SetVegetarianPreferencesActivity;
import com.cnbsoftware.reciperandomizermobileapp.enums.FoodCategoryType;
import com.cnbsoftware.reciperandomizermobileapp.models.UserDietryRequirementsModel;

public class PreferencesActivityManager {
    private FoodCategoryType nextFoodCategory;
    private ActivityManager activityManager;
    private static UserDietryRequirementsModel userDietryRequirements;

    // Meat => Seafood => Vegetarian if no requirements
    public PreferencesActivityManager(FoodCategoryType nextFoodCategory, Activity currentActivity) {
        this.nextFoodCategory = nextFoodCategory;
        this.activityManager = new ActivityManager(currentActivity);
    }

    public void SetUserDietryRequirements(UserDietryRequirementsModel userDietryRequirements) {
        this.userDietryRequirements = userDietryRequirements;
    }

    public UserDietryRequirementsModel GetUserDietryRequirements() {
        return this.userDietryRequirements;
    }

    public void StartNextPreferencesActivity(Bundle bundle) {

        if(userDietryRequirements.NoDietryRequirements) {
            StartNextPreferencesActivityBasedOnFoodCategory(bundle);
            return;
        }

        StartNextActivityBasedOnDietryRequirements(bundle);
    }

    private void StartNextActivityBasedOnDietryRequirements(Bundle bundle) {
        //Intent nextPreferencesIntent;
        if(userDietryRequirements.Pescatarian) {
            activityManager.OpenActivity(SetSeafoodPreferencesActivity.class, bundle);
        }
        else if(userDietryRequirements.Vegetarian) {
            activityManager.OpenActivity(SetVegetarianPreferencesActivity.class, bundle);
        }
        else {
            Log.e("PreferencesActivityManager", "Could not start activity for an unmapped dietry requirement");
        }
    }

    private void StartNextPreferencesActivityBasedOnFoodCategory(Bundle bundle) {
        Intent nextPreferencesIntent = null;
        switch(nextFoodCategory) {
            case Seafood:
                activityManager.OpenActivity(SetSeafoodPreferencesActivity.class, bundle);
                break;
            case Vegetarian:
                activityManager.OpenActivity(SetVegetarianPreferencesActivity.class, bundle);
                break;
            case Meat:
                activityManager.OpenActivity(SetMeatPreferencesActivity.class, bundle);
                break;
            default:
                Log.e("PreferencesActivityManager", String.format("Could not open Preferences Activity for FoodCategoryType %s", nextFoodCategory));
                break;
        }
    }
}
