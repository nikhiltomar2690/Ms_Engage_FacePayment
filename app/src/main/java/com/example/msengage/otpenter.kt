package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class otpenter : AppCompatActivity() {

    //Initialising variable for Firebase Authentication
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpenter)

        auth=FirebaseAuth.getInstance()
        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        //Declaring Verification Variables
        val verify = findViewById<Button>(R.id.verifyBtn)
        val otpbox = findViewById<EditText>(R.id.id_otp)

        //Getting OTP entered and storing it
        verify.setOnClickListener{
            var otp=otpbox.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{

                //If left empty this toast pops up
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

    }
    //Verifying if user entered OTP is valid or not
    //If user verified then allow to move to Kyc Activity
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    startActivity(Intent(applicationContext, uploadcam::class.java))
                    finish()
// ...
                } else {
                    //Invalid OTP then show invalid otp message
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}