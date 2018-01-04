package com.example.mokus.sapiadvertiser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class PhotoActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;

    //-------Button and image
    private Button btChoose;
    private Button btUpload;
    private ImageView imgView;

    //-------URI for the image path
    private Uri imagePath;

    //-------Firebase database init
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //----------Connect to firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //----------Buttons
        btChoose = (Button) findViewById(R.id.btChoose);
        btUpload = (Button) findViewById(R.id.btUpload);
        imgView = (ImageView) findViewById(R.id.imgView);

        //----------Click Listeners for the buttons
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });
    }

    private void uploadImage() {

        if(imagePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.");
            progressDialog.show();

            //---------Makeing the random ID for the image
            final String pathToImage = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(pathToImage);
            ref.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(PhotoActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    double uploadedData = (100.00*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.QUANTITY,String.valueOf(uploadedData));
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    Intent i = getIntent();
                    i.putExtra("pathImg",pathToImage);
                    setResult(Activity.RESULT_OK, i);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PhotoActivity.this,"Upload failed! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.00*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imagePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                imgView.setImageBitmap(bitmap);
            }
            catch (IOException e){

                e.printStackTrace();
            }
        }

    }
}
