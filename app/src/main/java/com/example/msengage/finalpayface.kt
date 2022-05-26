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
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
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
//    lateinit var response: Response



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalpayface)
        mstorageref = FirebaseStorage.getInstance().reference
        myimage = findViewById(R.id.img)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
//        apirequest()



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
//            var file1 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fb.jpg?alt=media&token=44935a49-3028-4bc6-9216-c6712bc71513"
//            var file2 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fa.jpg?alt=media&token=86fa63a0-9853-41fe-b626-ef8fe8fbc7d9"


        }
            .addOnFailureListener {
                Toast.makeText(
                    this@finalpayface,
                    "" + "Failed to upload",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

//    private fun apirequest() {
//        val client = OkHttpClient()
//        println("Reaching Json")
////        var file1 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fb.jpg?alt=media&token=44935a49-3028-4bc6-9216-c6712bc71513"
////        var file2 ="https://firebasestorage.googleapis.com/v0/b/msengage-otp-9ec4c.appspot.com/o/PKTeFB43HUXufpCIXavK6NNJPYw1%2Fa.jpg?alt=media&token=86fa63a0-9853-41fe-b626-ef8fe8fbc7d9"
//        val mediaType = "application/x-www-form-urlencoded".toMediaType()
//        val body = "linkFile1=https://img1.hscicdn.com/image/upload/f_auto,t_ds_square_w_320,q_50/lsci/db/PICTURES/CMS/316600/316605.png&linkFile2=https://s1.stabroeknews.com/images/2020/12/kohli8.png".toRequestBody(mediaType)
//        val request = Request.Builder()
//            .url("https://face-verification2.p.rapidapi.com/faceverification")
//            .post(body)
//            .addHeader("content-type", "application/x-www-form-urlencoded")
//            .addHeader("X-RapidAPI-Host", "face-verification2.p.rapidapi.com")
//            .addHeader("X-RapidAPI-Key", "d8189c82ebmsh1c27016fe57f7edp12d344jsn9cf5d20c3e49")
//            .build()
//           client.newCall(request).enqueue(object : Callback{
//               override fun onResponse(call: Call, response: Response) {
//                   val body = response.body?.string()
//                   println(body)
//               }
//
//               override fun onFailure(call: Call, e: IOException) {
//                   println("Failed to fetch")
//               }
//           })
//
//    }
}