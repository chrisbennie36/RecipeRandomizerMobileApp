package com.cnbsoftware.reciperandomizermobileapp.managers;

import android.content.Context;
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
    private Context currentActivityContext;
    private static UserDietryRequirementsModel userDietryRequirements;

    // Meat => Seafood => Vegetarian if no requirements
    public PreferencesActivityManager(FoodCategoryType nextFoodCategory, Context currentActivityContext) {
        this.nextFoodCategory = nextFoodCategory;
        this.currentActivityContext = currentActivityContext;
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
        Intent nextPreferencesIntent;
        if(userDietryRequirements.Pescatarian) {
            nextPreferencesIntent = new Intent(currentActivityContext, SetSeafoodPreferencesActivity.class);
        }
        else if(userDietryRequirements.Vegetarian) {
            nextPreferencesIntent = new Intent(currentActivityContext, SetVegetarianPreferencesActivity.class);
        }
        else {
            Log.e("PreferencesActivityManager", "Could not start activity for an unmapped dietry requirement");
            return;
        }

        nextPreferencesIntent.putExtras(bundle);
        nextPreferencesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivityContext.startActivity(nextPreferencesIntent);
    }

    private void StartNextPreferencesActivityBasedOnFoodCategory(Bundle bundle) {
        Intent nextPreferencesIntent = null;
        switch(nextFoodCategory) {
            case Seafood:
                nextPreferencesIntent = new Intent(currentActivityContext, SetSeafoodPreferencesActivity.class);
                break;
            case Vegetarian:
                nextPreferencesIntent = new Intent(currentActivityContext, SetVegetarianPreferencesActivity.class);
                break;
            case Meat:
                nextPreferencesIntent = new Intent(currentActivityContext, SetMeatPreferencesActivity.class);
                break;
            default:
                break;
        }

        if (nextPreferencesIntent == null) {
            Log.e("PreferencesActivityManager", String.format("Could not construct next intent for FoodCategoryType %s", nextFoodCategory));
            return;
        }

        nextPreferencesIntent.putExtras(bundle);
        nextPreferencesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivityContext.startActivity(nextPreferencesIntent);
    }
}
