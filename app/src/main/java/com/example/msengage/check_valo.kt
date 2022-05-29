package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class check_valo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_valo)
        val buy = findViewById<Button>(R.id.buy1)

        // Activity for Valorant Battlepass


        buy.setOnClickListener{
            val intent = Intent(this@check_valo,finalpayface::class.java)
            startActivity(intent)
        }
    }
}