package Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import Classes.userDB;
import Classes.otherUserDB;
import Models.messagesAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

import static Classes.artistlyDB.orderedByQuery;

public class MessagesFragment extends Fragment {

// declare layout ids
    TextView tvMessagesUsername;
    RecyclerView rvMessages_Messages;
    EditText etMessagesTextMessage;
    ImageButton ibMessagesSend;

// local variables
    userDB theUserDB;
    otherUserDB theOtherUserDB;
    String textMessage;
    boolean chatExists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        tvMessagesUsername = v.findViewById(R.id.tvMessages_Username);
        rvMessages_Messages = v.findViewById(R.id.rvMessages_Messages);
        etMessagesTextMessage = v.findViewById(R.id.etMessages_TextMessage);
        ibMessagesSend = v.findViewById(R.id.ibMessages_Send);

    // set recycler adapter layout
        rvMessages_Messages.setLayoutManager(new LinearLayoutManager(getActivity()));

    // retrieve arguments and call the adapter if necessary
        if (getArguments() != null) {
            theUserDB = new userDB();
            theOtherUserDB = new otherUserDB(getArguments().getString("messagingUserID"));
            theUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("chats/"+ theOtherUserDB.getUserID())){
                        chatExists = true;
                    }
                    else {
                        chatExists = false;
                    }
                    messagesAdapter();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    // set onClickListeners
        ibMessagesSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessage = etMessagesTextMessage.getText().toString();
                if(!textMessage.isEmpty()) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timeStamp = OffsetDateTime.now(ZoneOffset.UTC).format(formatter);

                    if (!chatExists) {
                        theUserDB.createChat(theOtherUserDB, textMessage, timeStamp);
                    } else {
                        theUserDB.newMessage(theOtherUserDB, textMessage, timeStamp);
                    }
                    etMessagesTextMessage.getText().clear();
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
                tvMessagesUsername.setText(theOtherUserDB.getUsername(snapshot));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

// move the bar and change follow list based on typeAdapter
    private void messagesAdapter() {

        Query messagesQuery = orderedByQuery("Users/" + theUserDB.getUserID() + "/chats/" + theOtherUserDB.getUserID(), "timeStamp");

        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<messagesAdapter> options = new FirebaseRecyclerOptions.Builder<messagesAdapter>().setQuery(messagesQuery, messagesAdapter.class).build();
        final FirebaseRecyclerAdapter<messagesAdapter, MessagesFragment.messagesAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<messagesAdapter, MessagesFragment.messagesAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessagesFragment.messagesAdapterViewHolder holder, final int position, @NonNull final messagesAdapter model) {
                holder.setInfo(getActivity(), model.getMessageText(), model.getSender());
            }
            @NonNull
            @Override
            public MessagesFragment.messagesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_messages, parent, false);
                return new MessagesFragment.messagesAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvMessages_Messages.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter. Maybe can just include this code in the firebaseUserSearch() but online everyone does it like this
    public static class messagesAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public messagesAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String messageText, String sender){
            final Context ctxF = ctx;
            TextView tvMessagesAdMessage =  mView.findViewById(R.id.tvMessagesAd_Message);
            final CircleImageView civMessagesAdProfilePhoto = mView.findViewById(R.id.civMessagesAd_ProfilePhoto);

            tvMessagesAdMessage.setText(messageText);
            final otherUserDB sendingUserDB = new otherUserDB(sender);
            sendingUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(ctxF).load(sendingUserDB.getProfilePhoto(snapshot)).into(civMessagesAdProfilePhoto);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            userDB theUserDB = new userDB();
            GradientDrawable messageShape =  new GradientDrawable();
            messageShape.setCornerRadius(30);

            if(sender.equals(theUserDB.getUserID())){ // the constraints are only changed per each holder in the adapter. They don't get set permanently, therefore have to always change for the user

                ConstraintLayout clMessagesAdLayout = mView.findViewById(R.id.clMessagesAd_Layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(clMessagesAdLayout);
                constraintSet.setVisibility(R.id.civMessagesAd_ProfilePhoto, View.INVISIBLE);
                constraintSet.clear(R.id.clMessagesAd_Message, ConstraintSet.START);
                constraintSet.connect(R.id.clMessagesAd_Message, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 20);
                constraintSet.applyTo(clMessagesAdLayout);

                messageShape.setColor(Color.parseColor("#008ae6")); // light blue
                mView.findViewById(R.id.clMessagesAd_Message).setBackground(messageShape);
                tvMessagesAdMessage.setTextColor(Color.WHITE);
            }
            else {

                messageShape.setColor(Color.parseColor("#e6e6e6")); // light gray
                mView.findViewById(R.id.clMessagesAd_Message).setBackground(messageShape);
            }
        }
    }
}