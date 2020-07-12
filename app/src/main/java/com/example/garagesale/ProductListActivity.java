package com.example.garagesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garagesale.adapter.PostAdapter;
import com.example.garagesale.interfaces.OnAdapterClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProductListActivity extends AppCompatActivity implements OnAdapterClickListener {

    private DatabaseReference userReference;
    ArrayList<HashMap<String, String>> list;
    ArrayList<HashMap<String, String>> list2;
    String categoryId, categoryName;
    String id, name;
    TextView title;
    RecyclerView rcyPost;
    PostAdapter postAdapter;
    ProgressDialog mDialog;
    FirebaseAuth mFirebaseAuth;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mFirebaseAuth = FirebaseAuth.getInstance();

        categoryId = ""+getIntent().getIntExtra("id", 0);
        categoryName = getIntent().getStringExtra("name");

        title = findViewById(R.id.title);
        rcyPost = findViewById(R.id.rcyPost);
        title.setText(categoryName);

        postAdapter = new PostAdapter(this, this);
        rcyPost.setAdapter(postAdapter);


        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Uploading...");
        mDialog.setCancelable(false);
        mDialog.show();

        userReference = FirebaseDatabase.getInstance().getReference().child("Post");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                if (dataSnapshot.exists()) {
                    //list = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                    //Log.e("Size", list.size()+"//");
                    filterList(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }

    private void filterList(DataSnapshot dataSnapshot) {

        list = new ArrayList<>();
        Iterator<DataSnapshot> data = dataSnapshot.getChildren().iterator();
        while (data.hasNext()) {
            HashMap<String, String> map = (HashMap<String, String>) data.next().getValue();
            if (map.get("categoryId").equals(categoryId)) {
                list.add(map);
            }
            //dataSnapshot.getChildren().iterator().remove();
        }
        /*for (int i=0; i<list.size(); i++) {
            if (list.get(i).get("categoryId").equals(id)) {
                list2.add(list.get(i));
            }
        }*/
        Log.e("Size", list.size()+"//");
        postAdapter.updateAdapter(list);
    }

    @Override
    public void onClick(int position) {
        if (mFirebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please Login first", Toast.LENGTH_SHORT).show();
        } else {
            id = list.get(position).get("userId");
            name = list.get(position).get("username");
            fetch1();
        }

    }

    void fetch1() {
        showCircularDialog();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("RecentMessages");
        myRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                hideCircularDialog();
                HashMap chats = (HashMap) dataSnapshot.getValue();
                if (chats != null) {
                    for (final Object entry : chats.keySet()) {
                        HashMap chat = (HashMap) chats.get(entry);
                        if(chat.keySet().toString().contains(getUserID()) && chat.keySet().toString().contains(categoryId))
                        {
                            Intent intent=new Intent(context,ChatActivity.class);
                            intent.putExtra("key" ,entry.toString());
                            intent.putExtra("id" , id);
                            intent.putExtra("name" , name);
                            startActivity(intent);
                            return;
                        }
                    }
                }


                HashMap chat = new HashMap();
                //String mGroupId = myRef.push().getKey();
                String mGroupId = "room_id"+System.currentTimeMillis();
                chat.put("Seen",true);
                myRef.child(mGroupId).child("lastText").setValue(chat);

                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("key" ,mGroupId);
                intent.putExtra("id" ,id);
                intent.putExtra("name" , name);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideCircularDialog();
            }
        });
    }

    private String getUserID() {
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        return mFirebaseUser.getUid();
    }

    private void hideCircularDialog() {
        mDialog.dismiss();
    }

    private void showCircularDialog() {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

}
