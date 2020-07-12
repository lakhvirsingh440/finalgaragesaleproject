package com.example.garagesale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.garagesale.adapter.ChatAdapter;
import com.example.garagesale.model.LastChatModel;
import com.example.garagesale.utils.ShareStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{
    RecyclerView chatRecyclerView;
    Context context;
    TextView userName,sendMessageBtn;

    ChatAdapter chatListAdapter;
    List<LastChatModel> chatList =  new ArrayList<>();
    String key,id,name;
    EditText sendMessageText;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context=this;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        key=getIntent().getStringExtra("key");
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");

        chatRecyclerView=(RecyclerView)findViewById(R.id.chatRecyclerView);
        userName=(TextView) findViewById(R.id.userName);
        sendMessageText=(EditText) findViewById(R.id.sendMessageText);
        sendMessageBtn=(TextView) findViewById(R.id.sendMessageBtn);

        userName.setText(name);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context);
        chatRecyclerView.setLayoutManager(mLayoutManager1);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Conversation").child(key);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sendMessageText.getText().toString().isEmpty())
                {}
                else
                {
                    HashMap chat = new HashMap();
                    chat.put("DisplayName", getUserName());
                    chat.put("Sender_id", getUserID());
                    chat.put("Text", sendMessageText.getText().toString());
                    chat.put("Seen",true);
                    chat.put("type","Text");
                    chat.put("Date", System.currentTimeMillis());

                    String mGroupId = myRef.push().getKey();
                    //chat.put("key", mGroupId);
                    myRef.child(mGroupId).setValue(chat);

                    HashMap chat1 = new HashMap();
                    chat1.put("DisplayName",name);
                    chat1.put("Sender_id", getUserID());
                    chat1.put("Text", sendMessageText.getText().toString());
                    chat1.put("Seen",true);
                    chat1.put("type","Text");
                    chat1.put("Date", System.currentTimeMillis());

                    DatabaseReference myRef1 = database.getReference("RecentMessages").child(key);
                    myRef1.child(getUserID()).setValue(chat);
                    myRef1.child(id).setValue(chat1);
                    myRef1.child("lastText").setValue(chat);

                    sendMessageText.setText("");
                }
            }
        });

        getChat(key);
    }



    void getChat(String key)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Conversation").child(key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getValue() == null)
                    return;
                chatList.clear();

                HashMap chats = (HashMap) dataSnapshot.getValue();

                for (final Object entry : chats.keySet())
                {
                    HashMap chat = (HashMap) chats.get(entry);

                    LastChatModel chatModel=new LastChatModel(chat.get("DisplayName").toString()
                            ,chat.get("Sender_id").toString()
                            ,chat.get("Text").toString()
                            ,chat.get("Seen").toString()
                            ,chat.get("type").toString()
                            ,chat.get("Date").toString()
                            ,entry.toString(),id,name);

                    chatList.add(chatModel);
                }

                Collections.sort(chatList, new Comparator<LastChatModel>()
                {
                    @Override
                    public int compare(LastChatModel lhs, LastChatModel rhs)
                    {

                        if (lhs.getDate().isEmpty() || lhs.getDate().equalsIgnoreCase(""))
                            return 0;
                        if (rhs.getDate().isEmpty() || rhs.getDate().equalsIgnoreCase(""))
                            return 0;
                        return (Double.valueOf(lhs.getDate()).compareTo(Double.valueOf(rhs.getDate())));
                    }
                });

                chatListAdapter = new ChatAdapter(context,chatList,getUserID());
                chatRecyclerView.setAdapter(chatListAdapter);

                if(chatList.size()>0)
                {
                    chatRecyclerView.scrollToPosition(chatList.size()-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private String getUserID() {
        return mFirebaseUser.getUid();
    }

    private String getUserName() {
        return ShareStorage.getUsername(this);
    }
}
