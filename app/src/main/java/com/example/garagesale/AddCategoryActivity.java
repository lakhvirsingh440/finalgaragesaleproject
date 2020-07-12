package com.example.garagesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference userReference;

    //Firebase storage
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    String[] items = {"Furniture", "Vehicle Parts", "Clothes", "Electronics", "Shoes", "Kitchen", "Books"};
    int[] images = {R.drawable.bench1, R.drawable.vehical1, R.drawable.clothe1, R.drawable.electronic1, R.drawable.shoe1, R.drawable.kitchen1, R.drawable.book1};

    Context context = this;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        //Init Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = firebaseStorage.getReference();

        database = FirebaseDatabase.getInstance();

        updateInfo();
    }

    ProgressDialog mDialog;
    private void updateInfo() {
        mDialog = new ProgressDialog(context);// for showing any process on screen
        mDialog.setMessage("Uploading...");
        mDialog.show();
        uploadData(pos);
    }

    private void uploadData(int pos) {
        byte[] bytes = getDrawableToBytes(images[pos]);
        uploadImage(items[pos], bytes);
    }

    private void uploadImage(final String name, byte[] res) {
        final StorageReference imageFolder =storageReference.child("images/"+name);
        imageFolder.putBytes(res).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Upload this url to avtar property of user
                        //First you need to add avtar property on user model
                        Map<String, Object> updateInfo = new HashMap<>();
                        updateInfo.put("name", name);
                        updateInfo.put("image", uri.toString());
                        userReference = database.getReference().child("Category").child(""+pos);
                        userReference.updateChildren(updateInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Log.e("info", pos + " upload");
                                    //Toast.makeText(context, "Information uploaded !", Toast.LENGTH_SHORT).show();
                                    if (pos == 6) {
                                        mDialog.dismiss();
                                    } else {
                                        pos++;
                                        uploadData(pos);
                                    }

                                    /*Intent post = new Intent(context, Main2Activity.class);
                                    startActivity(post);*/


                                }
                                else
                                    Toast.makeText(context, "Information uploaded failed !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        /*double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mDialog.setMessage("Uploading " + pos + " : "+progress+"%");*/
                    }
                });
    }

    private byte[] getDrawableToBytes(int resource) {
        Drawable d = getResources().getDrawable(resource);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }
}
