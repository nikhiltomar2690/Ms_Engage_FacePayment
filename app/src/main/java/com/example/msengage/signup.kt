package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {

   private lateinit var signupname: EditText
   private lateinit var signupphone :EditText
   private lateinit var btnnext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        signupname = findViewById(R.id.signupname)
        signupphone = findViewById(R.id.signupphone)

        btnnext = findViewById(R.id.btnnext)

        btnnext.setOnClickListener{
            userdata()
            val intent = Intent(this@signup, uploadcam::class.java)
            startActivity(intent)
        }

    }

    private fun userdata() {
        val signupname = signupname.text.toString().trim()
        var phone = signupphone.text.toString()

        if(signupname.isEmpty()){
            Toast.makeText(
                this@signup,
                "Please Enter your name",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if(phone.isEmpty()){
            Toast.makeText(
                this@signup,
                "Please Enter phone number",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("users")
        var uid = ref.push().key.toString()

        val user = user(uid,signupname,phone)

        ref.child(uid).setValue(user).addOnCompleteListener{
            Toast.makeText(
                this@signup,
                "Data saved successfullly",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
}