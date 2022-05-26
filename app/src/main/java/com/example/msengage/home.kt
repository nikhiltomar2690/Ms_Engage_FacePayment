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

        apirequest()

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

    private fun apirequest() {
        val client = OkHttpClient()
        println("Reaching Json")
//        var file1 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fb.jpg?alt=media&token=44935a49-3028-4bc6-9216-c6712bc71513"
//        var file2 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fa.jpg?alt=media&token=86fa63a0-9853-41fe-b626-ef8fe8fbc7d9"
        val mediaType = "application/x-www-form-urlencoded".toMediaType()
        val body = "linkFile1=https://img1.hscicdn.com/image/upload/f_auto,t_ds_square_w_320,q_50/lsci/db/PICTURES/CMS/316600/316605.png&linkFile2=https://s1.stabroeknews.com/images/2020/12/kohli8.png".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://face-verification2.p.rapidapi.com/faceverification")
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("X-RapidAPI-Host", "face-verification2.p.rapidapi.com")
            .addHeader("X-RapidAPI-Key", "d8189c82ebmsh1c27016fe57f7edp12d344jsn9cf5d20c3e49")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to fetch")
            }
        })

    }
}