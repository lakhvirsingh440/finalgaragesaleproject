package com.example.garagesale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    Button btnEx, btnsin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        btnEx = (Button) rootView.findViewById(R.id.signin);
        btnsin = (Button) rootView.findViewById(R.id.signup);
        btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getActivity(), Login.class);
                startActivity(myintent);
            }
        });
        btnsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getActivity(), signup.class);
                startActivity(myintent);
            }
        });
        return rootView;
    }



}
