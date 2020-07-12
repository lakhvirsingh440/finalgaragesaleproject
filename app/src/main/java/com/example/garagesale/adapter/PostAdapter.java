package com.example.garagesale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.garagesale.R;
import com.example.garagesale.interfaces.OnAdapterClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private OnAdapterClickListener listener;
    private ArrayList<HashMap<String, String>> list;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public PostAdapter(Context context, OnAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateAdapter(ArrayList<HashMap<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).get("image"))
                .into(holder.img);

        holder.name.setText(list.get(position).get("name"));
        holder.location.setText(list.get(position).get("address"));
        holder.phone.setText(list.get(position).get("phone"));
        holder.price.setText("Price: $"+list.get(position).get("Price"));
        holder.des.setText(list.get(position).get("description"));

        if (mFirebaseUser != null) {
            if (mFirebaseUser.getUid().equals(list.get(position).get("userId"))) {
                holder.chat.setVisibility(View.GONE);

            } else {
                holder.chat.setVisibility(View.VISIBLE);

            }
        } else {
            holder.chat.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, location, phone, price, des;
        Button chat;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgPost);
            name = itemView.findViewById(R.id.txtName);
            des = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.txtLocation);
            phone = itemView.findViewById(R.id.txtPhone);
            price = itemView.findViewById(R.id.txtPrice);
            chat = itemView.findViewById(R.id.btnChat);

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
