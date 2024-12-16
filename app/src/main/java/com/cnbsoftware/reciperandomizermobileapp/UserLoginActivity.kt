package com.cnbsoftware.reciperandomizermobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserDto
import com.cnbsoftware.reciperandomizermobileapp.helpers.UserServiceHelper
import com.google.android.material.textfield.TextInputEditText

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.user_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userLoginView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val btnUserLogin = findViewById(R.id.btnLogin) as Button
            val btnRegisterUser = findViewById(R.id.btnLoginRegisterUser) as Button
            val usernameInput = findViewById(R.id.inputLoginUsername) as TextInputEditText
            val passwordInput = findViewById(R.id.inputLoginPassword) as TextInputEditText

            val mainActivityIntent = Intent(this, MainActivity::class.java)
            val registerUserIntent = Intent(this, RegisterUserActivity::class.java)
            val setPreferencesIntent = Intent(this, SetPreferencesActivity::class.java)

            val userServiceHelper = UserServiceHelper(this.applicationContext, setPreferencesIntent, mainActivityIntent)

            var selectedLanguage = "en-GB"
            val bundle = getIntent().extras
            if(bundle != null && bundle.containsKey("UserLanguage")) {
                selectedLanguage = bundle.getString("UserLanguage").toString()
                registerUserIntent.putExtras(bundle)
            }

            btnRegisterUser.setOnClickListener {
                startActivity(registerUserIntent)
            }

            btnUserLogin.setOnClickListener {
                val loginSuccessful = userServiceHelper.Login(UserDto(usernameInput.text.toString(), passwordInput.text.toString(), selectedLanguage))

                if (!loginSuccessful) {
                    usernameInput.hint = "Incorrect username or password"
                    passwordInput.hint = "Incorrect username or password"
                }
            }

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}