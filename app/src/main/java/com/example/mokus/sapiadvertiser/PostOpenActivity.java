package com.example.mokus.sapiadvertiser;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * Created by Mokus on 1/1/2018.
 */

public class PostOpenActivity extends AppCompatActivity {

    private static final String TAG = "Post Open";

    //----Variables
    private TextView postTitle;
    private TextView postShortDesc;
    private TextView postLongDesc;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_open);

        Post e = (Post) getIntent().getSerializableExtra("Events");
        postTitle = (TextView) findViewById(R.id.post_title);
        postShortDesc = (TextView) findViewById(R.id.post_short_description);
        postLongDesc = (TextView) findViewById(R.id.post_long_description);
        img= (ImageView) findViewById(R.id.image_preview);

        //Writing out the post
        postTitle.setText(e.getTitle());
        postShortDesc.setText(e.getShortDesc());
        postLongDesc.setText(e.getLongDesc());

        String imPath = e.getPostImage();
        Uri myUri = Uri.parse(imPath);

        //-----------Itt valamiert nem jelenik meg a kep.....

        /*StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imPath);

        Glide.with(getApplicationContext())
                    .load(imPath)
                    .into(img);*/

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),myUri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        img.setImageBitmap(bitmap);

    }

}