package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class check_fort : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_fort)

        val buy = findViewById<Button>(R.id.buy1)

        buy.setOnClickListener{
            val intent = Intent(this@check_fort,finalpayface::class.java)
            startActivity(intent)
        }
    }
}