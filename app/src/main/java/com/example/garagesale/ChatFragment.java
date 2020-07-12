package com.example.garagesale;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.garagesale.adapter.ChatListAdapter;
import com.example.garagesale.model.LastChatModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends Fragment
{
    public static String TAG = ChatFragment.class.getSimpleName();
    RecyclerView chatRecyclerView;
    ChatListAdapter chatListAdapter;
    List<LastChatModel> chatList =  new ArrayList<>();
    TextView no_content;
    private static boolean s_persistenceInitialized = false;
    ProgressDialog mDialog;
    FirebaseAuth mFirebaseAuth;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecyclerView=(RecyclerView)view.findViewById(R.id.chatRecyclerView);
        no_content=(TextView) view.findViewById(R.id.no_content);

        mFirebaseAuth = FirebaseAuth.getInstance();
        //ChatListAdapter chatListAdapter = new ChatListAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        chatRecyclerView.setLayoutManager(mLayoutManager1);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //chatRecyclerView.setAdapter(chatListAdapter);
        fetch();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            mOnFragmentInteraction = (OnFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    public void fetch()
    {
        showCircularDialog();
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       /* if (!s_persistenceInitialized) {
            database.setPersistenceEnabled(true);
            s_persistenceInitialized = true;
        }

        database.setLogLevel(Logger.Level.DEBUG);*/
        DatabaseReference myRef = database.getReference("RecentMessages");
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                hideCircularDialog();
                if (dataSnapshot.getValue() == null)
                    return;
                chatList.clear();

                HashMap chats = (HashMap) dataSnapshot.getValue();
                for (final Object entry : chats.keySet())
                {
                    HashMap chat = (HashMap) chats.get(entry);
                    HashMap lastText= (HashMap)chat.get("lastText");

                    for (final Object users : chat.keySet())
                    {
                        Log.d("users","-----"+users);

                        if(getUserID()!=null)
                        {
                            if(chat.size()==3)
                            {
                                if(chat.keySet().contains(getUserID()))
                                {
                                    if(!users.toString().equalsIgnoreCase(getUserID()) &&
                                            !users.toString().equalsIgnoreCase("lastText"))
                                    {
                                        String otheruserId=users.toString();
                                        HashMap otherMap= (HashMap)chat.get(otheruserId);
                                        String otherName=otherMap.get("DisplayName").toString();

                                        if(lastText!=null)
                                        {
                                            if(lastText.get("DisplayName")!=null)
                                            {
                                                LastChatModel lastChatModel=new LastChatModel(lastText.get("DisplayName").toString(),lastText.get("Sender_id").toString()
                                                        ,lastText.get("Text").toString(),lastText.get("Seen").toString(),lastText.get("type").toString(),lastText.get("Date").toString()
                                                        ,entry.toString(),otheruserId,otherName);

                                                chatList.add(lastChatModel);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Log.d("chatList","===="+chatList.size());

                Collections.sort(chatList, new Comparator<LastChatModel>()
                {
                    @Override
                    public int compare(LastChatModel lhs, LastChatModel rhs)
                    {

                        if (lhs.getDate().isEmpty() || lhs.getDate().equalsIgnoreCase(""))
                            return 0;
                        if (rhs.getDate().isEmpty() || rhs.getDate().equalsIgnoreCase(""))
                            return 0;
                        return (Double.valueOf(rhs.getDate()).compareTo(Double.valueOf(lhs.getDate())));
                    }
                });

                chatListAdapter = new ChatListAdapter(getActivity(), chatList);
                chatRecyclerView.setAdapter(chatListAdapter);

                if(chatListAdapter.getItemCount() == 0){
                    no_content.setVisibility(View.VISIBLE);
                } else {
                    no_content.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideCircularDialog();
            }
        });
    }

    private String getUserID() {
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        return mFirebaseUser.getUid();
    }

    private void hideCircularDialog() {
        mDialog.dismiss();
    }

    private void showCircularDialog() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

}
