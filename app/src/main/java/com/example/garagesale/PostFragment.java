package com.example.garagesale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.garagesale.utils.ShareStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {
    View view;

    private FirebaseDatabase database;
    private DatabaseReference userReference;

    //Firebase storage
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String name, address, phone, description,price, image;
    ImageView imgProfile;
    EditText edtname, edtdescription, edtphone,edtaddress,etPrice;
    Spinner etcate;
    Button btnUpdate;
    FirebaseUser user;

    public static final int PICK_IMAGE_REQUEST=9999;
    public PostFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_post, container, false);

        //etcate = view.findViewById(R.id.spinner);
        etcate = (Spinner) view.findViewById(R.id.Spinnner);

        //Init Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = firebaseStorage.getReference();
         //categories = etcate.getSelectedItem().toString();

        if (user == null) {
            startActivity(new Intent(getContext(), Login.class));
            getActivity().finish();
        }
        
        //String uid = user.getUid();
        database = FirebaseDatabase.getInstance();

        


        imgProfile = view.findViewById(R.id.imgProfile);
        edtname = view.findViewById(R.id.edtname);
        edtaddress = view.findViewById(R.id.edtaddress);
        etPrice = view.findViewById(R.id.price);
        edtphone = view.findViewById(R.id.edtphone);
        edtdescription = view.findViewById(R.id.edtdescription);
        btnUpdate = view.findViewById(R.id.btnUpdate);


        edtname.setText(name);
        edtaddress.setText(address);
        edtphone.setText(phone);
        edtdescription.setText(description);
        etPrice.setText(price);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseImage();

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edtname.getText().toString();
                phone = edtphone.getText().toString();
                address = edtaddress.getText().toString();
                description = edtdescription.getText().toString();
                price = etPrice.getText().toString();


                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(address) && TextUtils.isEmpty(description) && TextUtils.isEmpty(price) && etcate.getSelectedItemPosition() == 0 && saveUri == null) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (saveUri == null) {
                    Toast.makeText(getContext(), "Please choose post image", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "Please enter your phone", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(description)) {
                    Toast.makeText(getContext(), "Please enter your description", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(price)) {
                    Toast.makeText(getContext(), "Please enter your price", Toast.LENGTH_SHORT).show();
                } else if (etcate.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please choose the category", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                    //updateUserInfo();
                }

            }
        });

        return view;
    }

    private void updateUserInfo() {




        Map<String, Object> updateInfo = new HashMap<>();

        //Check Validation
        if (!TextUtils.isEmpty(name))
            updateInfo.put("name", name);

        if (!TextUtils.isEmpty(phone))
            updateInfo.put("phone", phone);

        if (!TextUtils.isEmpty(address))
            updateInfo.put("address", address);

        if (!TextUtils.isEmpty(description))
            updateInfo.put("description", description);
        if (!TextUtils.isEmpty(price))
            updateInfo.put("Price", price);

        if (etcate.getSelectedItemPosition() != 0) {
            updateInfo.put("categoryId", ""+(etcate.getSelectedItemPosition() - 1));
            updateInfo.put("categoryName", ""+getResources().getStringArray(R.array.Spinnner)[etcate.getSelectedItemPosition()]);
        }


        Log.e("userId", user.getUid()+ "//"+user.getDisplayName());
        updateInfo.put("userId", user.getUid());
        updateInfo.put("username", ShareStorage.getUsername(getContext()));
        updateInfo.put("image",imagePath);

        updateInfo(updateInfo);
    }

    private void updateInfo(Map<String, Object> updateInfo) {

        String postId = updateInfo.get("categoryId") + "_" + Calendar.getInstance().getTimeInMillis();
        userReference = database.getReference().child("Post").child(postId);
        userReference.keepSynced(true);
        userReference.updateChildren(updateInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Information uploaded !", Toast.LENGTH_SHORT).show();

                    Intent post = new Intent(getActivity(), Main2Activity.class);
                    post.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(post);


                }
                else
                    Toast.makeText(getActivity(), "Information uploaded failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_REQUEST);
    }

    Uri saveUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            imgProfile.setImageURI(null);
            imgProfile.setImageURI(saveUri);
        }
    }

    String imagePath;
    ProgressDialog mDialog;
    private void uploadImage() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Uploading...");
        mDialog.setCancelable(false);
        mDialog.show();
        final String image = "PostImage_"+Calendar.getInstance().getTimeInMillis();    //Random name image upload
        final StorageReference imageFolder =storageReference.child("images/"+image);
        imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Upload this url to avtar property of user
                        //First you need to add avtar property on user model
                        Map<String,Object> avatarUpdate = new HashMap<>();

                        imagePath = uri.toString();
                        Log.e("path", imagePath);
                        updateUserInfo();

                    }
                });
            }
        });
    }
}
