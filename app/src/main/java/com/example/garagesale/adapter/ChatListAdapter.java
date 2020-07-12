package com.example.garagesale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.garagesale.ChatActivity;
import com.example.garagesale.R;
import com.example.garagesale.model.LastChatModel;
import com.example.garagesale.utils.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolderCategory>
{
    private Context context;
    List<LastChatModel> chatList;

    public ChatListAdapter(Context context, List<LastChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderCategory holder, final int position)
    {
        holder.tv_name.setText(chatList.get(position).getOtherName());
        holder.messageText.setText(chatList.get(position).getText());

        if(chatList.get(position).getDate()!=null)
        {
            holder.dateText.setText(Utility.convertTimeStampDate1(Long.parseLong(chatList.get(position).getDate())));
        }

        if(chatList.get(position).getDisplayName().length()>2)
        {
            holder.iv_user.setText(chatList.get(position).getDisplayName().substring(0,2));
        }
        else
        {
            holder.iv_user.setText(chatList.get(position).getDisplayName());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("key" , chatList.get(position).getKey());
                intent.putExtra("id" ,chatList.get(position).getOtheruserId());
                intent.putExtra("name" ,chatList.get(position).getOtherName());
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolderCategory extends RecyclerView.ViewHolder
    {
        TextView iv_user,tv_name,messageText,dateText;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);

            iv_user = itemView.findViewById(R.id.iv_user);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            messageText=(TextView)itemView.findViewById(R.id.messageText);
            dateText=(TextView)itemView.findViewById(R.id.dateText);

        }
    }
}
