package com.cnbsoftware.reciperandomizermobileapp

import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cnbsoftware.reciperandomizermobileapp.helpers.RecipeRandomizerApiHelper
import com.cnbsoftware.reciperandomizermobileapp.managers.ActivityManager

class MainActivity : AppCompatActivity() {
    private var userLoggedIn: Boolean = false
    private var userLanguage: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val btnInspireMe = findViewById(R.id.btn_inspire_me) as Button
            val btnRegisterUser = findViewById(R.id.btnRegisterUser) as Button
            val btnSelectLanguage = findViewById(R.id.btnSelectLanguage) as Button
            val btnLogin = findViewById(R.id.btnMainActivityLogin) as Button
            val btnChangePreferences = findViewById(R.id.btnChangePreferences) as Button
            val activityManager = ActivityManager(this)
            val recipeRandomizerHelper =
                RecipeRandomizerApiHelper(
                    activityManager
                )
            val bundle = getIntent().extras

            if(bundle != null) {
                userLoggedIn = bundle.getBoolean("UserLoggedIn")
                if(bundle.getString("UserLanguage") != null) {
                    userLanguage = bundle.getString("UserLanguage").toString()
                    btnSelectLanguage.text = String.format("Change Language (%s)", userLanguage.uppercase())
                }

                if(userLoggedIn) {
                    btnLogin.text = "Log Out"
                    btnRegisterUser.visibility = INVISIBLE
                    btnInspireMe.visibility = VISIBLE
                    btnChangePreferences.visibility = VISIBLE
                }
            }

            btnSelectLanguage.setOnClickListener {
                activityManager.OpenActivity(LanguagePickerActivity::class.java)
            }

            btnRegisterUser.setOnClickListener {
                activityManager.OpenActivity(RegisterUserActivity::class.java, bundle)
            }

            btnChangePreferences.setOnClickListener {
                if(bundle == null) {
                    Log.e("MainActivity", "Attempting to change user preferences, but bundle is null");
                }
                else {
                    val userId = bundle.getInt("UserId")
                    recipeRandomizerHelper.GetUserRecipePreferences(userId, this);
                }
            }

            btnLogin.setOnClickListener {
                if(!userLoggedIn) {
                    activityManager.OpenActivity(UserLoginActivity::class.java, bundle)
                } else {
                    btnLogin.text = "Log In"
                    btnRegisterUser.visibility = VISIBLE
                    btnInspireMe.visibility = INVISIBLE
                    btnChangePreferences.visibility = INVISIBLE
                }
            }

            btnInspireMe.setOnClickListener {
                if(bundle != null) {
                    val userId = bundle.getInt("UserId")
                    recipeRandomizerHelper.InspireUser(userId)
                }
            }

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}