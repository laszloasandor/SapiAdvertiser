package com.example.mokus.sapiadvertiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private static final int IMAGE_PATH = 72;

    //------Input buttons and texts
    private Button uploadBt;
    private Button photoBt;
    private EditText title;
    private EditText shortDesc;
    private EditText longDesc;
    private String imagesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //---------Input fields
        uploadBt = (Button)findViewById(R.id.uploadBt);
        photoBt = (Button)findViewById(R.id.photoBt);
        title = (EditText)findViewById(R.id.title);
        shortDesc = (EditText)findViewById(R.id.shortDescription);
        longDesc = (EditText)findViewById(R.id.description);

        //--------Click listeners for the buttons
        photoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoBt.setClickable(false);
                Intent i = new Intent(UploadActivity.this,PhotoActivity.class);
                startActivityForResult(i,72);
            }
        });

        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-----Uploading Data to Firebase
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                DatabaseReference postsCloudEndPoint = mDatabase.child("posts");
                Post post = new Post(title.getText().toString(),shortDesc.getText().toString(),longDesc.getText().toString(),imagesPath);
                postsCloudEndPoint.child(UUID.randomUUID().toString()).setValue(post);
                imagesPath = null;

                Intent i = new Intent(UploadActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_PATH) {

            if (resultCode == RESULT_OK) {

                Intent in= data;
                Bundle b = in.getExtras();
                if(b!=null)
                {
                    imagesPath =(String) b.get("pathImg");
                }
            }
        }
    }
}
