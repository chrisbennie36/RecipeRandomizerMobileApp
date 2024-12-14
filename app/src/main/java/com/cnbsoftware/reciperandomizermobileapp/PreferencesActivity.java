package com.cnbsoftware.reciperandomizermobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserRecipePreferencesDto;
import com.cnbsoftware.reciperandomizermobileapp.enums.FoodCategoryType;
import com.cnbsoftware.reciperandomizermobileapp.enums.RecipePreferenceType;
import com.cnbsoftware.reciperandomizermobileapp.helpers.RecipeRandomizerHelper;
import com.cnbsoftware.reciperandomizermobileapp.managers.PreferencesActivityManager;
import com.cnbsoftware.reciperandomizermobileapp.managers.PreferencesManager;
import com.cnbsoftware.reciperandomizermobileapp.models.UserDietryRequirementsModel;

import java.net.MalformedURLException;
import java.util.ArrayList;

public abstract class PreferencesActivity extends AppCompatActivity {

    private PreferencesActivityManager preferencesActivityManager;
    private boolean showDietryRequirements;
    private boolean showCurrentUserPreferences;
    private UserRecipePreferencesDto userRecipePreferencesDto;
    private ArrayList<RecipePreferenceDto> configuredRecipePreferences;
    private ArrayList<RecipePreferenceDto> userRecipePreferences;
    private int userId;
    private boolean isFinalActivity;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.set_preferences);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            showDietryRequirements = bundle.getBoolean("ShowDietryRequirements");
            userRecipePreferences = bundle.getParcelableArrayList("UserRecipePreferences");
            userId = bundle.getInt("UserId");
            configuredRecipePreferences = bundle.getParcelableArrayList("ConfiguredRecipePreferences");

            showCurrentUserPreferences = userId != 0 && userRecipePreferences != null;

            if(showCurrentUserPreferences) {
                userRecipePreferencesDto = new UserRecipePreferencesDto(userId, userRecipePreferences);
            }
        }

        SetGui(configuredRecipePreferences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.setPreferencesView), (v, insets) -> {
            Button btnSavePreferences = (Button)findViewById(R.id.btnSavePreferences);
            preferencesActivityManager = new PreferencesActivityManager(DetermineNextFoodCategory(), this.getApplicationContext());
            btnSavePreferences.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SavePreferences(userRecipePreferencesDto);
                }
            });

            return insets;
        });
    }

    public void SetGeneralPreferences() {
        preferencesManager = new PreferencesManager(true, false, false, false);
    }

    public void SetSeafoodPreferences() {
        preferencesManager = new PreferencesManager(false, true, false, false);

    }

    public void SetVegetarianPreferences() {
        preferencesManager = new PreferencesManager(false, false, true, false);
    }

    public void SetMeatPreferences() {
        preferencesManager = new PreferencesManager(false, false, false, true);
    }

    public void SetFinalPreferencesActivity(boolean isFinalActivity) {
        this.isFinalActivity = isFinalActivity;
    }

    private FoodCategoryType DetermineNextFoodCategory() {
        if (preferencesManager.isGeneralPreferences) {
            return FoodCategoryType.Seafood;
        } else if (preferencesManager.isSeafoodPreferences) {
            return FoodCategoryType.Vegetarian;
        } else {
            return FoodCategoryType.Meat;
        }
    }

    private void SetGui(ArrayList<RecipePreferenceDto> preferences) {
        if(showDietryRequirements) {
            TextView tvCaption = findViewById(R.id.tvCaption);
            tvCaption.setText("Do you have specific dietry requirements?");
        }
        else {
            TextView tvCaption = findViewById(R.id.tvCaption);
            tvCaption.setText("Choose Recipe Preferences");
        }

        RelativeLayout rrPreferences = findViewById(R.id.llPreferences);
        //ScrollView sv = new ScrollView(getApplicationContext());
        //sv.addView(llPreferences);

        boolean isInitialCheckBox = true;
        int topMarginDp = 100;
        CheckBox previousCheckBox = null;

        for (RecipePreferenceDto preference: preferencesManager.determineRecipePreferencesToDisplay(preferences)) {
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(preference.Name);
            cb.setId(preference.Id);

            topMarginDp += 10;

            if(isInitialCheckBox) {
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.tvCaption);
                params.setMargins(0, topMarginDp, 0, 0);
                cb.setLayoutParams(params);
                isInitialCheckBox = false;
            } else {
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, previousCheckBox.getId());
                params.setMargins(0, topMarginDp, 0, 0);
                cb.setLayoutParams(params);
            }

            rrPreferences.addView(cb);
            previousCheckBox = cb;

            if(showCurrentUserPreferences) {
                preferencesManager.dataBindPreference(userRecipePreferencesDto.RecipePreferences, preference, cb);
            }
        }

        Button savePreferencesButton = new Button(this.getApplicationContext());
        savePreferencesButton.setId(R.id.btnSavePreferences);
        savePreferencesButton.setText("Save Preferences");
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, previousCheckBox.getId());
        params.setMargins(0, topMarginDp + 30, 0, 0);
        savePreferencesButton.setLayoutParams(params);
        rrPreferences.addView(savePreferencesButton);
    }

    //ToDo: Add a method which returns the changed preferences in a ChangedUserPreferencesModel to the extending class.
    //ToDo: This class then calls the new base SavePreferences method which accepts the userProfileDto, adds it to the bundle and starts the next activity
    public void SavePreferences(UserRecipePreferencesDto userRecipePreferencesDto) {
        Bundle bundle = getIntent().getExtras();

        if(bundle == null) {
            Log.e("SetPreferencesActivity", "Bundle is null, cannot retrieve User Id for Recipe Preferences");
        }

        ArrayList<RecipePreferenceDto> selectedPreferences = new ArrayList<>();
        ArrayList<RecipePreferenceDto> removedPreferences = new ArrayList<>();

        UserDietryRequirementsModel dietryRequirementsModel = new UserDietryRequirementsModel();

        for (RecipePreferenceDto preference: preferencesManager.determineRecipePreferencesToDisplay(configuredRecipePreferences)) {
            CheckBox cb = findViewById(preference.Id);

            if(cb.isChecked()) {
                selectedPreferences.add(preference);

                /*if (showDietryRequirements || showCurrentUserPreferences) {
                    MapDietryRequirements(dietryRequirementsModel, preference);
                }*/
            }
            else {
                if(showCurrentUserPreferences) {
                    if(userRecipePreferencesDto.HasPreference(r -> r.Id == preference.Id)) {
                        preference.Excluded = true;
                        removedPreferences.add(preference);
                    }
                }
            }
        }

        UserRecipePreferencesDto dto = new UserRecipePreferencesDto();

        //Get the Recipe Preferences which the user selected/removed in the previous Preferences activity
        dto.RecipePreferences = bundle.getParcelableArrayList("UserRecipePreferences");
        //Set the Recipe Preferences which the user selected/removed in the current Preferences activity
        dto = preferencesManager.mapChangedUserPreferences(dto, selectedPreferences, removedPreferences);

        bundle.putParcelableArrayList("UserRecipePreferences", dto.RecipePreferences);

        if(showDietryRequirements || showCurrentUserPreferences) {
            preferencesActivityManager.SetUserDietryRequirements(dietryRequirementsModel);
            //ToDo: Send Dietry requiements to API, start the following preference activity from within the recipeRandomizerHelper
        }

        if(showDietryRequirements) {
            bundle.putBoolean("ShowDietryRequirements", false);
        }

        if(isFinalActivity) {
            try {
                dto.UserId = bundle.getInt("UserId") != 0 ? bundle.getInt("UserId") : userRecipePreferencesDto.UserId;
                RecipeRandomizerHelper recipeRandomizerHelper = new RecipeRandomizerHelper(this.getApplicationContext(), new Intent(this.getApplicationContext(), MainActivity.class));
                recipeRandomizerHelper.SaveRecipePreferences(dto);
            } catch (MalformedURLException e) {
                Log.e("PreferencesActivity", "Malformed URL when creating the Recipe Randomizer");
            }
        } else {
            preferencesActivityManager.StartNextPreferencesActivity(bundle);
        }
    }

    private void MapDietryRequirements(UserDietryRequirementsModel model, int dietryRequirement) {
        if(RecipePreferenceType.valueOf(dietryRequirement) == RecipePreferenceType.Pescatarian) {
            model.Pescatarian = true;
        }
        if(RecipePreferenceType.valueOf(dietryRequirement) == RecipePreferenceType.Vegetarian) {
            model.Vegetarian = true;
        }
        if(RecipePreferenceType.valueOf(dietryRequirement) == RecipePreferenceType.Meat) {
            model.NoDietryRequirements = true;
        }
    }
}