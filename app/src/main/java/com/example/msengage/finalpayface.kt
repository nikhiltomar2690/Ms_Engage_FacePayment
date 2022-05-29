package com.example.msengage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import kotlin.Unit.toString


class finalpayface : AppCompatActivity() {

    // Declaring all variables that will be used in this activity
    private var storageref: StorageReference? = null
    lateinit var auth : FirebaseAuth
    val objectMapper = jacksonObjectMapper()
    private var mDatabase: DatabaseReference? = null

    // Getting user Currentid from FireBase
    val userid = FirebaseAuth.getInstance().currentUser!!.uid
    private var ref: StorageReference?=null

    // Creating two variables to store response from Firebase of Image Links
     var linkfile1 :String =""
     var linkfile2: String =""

    // Using SharedPreference for store and retrieve value of links out of function
    private lateinit var pref: SharedPreferences
    lateinit var uploadbtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalpayface)

        // Creating variable for SharedPreference
        pref = getSharedPreferences("links", Context.MODE_PRIVATE)

        // Creating Instance for Firebase Storage and Database
        storageref = FirebaseStorage.getInstance().getReference()
        mDatabase = FirebaseDatabase.getInstance().getReference()

        //Onclick action for Upload Image Button which sends user photo to the storage
        uploadbtn = findViewById(R.id.upload2)
        uploadbtn.setOnClickListener {

            //using progressbar until api response arrives
            val progressbarr = findViewById<ProgressBar>(R.id.progressbarr)
            progressbarr.visibility = View.VISIBLE
            val faceani = findViewById<ImageView>(R.id.faceimageani)
            faceani.alpha = 0.5F
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 101)
        }



    }
    //After doing specific activity the task returns on this function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                onCaptureImageResult(data)
            }
        }
    }

    //Capturing image and compressing image in this function
    private fun onCaptureImageResult(data: Intent?) {
        val thumbnail = data!!.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
       thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bb = bytes.toByteArray()

        //Calling uploadToFirebase function to uplaod the captured image to Database
        uploadToFirebase(bb)
    }

    // Uplaod captured Image to Firebase Storage
    private fun uploadToFirebase(bb: ByteArray) {

        //Defining path to store the image
        val sr = storageref!!.child(userid+"/b" + ".jpg")
        sr.putBytes(bb).addOnSuccessListener {
            Toast.makeText(this@finalpayface, "Recognising Faces", Toast.LENGTH_SHORT).show()

            //After capturing both live images , retreive their links using this function
         fetchimageurl()

        }
            .addOnFailureListener {
                Toast.makeText(
                    this@finalpayface,
                    "" + "Failed to upload",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    //This function uses the SharedPreferences to store the values and pass them to FaceVerification API as parameters
    private fun fetchimageurl(){
        val editor = pref.edit()
        storageref!!.child(userid+"/a" + ".jpg").downloadUrl.addOnSuccessListener {
            linkfile1 =it.toString()
            editor.putString("linkfile1", linkfile1)
            //this commit() function commits immediately the values
            editor.commit()
            val progressbarr = findViewById<ProgressBar>(R.id.progressbarr)
            progressbarr.visibility = View.GONE

        }.addOnFailureListener {
            // Handle any errors
        }
        storageref!!.child(userid+"/b" + ".jpg").downloadUrl.addOnSuccessListener {
            linkfile2 =it.toString()
            editor.putString("linkfile2", linkfile2)
            editor.commit()
        }.addOnFailureListener {
            // Handle any errors
        }

        //Retrieve values from SharedPref to these variables
        var link1: String? = pref.getString("linkfile1", "linkfile1this")
        var link2: String? = pref.getString("linkfile2", "linkfile1this")

        //Checking if the values are not null
        if (link1 != null && link2 != null) {

            //Now pass the parameters to the API
            apirequest(link1,link2)
        }

//
    }

    //Function for the API call
    private fun apirequest(link1 : String , link2 :String) {

        // initialising OKhttpClient()
        val client = OkHttpClient()

        //Encode the url of the images to the valid URL format
        // URL Encoder also secures the URL
        var link1URI = URLEncoder.encode(link1, "UTF-8");
        var link2URI = URLEncoder.encode(link2, "UTF-8");


        // Using Coroutines for running the code on different threads
        // Achieves concurrency and saves overhead
        GlobalScope.launch(Dispatchers.IO) {
            val mediaType = "application/x-www-form-urlencoded".toMediaType()

            // Putting encoded Url variables in the API body
            val body = RequestBody.create(mediaType, "linkFile1=${link1URI}&linkFile2=${link2URI}")

            //sending request to the API
            val request = Request.Builder()
                .url("https://face-verification2.p.rapidapi.com/faceverification")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Host", "face-verification2.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "5b5487f715mshfd5e195575f9544p1e2572jsn7cde575f3acd")
                .build()

            client.newCall(request).execute().use {

                //getting response from the API
                val parsedResponse = parseResponse(it)

                //Printing in Logcat for the verifying the result
                println(parsedResponse.toString())
                println(parsedResponse["data"]["resultMessage"])

                //storing similarPercent and using it for the transaction condition
                val result = parsedResponse["data"]["similarPercent"].toString().toDouble()
                if(result > 0.80) {
                    val intent =Intent(this@finalpayface,payment_done::class.java)
                    startActivity(intent)
                }
                else if(result<0.80){
                    Toast.makeText(applicationContext,"Faces do not match",Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    //this function is for Parsing the API response
    fun parseResponse(response : Response): JsonNode {
        val body = response.body?.string() ?: ""
        val jsonBody = objectMapper.readValue<JsonNode>(body)
        return jsonBody
    }



}