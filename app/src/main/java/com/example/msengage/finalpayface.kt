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
import android.view.View
import android.widget.ImageView
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream

class finalpayface : AppCompatActivity() {

    private var mstorageref: StorageReference? = null
    lateinit var auth : FirebaseAuth
    var myimage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalpayface)
        mstorageref = FirebaseStorage.getInstance().reference
        myimage = findViewById(R.id.img)

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
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
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
}