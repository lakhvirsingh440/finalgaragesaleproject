package com.example.garagesale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagesale.R;
import com.example.garagesale.model.LastChatModel;
import com.example.garagesale.utils.Utility;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolderCategory>
{
    Context context;
    List<LastChatModel> chatList;
    String userID;

    public ChatAdapter(Context context,List<LastChatModel> chatList,String userID)
    {
        this.context = context;
        this.chatList = chatList;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderCategory holder, final int position)
    {
        if(chatList.get(position).getSender_id().equalsIgnoreCase(userID))
        {
            holder.receiveLay.setVisibility(View.GONE);
            holder.sendLay.setVisibility(View.VISIBLE);

            if(chatList.get(position).getDisplayName().length()>2)
            {
                holder.iv_user.setText(chatList.get(position).getDisplayName().substring(0,2));
            }
            else
            {
                holder.iv_user.setText(chatList.get(position).getDisplayName());
            }

            holder.tv_name.setText(chatList.get(position).getDisplayName());
            holder.messageText.setText(chatList.get(position).getText());
            if(chatList.get(position).getDate()!=null)
            {
                holder.datetext.setText(Utility.convertTimeStampDate1(Long.parseLong(chatList.get(position).getDate())));
            }
        }
        else
        {
            holder.receiveLay.setVisibility(View.VISIBLE);
            holder.sendLay.setVisibility(View.GONE);

            if(chatList.get(position).getDisplayName().length()>2)
            {
                holder.receiverImage.setText(chatList.get(position).getDisplayName().substring(0,2));
            }
            else
            {
                holder.receiverImage.setText(chatList.get(position).getDisplayName());
            }

            holder.receiverName.setText(chatList.get(position).getDisplayName());
            holder.receivermessageText.setText(chatList.get(position).getText());
            if(chatList.get(position).getDate()!=null)
            {
                holder.receiverDate.setText(Utility.convertTimeStampDate1(Long.parseLong(chatList.get(position).getDate())));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return chatList.size();
    }

    class ViewHolderCategory extends RecyclerView.ViewHolder
    {
        RelativeLayout sendLay,receiveLay;
        TextView iv_user,tv_name,messageText,datetext,receiverImage,receiverName,receivermessageText,receiverDate;

        public ViewHolderCategory(@NonNull View itemView)
        {
            super(itemView);

            sendLay=(RelativeLayout)itemView.findViewById(R.id.sendLay);
            receiveLay=(RelativeLayout)itemView.findViewById(R.id.receiveLay);

            iv_user=(TextView) itemView.findViewById(R.id.iv_user);
            receiverImage=(TextView) itemView.findViewById(R.id.receiverImage);
            receiverName=(TextView) itemView.findViewById(R.id.receiverName);
            receivermessageText=(TextView) itemView.findViewById(R.id.receivermessageText);
            receiverDate=(TextView) itemView.findViewById(R.id.receiverDate);
            tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            messageText=(TextView) itemView.findViewById(R.id.messageText);
            datetext=(TextView) itemView.findViewById(R.id.datetext);

        }
    }
}
