package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.inzucorp.artistly.ProfileActivity;
import com.inzucorp.artistly.R;

import Classes.artistlyDB;
import Classes.userDB;
import Models.usersAdapter;

public class SearchExploreFragment extends Fragment {

// declare layout ids
    EditText etSearchExploreSearch;
    RecyclerView rvSearchExploreUsers;

// local variables
    userDB theUserDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_explore, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize local variables
        theUserDB = new userDB();
    // initialize layout ids
        etSearchExploreSearch = v.findViewById(R.id.etSearchExplore_Search);
        rvSearchExploreUsers = v.findViewById(R.id.rvSearchExplore_Users);

    // set recycler adapter layout
        rvSearchExploreUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

    // add listener to search text changes so that the recycler adapter view is updated to whatever the app user is typing
        etSearchExploreSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    searchAllAdapter(s.toString().toLowerCase());
                }
                else {
                    searchAllAdapter("");
                }
            }
        });

        return v;
    }

// initiate adapter when activity is opened
    @Override
    public void onResume()
    {
        super.onResume();
        // initiate the view to show all users
        searchAllAdapter("");
    }

// query firebase for what is being searched and inflate the recycler view
    private void searchAllAdapter(String usernameSearch) {
        // query Users by their username matching usernameSearch
        Query usernameQuery = artistlyDB.startingWithQuery("Users", "usernameLowerCase", usernameSearch);
        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<usersAdapter> options = new FirebaseRecyclerOptions.Builder<usersAdapter>().setQuery(usernameQuery, usersAdapter.class).build();
        final FirebaseRecyclerAdapter<usersAdapter, SearchExploreFragment.usersAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<usersAdapter, SearchExploreFragment.usersAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchExploreFragment.usersAdapterViewHolder holder, final int position, @NonNull final usersAdapter model) {
                holder.setInfo(getActivity(), model.getUsername(), model.getName(), model.getProfilePhoto());
                final userDB theUserDB = new userDB();
                final String someUserID = getRef(position).getKey();
                // if not user model then add an onClickListener to the view
                if (!theUserDB.getUserID().equals(someUserID)) {
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // pass the user that was clicked as an argument through the intent
                            Intent intentOthers = new Intent(getActivity(), ProfileActivity.class);
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
            public SearchExploreFragment.usersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_users, parent, false);
                return new SearchExploreFragment.usersAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvSearchExploreUsers.setAdapter(adapter);
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
            TextView tvUsersAdUsername =  mView.findViewById(R.id.tvUsersAd_Usernames);
            TextView tvUsersAdName = mView.findViewById(R.id.tvUsersAd_Name);
            final ImageView civUsersAdProfilePhoto = mView.findViewById(R.id.civUsersAd_ProfilePhoto);

            tvUsersAdUsername.setText(username);
            tvUsersAdName.setText(name);
            Glide.with(ctxF).load(profilePhoto).into(civUsersAdProfilePhoto);
        }
    }
}