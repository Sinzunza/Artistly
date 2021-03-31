package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inzucorp.artistly.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Classes.userDB;
import Classes.otherUserDB;
import Models.chatsAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

import static Classes.artistlyDB.createChat;
import static Classes.artistlyDB.newMessage;
import static Classes.artistlyDB.orderedByQuery;

public class ChatFragment extends Fragment {

// declare layout ids
    TextView tvChatUsername;
    RecyclerView rvChat_Messages;
    EditText etChatTextMessage;
    ImageButton ibChatSend;

// local variables
    userDB theUserDB;
    otherUserDB theOtherUserDB;
    String textMessage;
    boolean chatExists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

    // initialize layout ids
        tvChatUsername = v.findViewById(R.id.tvChat_Username);
        rvChat_Messages = v.findViewById(R.id.rvChat_Messages);
        etChatTextMessage = v.findViewById(R.id.etChat_TextMessage);
        ibChatSend = v.findViewById(R.id.ibChat_Send);

    // set recycler adapter layout
        rvChat_Messages.setLayoutManager(new LinearLayoutManager(getActivity()));

    // retrieve arguments and call the adapter if necessary
        if (getArguments() != null) {
            theUserDB = new userDB();
            theOtherUserDB = new otherUserDB(getArguments().getString("messagingUserID"));
            theUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("messages/"+ theOtherUserDB.getUserID())){
                        chatExists = true;
                        chatAdapter();
                    }
                    else {
                        chatExists = false;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    // set onClickListeners
        ibChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessage = etChatTextMessage.getText().toString();
                if(!textMessage.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timeStamp = LocalDateTime.now().format(formatter);

                    if (chatExists) {
                        createChat(theOtherUserDB, textMessage, timeStamp);
                    } else {
                        newMessage(theOtherUserDB, textMessage, timeStamp);
                    }
                    etChatTextMessage.getText().clear();
                }
            }
        });

        return v;
    }

// load user data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        theOtherUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            // setting visitingUser's info in the activity
                tvChatUsername.setText(theOtherUserDB.getUsername(snapshot));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

// move the bar and change follow list based on typeAdapter
    private void chatAdapter() {

        Query chatsQuery = orderedByQuery("Users/" + theUserDB.getUserID() + "/messages/" + theOtherUserDB.getUserID(), "timeStamp");

        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<chatsAdapter> options = new FirebaseRecyclerOptions.Builder<chatsAdapter>().setQuery(chatsQuery, chatsAdapter.class).build();
        final FirebaseRecyclerAdapter<chatsAdapter, ChatFragment.chatsAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<chatsAdapter, ChatFragment.chatsAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatFragment.chatsAdapterViewHolder holder, final int position, @NonNull final chatsAdapter model) {
                holder.setInfo(getActivity(), model.getMessageText(), model.getSender());
            }
            @NonNull
            @Override
            public ChatFragment.chatsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chats, parent, false);
                return new ChatFragment.chatsAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvChat_Messages.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter. Maybe can just include this code in the firebaseUserSearch() but online everyone does it like this
    public static class chatsAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public chatsAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String messageText, String sender){
            final Context ctxF = ctx;
            TextView tvChatsAd_Message =  mView.findViewById(R.id.tvChatsAd_Message);
            final CircleImageView civChatsAdProfilePhoto = mView.findViewById(R.id.civChatsAd_ProfilePhoto);

            tvChatsAd_Message.setText(messageText);
            final otherUserDB sendingUserDB = new otherUserDB(sender);
            sendingUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(ctxF).load(sendingUserDB.getProfilePhoto(snapshot)).into(civChatsAdProfilePhoto);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            userDB theUserDB = new userDB();

            if(sender.equals(theUserDB.getUserID())){ // the constraints are only changed per each holder in the adapter. They don't get set permanently, therefore have to always change for the user

                ConstraintLayout clChatsAdLayout = mView.findViewById(R.id.clChatsAd_Layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(clChatsAdLayout);
                constraintSet.setVisibility(R.id.civChatsAd_ProfilePhoto, View.INVISIBLE);
                constraintSet.clear(R.id.clChatsAd_Message, ConstraintSet.START);
                constraintSet.clear(R.id.clChatsAd_Message, ConstraintSet.END);
                constraintSet.connect(R.id.clChatsAd_Message, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 20);
                constraintSet.connect(R.id.clChatsAd_Message, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 210);
                constraintSet.applyTo(clChatsAdLayout);
            }
        }
    }
}