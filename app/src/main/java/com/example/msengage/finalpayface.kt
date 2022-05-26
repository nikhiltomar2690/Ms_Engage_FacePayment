package com.example.msengage

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.StorageReference
import android.os.Bundle
import com.example.msengage.R
import com.google.firebase.storage.FirebaseStorage
import android.content.Intent
import android.provider.MediaStore
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException

class finalpayface : AppCompatActivity() {

    private var mstorageref: StorageReference? = null
    lateinit var auth : FirebaseAuth
    var myimage: ImageView? = null
    val objectMapper = jacksonObjectMapper()
//    lateinit var response: Response




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalpayface)
        mstorageref = FirebaseStorage.getInstance().reference
        myimage = findViewById(R.id.img)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        apirequest()
    }



    fun uploadimage(v: View?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                onCaptureImageResult(data)
            }
        }
    }

    private fun onCaptureImageResult(data: Intent?) {
        val thumbnail = data!!.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bb = bytes.toByteArray()
        uploadToFirebase(bb)
    }

    private fun uploadToFirebase(bb: ByteArray) {

//        String phone = "1111";
        // myimages/1111_first.jpg
        val phone = FirebaseAuth.getInstance().currentUser!!.uid
        val sr = mstorageref!!.child(phone+"/b" + ".jpg")
        sr.putBytes(bb).addOnSuccessListener {
            Toast.makeText(this@finalpayface, "success", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@finalpayface,
                    "" + "Failed to upload",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    private fun apirequest() {


        val client = OkHttpClient()

        GlobalScope.launch(Dispatchers.IO) {
            val mediaType = "application/x-www-form-urlencoded".toMediaType()
            val body = "linkFile1=https://admin.thecricketer.com/weblab/Sites/96c8b790-b593-bfda-0ba4-ecd3a9fdefc2/resources/images/site/kohliheadshot-min.jpg&linkFile2=https://i.pinimg.com/originals/f0/5e/58/f05e58ea32f314a7bb39e2306ef90c14.png".toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://face-verification2.p.rapidapi.com/faceverification")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Host", "face-verification2.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "9d366f6cbdmsh32b7c6b43e6bcdep14c956jsn81c1a47b0532")
                .build()

            client.newCall(request).execute().use {
                val parsedResponse = parseResponse(it)
                println(parsedResponse.toString())
                println(parsedResponse["data"]["resultMessage"])
                val result = parsedResponse["data"]["similarPercent"].toString().toDouble()
                if(result > 0.80) println("YES")
                else println("NO")
            }
        }

    }
    fun parseResponse(response : Response): JsonNode {
        val body = response.body?.string() ?: ""
        val jsonBody = objectMapper.readValue<JsonNode>(body)
        return jsonBody
    }


}