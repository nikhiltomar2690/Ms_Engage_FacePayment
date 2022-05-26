package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class home : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

        val logout =findViewById<Button>(R.id.idLogout)
        val buygta = findViewById<Button>(R.id.buygta)
        val buyvalo = findViewById<Button>(R.id.buyvalo)

        val hyperx = findViewById<ImageView>(R.id.hyperx)
        val fortnite = findViewById<ImageView>(R.id.fortnite)
        val cod = findViewById<ImageView>(R.id.cod)
        val fifa = findViewById<ImageView>(R.id.fifa)

        if(currentUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        buygta.setOnClickListener{
            val intent = Intent(this@home, check_gta::class.java)
            startActivity(intent)
        }
        buyvalo.setOnClickListener{
            val intent = Intent(this@home, check_valo::class.java)
            startActivity(intent)
        }
        hyperx.setOnClickListener{
            val intent = Intent(this@home, check_hyperx::class.java)
            startActivity(intent)
        }
        fortnite.setOnClickListener{
            val intent = Intent(this@home, check_fort::class.java)
            startActivity(intent)
        }
        cod.setOnClickListener{
            val intent = Intent(this@home, check_cod::class.java)
            startActivity(intent)
        }
        fifa.setOnClickListener{
            val intent = Intent(this@home, check_fifa::class.java)
            startActivity(intent)
        }


        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }


}