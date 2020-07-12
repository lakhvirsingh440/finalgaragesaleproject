package com.example.garagesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.garagesale.utils.ShareStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class Login extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    TextView tv_forgot;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passwordlogin);
        btnSignIn = findViewById(R.id.buttonlogin);
        tvSignUp = findViewById(R.id.signup);
        tv_forgot = findViewById(R.id.tv_forgot);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this, "Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    final ProgressDialog mDialog = new ProgressDialog(context);
                    mDialog.setMessage("Uploading...");
                    mDialog.setCancelable(false);
                    mDialog.show();

                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                            else  {
                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference mDbRef = mDatabase.getReference().child("User").child(task.getResult().getUser().getUid());

                                mDbRef.addValueEventListener(new ValueEventListener(){
                                    String userEmail,username,dob ;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)  {
                                        mDialog.dismiss();
                                        HashMap<String, String> keyId = (HashMap<String, String>) dataSnapshot.getValue();
                                        userEmail = keyId.get("Email");
                                        username = keyId.get("username");
                                        dob = keyId.get("DOB");
                                        ShareStorage.setUserId(context, task.getResult().getUser().getUid());
                                        ShareStorage.setUserName(context, username);
                                        ShareStorage.setEmail(context, email);
                                        Intent inToHome = new Intent(Login.this,Main2Activity.class);
                                        startActivity(inToHome);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Login.this, "Error Ocurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intoSignUp = new Intent(Login.this, signup.class);
                startActivity(intoSignUp);

            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, otp.class);
                startActivity(i);
            }
        });

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    /*Toast.makeText(Login.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Main2Activity.class);
                    startActivity(i);*/
                }
                else {
                    Toast.makeText(Login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

}
