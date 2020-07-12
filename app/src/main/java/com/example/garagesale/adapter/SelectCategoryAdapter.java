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

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.SelectedCategoryViewHolder> {

    private Context context;
    private OnAdapterClickListener listener;
    private ArrayList<HashMap<String, String>> list;

    public SelectCategoryAdapter(Context context, OnAdapterClickListener listener) {
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
    public SelectedCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_select_category_item, parent, false);
        return new SelectedCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedCategoryViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).get("image"))
                .into(holder.img);

        holder.name.setText(list.get(position).get("name"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SelectedCategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public SelectedCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCategory);
            name = itemView.findViewById(R.id.txtCategoryName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
