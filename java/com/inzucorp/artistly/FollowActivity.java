package com.inzucorp.artistly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import Models.usersAdapter;
import Classes.artistlyDB;
import Classes.userDB;
import Classes.otherUserDB;

public class FollowActivity extends AppCompatActivity {
// public enums. Needed in Profile Fragments.
    public enum ArtistlyFollow {
        Followers,
        Following
    }

// declare layout ids
    TextView tvFollowUsername, tvFollowFollowers, tvFollowFollowing;
    FrameLayout flFollowFollowersBar, flFollowFollowingBar;
    RecyclerView rvFollowUsers;
    ImageButton ibFollowBack, ibFollowHome, ibFollowMessages, ibFollowExplore, ibFollowNewPost, ibFollowProfile;

// local variables
    otherUserDB someUserDB; // someUser can be either the user or other user
    ArtistlyFollow followType = ArtistlyFollow.Followers;
    float x1, x2, y1; // used for swiping functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        tvFollowUsername = findViewById(R.id.tvFollow_Username);
        tvFollowFollowers = findViewById(R.id.tvFollow_Followers);
        tvFollowFollowing = findViewById(R.id.tvFollow_Following);
        flFollowFollowersBar = findViewById(R.id.flFollow_FollowersBar);
        flFollowFollowingBar = findViewById(R.id.flFollow_FollowingBar);
        rvFollowUsers = findViewById(R.id.rvFollow_Users);
        ibFollowBack = findViewById(R.id.ibFollow_Back);
        ibFollowHome = findViewById(R.id.ibFollow_Home);
        ibFollowMessages = findViewById(R.id.ibFollow_Messages);
        ibFollowExplore = findViewById(R.id.ibFollow_Explore);
        ibFollowNewPost = findViewById(R.id.ibFollow_NewPost);
        ibFollowProfile = findViewById(R.id.ibFollow_Profile);

    // set recycler adapter layout
        rvFollowUsers.setLayoutManager(new LinearLayoutManager(this));

    // set onClickListeners
        tvFollowFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followType != ArtistlyFollow.Followers){
                    followAdapter(ArtistlyFollow.Followers);
                }
            }
        });

        tvFollowFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followType != ArtistlyFollow.Following){
                    followAdapter(ArtistlyFollow.Following);
                }
            }
        });

        ibFollowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibFollowHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FollowActivity.this, HomeActivity.class));
            }
        });

        ibFollowMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FollowActivity.this, MessagingActivity.class));
            }
        });

        ibFollowExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FollowActivity.this, ExploreActivity.class));
            }
        });

        ibFollowNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FollowActivity.this, NewPostActivity.class));
            }
        });

        ibFollowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FollowActivity.this, ProfileActivity.class));
            }
        });
    }

// load username and show the appropriate follow adapter
    @Override
    protected void onResume()
    {
        super.onResume();
        someUserDB = new otherUserDB(getIntent().getStringExtra("someUserID")); // since just displaying username, we'll use visitingUserFirebase
        someUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvFollowUsername.setText(someUserDB.getUsername(snapshot));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        followAdapter((ArtistlyFollow) getIntent().getSerializableExtra("followType"));
    }

// swiping functionality
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                System.out.println("Y1 = " + y1 + "\n");
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                // swipe left, if user swiped left & swipe was done in fragment
                if(x1 > (x2 + 170) && y1 > 235) {
                    if(followType != ArtistlyFollow.Following) {
                        followAdapter(ArtistlyFollow.Following);
                    }
                }
                // swipe right, ...
                else if((x1 + 170) < x2 && y1 > 235) {
                    if(followType != ArtistlyFollow.Followers){
                        followAdapter(ArtistlyFollow.Followers);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

// move the bar and change follow list based on typeAdapter
    private void followAdapter(ArtistlyFollow typeAdapter) {
    // set which follow tab to underline and set the query
        Query usernameQuery;
        if(typeAdapter == ArtistlyFollow.Following){
            followType = ArtistlyFollow.Following;
            flFollowFollowingBar.setPadding(0,0,0,6);
            flFollowFollowersBar.setPadding(0,0,0,0);
            usernameQuery = artistlyDB.startingWithQuery("Users", "followers" + "/" + someUserDB.getUserID(), "");
        }
        else {
            followType = ArtistlyFollow.Followers;
            flFollowFollowersBar.setPadding(0,0,0,6);
            flFollowFollowingBar.setPadding(0,0,0,0);
            usernameQuery = artistlyDB.startingWithQuery("Users", "following" + "/" + someUserDB.getUserID(), "");
        }

    // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<usersAdapter> options = new FirebaseRecyclerOptions.Builder<usersAdapter>().setQuery(usernameQuery, usersAdapter.class).build();
        final FirebaseRecyclerAdapter<usersAdapter, usersAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<usersAdapter, usersAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull usersAdapterViewHolder holder, final int position, @NonNull final usersAdapter model) {
                holder.setInfo(getApplicationContext(), model.getUsername(), model.getName(), model.getProfilePhoto());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final userDB theUserDB = new userDB();
                        final String someUserID = getRef(position).getKey();
                    // if not user then load OthersProfileActivity (passing someUserID as argument), else load ProfileActivity
                        if (!theUserDB.getUserID().equals(someUserID)) {
                            Intent intent = new Intent(FollowActivity.this, ProfileActivity.class);
                            intent.putExtra("someUserID", someUserID);
                            startActivity(intent);
                        }
                        else {
                            startActivity(new Intent(FollowActivity.this, ProfileActivity.class));
                        }
                    }
                });
            }
            @NonNull
            @Override
            public usersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_users, parent, false);
                return new usersAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvFollowUsers.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter. Maybe can just include this code in the firebaseUserSearch() but online everyone does it like this
    public static class usersAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public usersAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String username, String name, String profilePhoto){
            final Context ctxF = ctx;
            TextView tvUsersAdUsername =  mView.findViewById(R.id.tvUsersAd_Usernames);
            TextView tvUsersAdName = mView.findViewById(R.id.tvUsersAd_Name);
            final ImageView civUsersAdProfilePhoto = mView.findViewById(R.id.civUsersAd_ProfilePhoto);

            tvUsersAdUsername.setText(username);
            tvUsersAdName.setText(name);
            Glide.with(ctxF).load(profilePhoto).into(civUsersAdProfilePhoto);
        }
    }
}

