package com.cnbsoftware.reciperandomizermobileapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cnbsoftware.reciperandomizermobileapp.dtos.UserDto;
import com.cnbsoftware.reciperandomizermobileapp.helpers.UserServiceApiHelper;
import com.cnbsoftware.reciperandomizermobileapp.managers.ActivityManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.MalformedURLException;

public class RegisterUserActivity extends AppCompatActivity {

    private UserServiceApiHelper userServiceApiHelper;
    private ActivityManager activityManager;
    private String userLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_user);
        activityManager = new ActivityManager(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerUserView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Button btnRegisterUser = (Button)findViewById(R.id.btnRegisterUser);
            TextInputEditText inputUsername = (TextInputEditText) findViewById(R.id.iputUsernameEditText);
            TextInputEditText inputEmail = (TextInputEditText) findViewById(R.id.inputEmailEditText);
            TextInputEditText inputPassword = (TextInputEditText) findViewById(R.id.inputPasswordEditText);

            try {
                userServiceApiHelper = new UserServiceApiHelper(activityManager);
            } catch (MalformedURLException e) {
                Log.e("RegisterUserActivity", "Malformed Recipe Randomizer API URL");
            }

            Bundle bundle = getIntent().getExtras();

            if(bundle != null && bundle.containsKey("UserLanguage")) {
                userLanguage = bundle.getString("UserLanguage");
            }

            btnRegisterUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDto userDto = new UserDto(inputUsername.getText().toString(), inputPassword.getText().toString(), userLanguage);
                    try {
                        userServiceApiHelper.saveUser(userDto);
                    } catch (IOException e) {
                        Log.e("RegisterUserActivity", "JSON error occurred when serializing the Create User Request Body");
                    }
                }
            });

            return insets;
        });
    }
}