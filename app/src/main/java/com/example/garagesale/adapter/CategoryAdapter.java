package com.example.garagesale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.garagesale.R;
import com.example.garagesale.interfaces.OnAdapterClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private OnAdapterClickListener listener;
    private ArrayList<HashMap<String, String>> list;

    public CategoryAdapter(Context context, OnAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();
    }

    public void updateAdapter(ArrayList<HashMap<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).get("image"))
                .into(holder.img);

        holder.name.setText(list.get(position).get("name"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_category);
            name = itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
