package com.example.garagesale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class furniture extends AppCompatActivity {
    View view;
    private furniture context;
    private TextView url;
    private ImageView img;
    TextView mName, mAddress, mPhone, mDescription, mPrice;
    private String edtname,edtaddress,edtphone,edtdescription,etPrice;
    ImageView postimage;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private static final String USERS = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);
        context = this;
        img = findViewById(R.id.image_view_upload);
        mName = findViewById(R.id.edtname);

        mAddress = findViewById(R.id.edtaddress);

        mPhone = findViewById(R.id.edtphone);

        mDescription = findViewById(R.id.edtdescription);

        mPrice = findViewById(R.id.etPrice);

        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Category")/*.child("vehicle")*/;

        getUserInfo();

    }

    private void getUserInfo() {

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message = dataSnapshot.child("avatarUrl").getValue(String.class);
                Picasso.get().load(message).into(img);

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        edtname = map.get("name").toString();
                        mName.setText(edtname);
                    }

                    if (map.get("address") != null) {
                        edtaddress = map.get("address").toString();
                        mAddress.setText(edtaddress);
                    }

                    if (map.get("phone") != null) {
                        edtphone = map.get("phone").toString();
                        mPhone.setText(edtphone);
                    }

                    if (map.get("description") != null) {
                        edtdescription = map.get("description").toString();
                        mDescription.setText(edtdescription);
                    }

                    if (map.get("Price") != null) {
                        etPrice = map.get("Price").toString();
                        mPrice.setText(etPrice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}