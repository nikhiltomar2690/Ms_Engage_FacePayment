package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class check_fifa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_fifa)

        // Activity for Fifa Product

        val buy = findViewById<Button>(R.id.buy1)

        buy.setOnClickListener{
            val intent = Intent(this@check_fifa,finalpayface::class.java)
            startActivity(intent)
        }

    }
}