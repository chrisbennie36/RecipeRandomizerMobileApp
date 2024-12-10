package com.cnbsoftware.reciperandomizermobileapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cnbsoftware.reciperandomizermobileapp.dtos.UserDto;
import com.cnbsoftware.reciperandomizermobileapp.helpers.UserServiceHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.MalformedURLException;

public class RegisterUserActivity extends AppCompatActivity {

    private UserServiceHelper userServiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerUserView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Button btnRegisterUser = (Button)findViewById(R.id.btnRegisterUser);
            TextInputEditText inputUsername = (TextInputEditText) findViewById(R.id.iputUsernameEditText);
            TextInputEditText inputEmail = (TextInputEditText) findViewById(R.id.inputEmailEditText);
            TextInputEditText inputPassword = (TextInputEditText) findViewById(R.id.inputPasswordEditText);
            Intent setPreferencesActivityIntent = new Intent(this.getApplicationContext(), SetPreferencesActivity.class);
            setPreferencesActivityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);

            try {
                userServiceHelper = new UserServiceHelper(this.getApplicationContext(), setPreferencesActivityIntent, null);
            } catch (MalformedURLException e) {
                Log.e("RegisterUserActivity", "Malformed Recipe Randomizer API URL");
            }

            btnRegisterUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDto userDto = new UserDto(inputUsername.getText().toString(), inputPassword.getText().toString());
                    try {
                        userServiceHelper.saveUser(userDto);
                    } catch (IOException e) {
                        Log.e("RegisterUserActivity", "JSON error occurred when serializing the Create User Request Body");
                    }
                }
            });

            return insets;
        });
    }
}