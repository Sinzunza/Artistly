package Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import Classes.otherUserDB;
import Classes.userDB;
import Models.chatsAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

import static Classes.artistlyDB.orderedByQuery;
import static Classes.timeParser.timeRevert;

public class ChatsFragment extends Fragment {

// declare layout ids
    FrameLayout flMessagingFragment;
    ImageButton ibChatsNewChat;
    RecyclerView rvChats_Chats;

// local variables
    userDB theUserDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragmentS
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        flMessagingFragment = getActivity().findViewById(R.id.flMessaging_Fragment);
        ibChatsNewChat = v.findViewById(R.id.ibChats_NewChat);
        rvChats_Chats = v.findViewById(R.id.rvChats_Chats);

    // initiate local variables
        theUserDB = new userDB();

    // set recycler adapter layout
        rvChats_Chats.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsAdapter();

    // set onClickListeners
        ibChatsNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment frSearchMessages = new SearchMessagesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(flMessagingFragment.getId(), frSearchMessages).addToBackStack(null).commit();
            }
        });

        return v;
    }

// move the bar and change follow list based on typeAdapter
    private void chatsAdapter() {

        Query chatsQuery = orderedByQuery("Users/" + theUserDB.getUserID() + "/chats/lastMessages", "timeStamp");

        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<chatsAdapter> options = new FirebaseRecyclerOptions.Builder<chatsAdapter>().setQuery(chatsQuery,chatsAdapter.class).build();
        final FirebaseRecyclerAdapter<chatsAdapter, chatsAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<chatsAdapter, ChatsFragment.chatsAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsFragment.chatsAdapterViewHolder holder, final int position, @NonNull final chatsAdapter model) {
                holder.setInfo(getActivity(), model.getMessageText(), model.getUser(), model.getTimeStamp());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pass the user that was clicked as an argument through the intent
                        final Fragment frMessages = new MessagesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("messagingUserID", model.getUser());
                        frMessages.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(flMessagingFragment.getId(), frMessages).commit();
                    }
                });
            }
            @NonNull
            @Override
            public ChatsFragment.chatsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chats, parent, false);
                return new ChatsFragment.chatsAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvChats_Chats.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter. Maybe can just include this code in the firebaseUserSearch() but online everyone does it like this
    public static class chatsAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public chatsAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String messageText, String user, String timeStamp){
            final Context ctxF = ctx;
            final TextView tvChatsAdUsername = mView.findViewById(R.id.tvChatsAd_Username);
            TextView tvChatsAdMessage = mView.findViewById(R.id.tvChatsAd_Message);
            TextView tvChatsAdTime = mView.findViewById(R.id.tvChatsAd_Time);
            final CircleImageView civChatsAdProfilePhoto = mView.findViewById(R.id.civChatsAd_ProfilePhoto);

            tvChatsAdMessage.setText(messageText);
            tvChatsAdTime.setText(timeRevert(timeStamp));

            final otherUserDB theOtherUserDB = new otherUserDB(user);
            theOtherUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvChatsAdUsername.setText(theOtherUserDB.getUsername(snapshot));
                    Glide.with(ctxF).load(theOtherUserDB.getProfilePhoto(snapshot)).into(civChatsAdProfilePhoto);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}