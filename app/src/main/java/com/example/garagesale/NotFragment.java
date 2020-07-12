package com.example.garagesale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garagesale.adapter.NotificationAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NotFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not, container, false);

        recyclerView = view.findViewById(R.id.rcyNotification);
        adapter = new NotificationAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
