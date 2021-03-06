package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.msengage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Binding is used to Bind all elemets without finding elements everytime
        //This activity is used to move to login activity
        binding.btnget.setOnClickListener {
            val intent = Intent(this@MainActivity, login::class.java)
            startActivity(intent)
        }



    }
}