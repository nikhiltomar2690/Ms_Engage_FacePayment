package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class home : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

        val logout =findViewById<Button>(R.id.idLogout)
        val buy1 = findViewById<Button>(R.id.buy1)

        if(currentUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        buy1.setOnClickListener{
            val intent = Intent(this@home, uploadcam::class.java)
            startActivity(intent)
        }


        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }
}