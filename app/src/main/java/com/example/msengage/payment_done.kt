package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class payment_done : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_done)

        val redirect_to_home = findViewById<Button>(R.id.upload)

        //if user_kyc and user_final image confidence score is more than the required condition
        //then move to this activity
        //this is order successful activity

        redirect_to_home.setOnClickListener{
            val intent = Intent(this@payment_done,home::class.java)
            startActivity(intent)
        }
    }
}