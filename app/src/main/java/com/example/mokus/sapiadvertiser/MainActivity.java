package com.example.mokus.sapiadvertiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    //--------Database autentification
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //--------Input buttons
    private Button uploadBt;
    private Button loginBt;
    private Button registerBt;
    private Button logoutBt;

    //--------Recycler view init
    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewEventAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------Input buttons state
        loginBt = (Button) findViewById(R.id.btLogin1);
        registerBt = (Button) findViewById(R.id.btRegister1);
        uploadBt = (Button)findViewById(R.id.btUpload1);
        logoutBt = (Button) findViewById(R.id.btLogout1);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            loginBt.setVisibility(View.VISIBLE);
            registerBt.setVisibility(View.VISIBLE);
            uploadBt.setVisibility(View.GONE);
            logoutBt.setVisibility(View.GONE);
        }
        else{
            loginBt.setVisibility(View.GONE);
            registerBt.setVisibility(View.GONE);
            uploadBt.setVisibility(View.VISIBLE);
            logoutBt.setVisibility(View.VISIBLE);
        }

        //----------Recycler view and adapter
        recyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);
        mAdapter = new RecyclerViewEventAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        post();

        //--------Button click listeners
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UploadActivity.class);
                startActivity(i);
                finish();
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                dlgAlert.setMessage("Are you sure?");
                dlgAlert.setTitle("Logout");
                dlgAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginBt.setVisibility(View.VISIBLE);
                                registerBt.setVisibility(View.VISIBLE);
                                uploadBt.setVisibility(View.GONE);
                                logoutBt.setVisibility(View.GONE);
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                dlgAlert.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
    }

    private void post() {
        //-------Getting data snapshots from firebase
        DatabaseReference myRef = database.getReference().child("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                String postImage;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DataSnapshot dsi = ds.child("postImages");
                    postImage=dsi.getValue(String.class);
                    Post e = new Post(ds.child("title").getValue(String.class), ds.child("shortDesc").getValue(String.class), ds.child("longDesc").getValue(String.class), postImage);
                    postList.add(e);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
