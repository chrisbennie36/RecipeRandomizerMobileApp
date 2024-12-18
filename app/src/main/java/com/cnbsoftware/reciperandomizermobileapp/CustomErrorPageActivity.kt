package com.cnbsoftware.reciperandomizermobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CustomErrorPageActivity : AppCompatActivity() {
    private var problemDetails: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.custom_error_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customErrorPageView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val btnOk = findViewById(R.id.btnOk) as Button
            val tvTraceId = findViewById(R.id.tvErrorTraceId) as TextView
            val tvUserErrorNotification = findViewById(R.id.tvUserErrorNotification) as TextView
            val mainActivity = Intent(this, MainActivity::class.java)
            val bundle = getIntent().extras

            if(bundle != null) {
                problemDetails = bundle.getString("ProblemDetails")
                tvUserErrorNotification.setText(String.format("An error has occurred, search for the below Trace ID in Cloudwatch for more info"));
                tvTraceId.setText(getTraceId(problemDetails));
            }

            btnOk.setOnClickListener {
                startActivity(mainActivity)
            }

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun getTraceId(problemDetails: String?): String {
        if(problemDetails == null) {
            return ""
        } else if(problemDetails.contains("traceId")) {
            val traceId = problemDetails.substringAfter("traceId")
            //Problem details returns a trace ID with some extra formatting, so get rid of this
            return traceId.trim(':').trim('}').trim('"').trim(':').removePrefix("\"00-").removeSuffix("-00")
        } else {
            return problemDetails;
        }
    }
}