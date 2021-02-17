package com.inzucorp.artistly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;

import Classes.usersAdapter;
import Classes.artistlyDB;
import Classes.userDB;

public class SearchActivity extends AppCompatActivity {

// declare layout ids
    EditText etSearchSearch;
    RecyclerView rvSearchUsers;
    ImageButton ibSearchHome, ibSearchMessages, ibSearchExplore, ibSearchNewPost, ibSearchProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    // initialize layout ids
        etSearchSearch = findViewById(R.id.etSearch_Search);
        rvSearchUsers = findViewById(R.id.rvSearch_Users);
        ibSearchHome = findViewById(R.id.ibSearch_Home);
        ibSearchMessages = findViewById(R.id.ibSearch_Messages);
        ibSearchExplore = findViewById(R.id.ibSearch_Explore);
        ibSearchNewPost = findViewById(R.id.ibSearch_NewPost);
        ibSearchProfile = findViewById(R.id.ibSearch_Profile);

    // set recycler adapter layout
        rvSearchUsers.setLayoutManager(new LinearLayoutManager(this));

    // add listener to search text changes so that the recycler adapter view is updated to whatever the app user is typing
        etSearchSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    searchAdapter(s.toString().toLowerCase());
                }
                else {
                    searchAdapter("");
                }
            }
        });

    // set onClickListeners
        ibSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, HomeActivity.class));
            }
        });

        ibSearchMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MessagesActivity.class));
            }
        });

        ibSearchExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, ExploreActivity.class));
            }
        });

        ibSearchNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, NewPostActivity.class));
            }
        });

        ibSearchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    // initiate the view to show all users
        searchAdapter("");
    }

// query firebase for what is being searched and inflate the recycler view
    private void searchAdapter(String usernameSearch) {
    // query Users by their username matching usernameSearch
        Query usernameQuery = artistlyDB.startingWithQuery("Users", "usernameLowerCase", usernameSearch);
    // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<usersAdapter> options = new FirebaseRecyclerOptions.Builder<usersAdapter>().setQuery(usernameQuery, usersAdapter.class).build();
        final FirebaseRecyclerAdapter<usersAdapter, usersAdapterViewHolder>  adapter = new FirebaseRecyclerAdapter<usersAdapter, usersAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull usersAdapterViewHolder holder, final int position, @NonNull final usersAdapter model) {
                holder.setInfo(getApplicationContext(), model.getUsername(), model.getName(), model.getProfilePhoto());
                final userDB theUserDB = new userDB();
                final String someUserID = getRef(position).getKey();
            // if not user model then add an onClickListener to the view
                if (!theUserDB.getUserID().equals(someUserID)) {
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        // pass the user that was clicked as an argument through the intent
                            Intent intentOthers = new Intent(SearchActivity.this, ProfileActivity.class);
                            intentOthers.putExtra("someUserID", someUserID);
                            startActivity(intentOthers);
                        }
                    });
                }
            // if user model then hide the view
                else {
                    ViewGroup.LayoutParams params = holder.mView.getLayoutParams();
                    params.height = 0;
                    holder.mView.setLayoutParams(params);
                    holder.mView.setVisibility(View.GONE);
                }
            }
            @NonNull
            @Override
            public usersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_users, parent, false);
                return new usersAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvSearchUsers.setAdapter(adapter);
    }

// view Holder Class needed for recycler adapter. Maybe can just include this code in the firebaseUserSearch() but online everyone does it like this
    public static class usersAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public usersAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String username, String name, String profilePhoto){
            final Context ctxF = ctx;
            TextView tvUsersUsername =  mView.findViewById(R.id.tvUsers_Usernames);
            TextView tvUsersName = mView.findViewById(R.id.tvUsers_Name);
            final ImageView civUsersProfilePhoto = mView.findViewById(R.id.civUsers_ProfilePhoto);

            tvUsersUsername.setText(username);
            tvUsersName.setText(name);
            Glide.with(ctxF).load(profilePhoto).into(civUsersProfilePhoto);
        }
    }
}