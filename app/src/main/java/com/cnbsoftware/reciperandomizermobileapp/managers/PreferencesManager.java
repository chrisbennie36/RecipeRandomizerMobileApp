package com.cnbsoftware.reciperandomizermobileapp.managers;

import android.util.Log;
import android.widget.CheckBox;

import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserRecipePreferencesDto;
import com.cnbsoftware.reciperandomizermobileapp.enums.FoodCategoryType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

public class PreferencesManager {

    public final boolean isGeneralPreferences;
    public final boolean isSeafoodPreferences;
    public final boolean isVegetarianPreferences;
    public final boolean isMeatPreferences;

    public PreferencesManager(boolean isGeneralPreferences, boolean isSeafoodPreferences, boolean isVegetarianPreferences, boolean isMeatPreferences)
    {
        this.isGeneralPreferences = isGeneralPreferences;
        this.isSeafoodPreferences = isSeafoodPreferences;
        this.isVegetarianPreferences = isVegetarianPreferences;
        this.isMeatPreferences = isMeatPreferences;
    }

    public void dataBindPreference(ArrayList<RecipePreferenceDto> userRecipePreferences, RecipePreferenceDto preference, CheckBox cb) {
        for(RecipePreferenceDto userRecipePreference : userRecipePreferences) {
            if(userRecipePreference.Id == preference.Id && !userRecipePreference.Excluded) {
                cb.setChecked(true);
                break;
            }
        }
    }

    public ArrayList<RecipePreferenceDto> determineRecipePreferencesToDisplay(ArrayList<RecipePreferenceDto> configuredRecipePreferences) {
        if (isGeneralPreferences) {
            return getConfiguredPreferencesOfType(configuredRecipePreferences, r -> r.Type == FoodCategoryType.General.getValue());
        } else if (isSeafoodPreferences) {
            return getConfiguredPreferencesOfType(configuredRecipePreferences, r -> r.Type == FoodCategoryType.Seafood.getValue());
        } else if (isVegetarianPreferences) {
            return getConfiguredPreferencesOfType(configuredRecipePreferences, r -> r.Type == FoodCategoryType.Vegetarian.getValue());
        } else if (isMeatPreferences) {
            return getConfiguredPreferencesOfType(configuredRecipePreferences, r -> r.Type == FoodCategoryType.Meat.getValue());
        } else {
            Log.w("PreferencesManager", "Encountered unmapped preference type when retrieving configured recipe preferences for type");
            return new ArrayList<>();
        }
    }

    private ArrayList<RecipePreferenceDto> getConfiguredPreferencesOfType(ArrayList<RecipePreferenceDto> configuredRecipePreferences, Predicate<RecipePreferenceDto> recipePreferencePredicate) {
        ArrayList<RecipePreferenceDto> configuredRecipePreferencesOfType = new ArrayList<>();

        for(RecipePreferenceDto configuredRecipePreference : configuredRecipePreferences
                .stream()
                .filter(recipePreferencePredicate)
                .toArray(RecipePreferenceDto[]::new)) {
            configuredRecipePreferencesOfType.add(configuredRecipePreference);

        }

        return configuredRecipePreferencesOfType;
    }

    public UserRecipePreferencesDto mapChangedUserPreferences(UserRecipePreferencesDto dto, ArrayList<RecipePreferenceDto> selectedPreferences, ArrayList<RecipePreferenceDto> removedPreferences) {

        if(dto == null) {
            dto = new UserRecipePreferencesDto();
        }

        if(dto.RecipePreferences == null) {
            dto.RecipePreferences = new ArrayList<>();
        }

        dto.RecipePreferences.addAll(selectedPreferences);
        dto.RecipePreferences.addAll(removedPreferences);

        return dto;
    }
}
