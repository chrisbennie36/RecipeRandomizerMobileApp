package com.cnbsoftware.reciperandomizermobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LanguagePickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.language_picker);
        ListView languagePicker = findViewById(R.id.lvLanguages);
        setGui(languagePicker);
        languagePicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLanguage = (String) adapterView.getItemAtPosition(i);
                startMainActivity(selectedLanguage);
            }
        });
    }

    private void startMainActivity(String selectedLanguage) {
        Bundle bundle = new Bundle();
        bundle.putString("UserLanguage", selectedLanguage);
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtras(bundle);
        startActivity(mainActivityIntent);
    }

    private void setGui(ListView languagePicker) {
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(this, R.layout.language_item, getSupportedLanguages());
        languagePicker.setAdapter(languagesAdapter);
    }

    private String[] getSupportedLanguages() {

        String[] supportedLanguages = {
            "en-GB",
            "nl",
            "tk"
        };

        return supportedLanguages;
    }
}
