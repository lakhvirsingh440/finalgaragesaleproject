package com.example.garagesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.garagesale.utils.ShareStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
   private EditText emailId, password, username,dateofbirth,confirmpassword;
   private Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailsign);
        password = findViewById(R.id.editText2);
        confirmpassword = findViewById(R.id.cpasswordsign);
        username = findViewById(R.id.username);
        dateofbirth = findViewById(R.id.dob);
        btnSignUp = findViewById(R.id.signup);
        tvSignIn = findViewById(R.id.signin);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String cpwd = confirmpassword.getText().toString();
                String usern =  username.getText().toString();
                String dob =  dateofbirth.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(usern.isEmpty()){
                    username.setError("Please enter your username");
                    username.requestFocus();
                }
                else if(dob.isEmpty()){
                    dateofbirth.setError("Please enter your Date of birth");
                    dateofbirth.requestFocus();
                }
                else if(!(cpwd.equals(pwd) && pwd.length()>=7)){
                    password.setError("Please check your password");
                    confirmpassword.setError("Please check your password");
                    confirmpassword.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(signup.this, "Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){

                    final ProgressDialog mDialog = new ProgressDialog(context);
                    mDialog.setMessage("Uploading...");
                    mDialog.setCancelable(false);
                    mDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()) {
                                mDialog.dismiss();
                                Toast.makeText(signup.this, "SignUp Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                final HashMap<String,Object> map = new HashMap<>();
                                map.put("username",username.getText().toString());
                                map.put("DOB",dateofbirth.getText().toString());
                                map.put("Email",emailId.getText().toString());
                                map.put("image", "");
                                map.put("userId", task.getResult().getUser().getUid());

                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(task.getResult().getUser().getUid())
                                        .updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();
                                                ShareStorage.setUserId(context, (String) map.get("userId"));
                                                ShareStorage.setUserName(context, username.getText().toString());
                                                ShareStorage.setEmail(context, emailId.getText().toString());
                                                startActivity(new Intent(signup.this,Login.class));
                                                finish();
                                            }
                                        });

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(signup.this, "Error Ocurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signup.this,Login.class);
                startActivity(i);
            }
        });

    }

}
