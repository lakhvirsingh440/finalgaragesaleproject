package com.example.garagesale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.garagesale.adapter.CategoryAdapter;
import com.example.garagesale.adapter.CategoryModel;
import com.example.garagesale.interfaces.OnAdapterClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements OnAdapterClickListener {
    View view;
    ImageView furniture;
    private DatabaseReference userReference;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<HashMap<String, String>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view  = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = view.findViewById(R.id.rcyCategory);
        categoryAdapter = new CategoryAdapter(getContext(), this);
        recyclerView.setAdapter(categoryAdapter);

        userReference = FirebaseDatabase.getInstance().getReference().child("Category");
        getUserInfo();



        return view;
    }

    private void getUserInfo() {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Uploading...");
        mDialog.setCancelable(false);
        mDialog.show();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String message = dataSnapshot.child("avatarUrl").getValue(String.class);
                //Picasso.get().load(message).into(img);
                mDialog.dismiss();
                if (dataSnapshot.exists()) {
                    list = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                    Log.e("Size", list.size()+"//");
                    categoryAdapter.updateAdapter(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), ProductListActivity.class);
        intent.putExtra("id", position);
        intent.putExtra("name", list.get(position).get("name"));
        startActivity(intent);
    }
}
