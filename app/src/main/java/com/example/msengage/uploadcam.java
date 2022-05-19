package com.example.msengage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class uploadcam extends AppCompatActivity {

    private StorageReference mstorageref;
    ImageView myimage;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_uploadcam);
        mstorageref = FirebaseStorage.getInstance().getReference();

        myimage = findViewById(R.id.img);


    }

    public void uploadimage(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode, @Nullable Intent data){
    super.onActivityResult(requestCode,resultCode,data);

    if(resultCode== Activity.RESULT_OK){
        if(requestCode==101){
            onCaptureImageResult(data);
        }
      }
    }
    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        byte bb[] = bytes.toByteArray();
        myimage.setImageBitmap(thumbnail);

        uploadToFirebase(bb);
    }

    private void uploadToFirebase(byte[] bb){
        StorageReference sr  = mstorageref.child("myimages/a.jpg");
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(uploadcam.this,"success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadcam.this, ""+"Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

}