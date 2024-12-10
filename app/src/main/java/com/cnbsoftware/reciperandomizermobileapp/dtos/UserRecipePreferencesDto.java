package com.cnbsoftware.reciperandomizermobileapp.dtos;

import java.util.ArrayList;
import java.util.function.Predicate;

public class UserRecipePreferencesDto {
    public int UserId;
    public ArrayList<RecipePreferenceDto> RecipePreferences;

    public UserRecipePreferencesDto() {
        RecipePreferences = new ArrayList<>();
    }

    public UserRecipePreferencesDto(int userId, ArrayList<RecipePreferenceDto> recipePreferences) {
        UserId = userId;
        RecipePreferences = recipePreferences;
    }

    public boolean HasRemovedPreferences(Predicate<RecipePreferenceDto> predicate) {
        ArrayList<RecipePreferenceDto> removedPreferences = new ArrayList<>();

        for(RecipePreferenceDto removedPreference : RecipePreferences
                .stream()
                .filter(predicate)
                .toArray(RecipePreferenceDto[]::new)) {
            removedPreferences.add(removedPreference);
        }

        return !removedPreferences.isEmpty();
    }

    public boolean HasPreference(Predicate<RecipePreferenceDto> predicate) {
        return RecipePreferences.stream().anyMatch(predicate);
    }
}
