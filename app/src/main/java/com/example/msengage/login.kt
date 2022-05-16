package com.example.msengage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class login : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    val progressbar: ProgressBar? = null
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth=FirebaseAuth.getInstance()

        val btnotp = findViewById<Button>(R.id.btnotp)
        val progressbar = findViewById<ProgressBar>(R.id.progressbar)
        progressbar?.visibility = View.GONE



        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, home::class.java))
            finish()
        }
        btnotp.setOnClickListener{
            progressbar.visibility = View.VISIBLE
            loginpage()
        }
        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                progressbar.visibility = View.GONE
                startActivity(Intent(applicationContext, home::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressbar.visibility = View.GONE
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext,otpenter::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }
    }

    private fun loginpage() {
        val mobileNumber=findViewById<EditText>(R.id.phoneno)
        var number=mobileNumber.text.toString().trim()

        if(!number.isEmpty()){
            progressbar?.visibility = View.GONE
            number="+91"+number
            sendVerificationcode(number)
        }
        else{
            progressbar?.visibility = View.GONE
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}