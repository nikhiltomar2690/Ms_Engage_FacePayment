package com.example.msengage

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.StorageReference
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream

class uploadcam : AppCompatActivity() {

    //In this activity we uplaod the kyc image to Firebase Storage
    private var mstorageref: StorageReference? = null
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstances: Bundle?) {
        super.onCreate(savedInstances)
        setContentView(R.layout.activity_uploadcam)

        //Creating mstorageref for Firebase Storage
        mstorageref = FirebaseStorage.getInstance().reference


    }

    //Onclick is applied in the XML code
    //this function is called to access the Camera
    fun uploadimage(v: View?) {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    //this func always run after assigned tasks are over
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                onCaptureImageResult(data)
            }
        }
    }

    //This function defines what to do when capture image
    //Captures and then compresses the image
    // after this call the uploadtoFirebase()
    private fun onCaptureImageResult(data: Intent?) {
        val thumbnail = data!!.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val bb = bytes.toByteArray()
        uploadToFirebase(bb)
    }

    //the paramter is received from onCaptureImageResult() and used to save in the Storage
    private fun uploadToFirebase(bb: ByteArray) {
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        val sr = mstorageref!!.child(userid+"/a" + ".jpg")
        sr.putBytes(bb).addOnSuccessListener {
            //if saving successful then
            //display a toast and move to next activity
            Toast.makeText(this@uploadcam, "success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, home::class.java))
            finish()

        }
                //if failed display failed to load
            .addOnFailureListener {
                Toast.makeText(
                    this@uploadcam,
                    "" + "Failed to upload",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}