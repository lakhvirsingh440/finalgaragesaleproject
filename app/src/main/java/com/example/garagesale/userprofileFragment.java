package com.example.garagesale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.garagesale.utils.ShareStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.garagesale.PostFragment.PICK_IMAGE_REQUEST;

public class userprofileFragment extends Fragment {
    Button btnlog,delete;
    ImageView imgProfile;
    private DatabaseReference User;
    private FirebaseDatabase database;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private static final String USERS = "User";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_userprofile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        imgProfile = rootView.findViewById(R.id.imgProfile1);
        delete = rootView.findViewById(R.id.delete);
        if (user != null) {
            // Name, email address, and profile photo Url

            final String email = user.getEmail();

            Uri photoUrl = user.getPhotoUrl();


            TextView tvemail = (TextView)rootView.findViewById(R.id.textView2);


            tvemail.setText(email);


            // The user's ID, unique to the Firebase project.
            final String uid = user.getUid();
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseImage();

                }
            });

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDbRef = mDatabase.getReference().child(USERS).child(uid);
            Log.v("USERID", mDbRef.getKey());

            mDbRef.addValueEventListener(new ValueEventListener(){
                String userEmail,username,dob ;
                @Override
                public void onDataChange(@NonNull  DataSnapshot dataSnapshot)  {
                    HashMap<String, String> keyId = (HashMap<String, String>) dataSnapshot.getValue();
                    userEmail = keyId.get("Email");
                    username = keyId.get("username");
                    dob = keyId.get("DOB");
                    String message = dataSnapshot.child("avatarUrl").getValue(String.class);
                    Picasso.get().load(message).into(imgProfile);
                    ShareStorage.setUserId(getContext(), uid);
                    ShareStorage.setUserName(getContext(), username);
                    ShareStorage.setEmail(getContext(), email);

                        final TextView tvdob = (TextView) rootView.findViewById(R.id.textView3);
                        final TextView tvName = (TextView) rootView.findViewById(R.id.textView);
                        tvName.setText(username);
                        tvdob.setText(dob);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
        final String uid = user.getUid();
        User = database.getReference().child(USERS).child(uid);;
        User.keepSynced(true);






        btnlog = (Button) rootView.findViewById(R.id.logout);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareStorage.clean(getContext());
                FirebaseAuth.getInstance().signOut();
                Intent myintent = new Intent(getActivity(), Main2Activity.class);
                startActivity(myintent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential("user@example.com", "password1234");

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "User Account deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });
                Intent myintent = new Intent(getActivity(), Main2Activity.class);
                startActivity(myintent);
            }
        });
        return rootView;

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri saveUri = data.getData();
            imgProfile.setImageURI(null);
            imgProfile.setImageURI(saveUri);
            if (saveUri!=null)
            {
                final ProgressDialog mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Uploading...");
                mDialog.show();

                String image = UUID.randomUUID().toString();    //Random name image upload
                final StorageReference imageFolder =storageReference.child("images/"+image);
                imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        mDialog.dismiss();

                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Upload this url to avtar property of user
                                //First you need to add avtar property on user model

                                Map<String,Object> avatarUpdate = new HashMap<>();
                                avatarUpdate.put("avatarUrl",uri.toString());



                                User.updateChildren(avatarUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful())
                                                    Toast.makeText(getActivity(), "Uploaded !", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(getActivity(), "Uploaded Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mDialog.setMessage("Uploading : "+progress+"%");
                            }
                        });
            }
        }
    }


}
