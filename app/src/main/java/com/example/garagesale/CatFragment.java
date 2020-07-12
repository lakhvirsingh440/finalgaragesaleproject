package com.example.garagesale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garagesale.adapter.SelectCategoryAdapter;
import com.example.garagesale.interfaces.OnAdapterClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CatFragment extends Fragment implements OnAdapterClickListener {

    private RecyclerView recyclerView;
    private SelectCategoryAdapter adapter;
    private DatabaseReference userReference;
    private ArrayList<HashMap<String, String>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cat, container, false);

        recyclerView = view.findViewById(R.id.rcySelectCategory);
        adapter = new SelectCategoryAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        userReference = FirebaseDatabase.getInstance().getReference().child("Category");
        getUserInfo();

        return view;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), ProductListActivity.class);
        intent.putExtra("id", position);
        intent.putExtra("name", list.get(position).get("name"));
        startActivity(intent);
    }

    private void getUserInfo() {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.dismiss();
                if (dataSnapshot.exists()) {
                    list = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                    Log.e("Size", list.size()+"//");
                    adapter.updateAdapter(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }
}
