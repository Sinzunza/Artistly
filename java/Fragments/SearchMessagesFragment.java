package Fragments;

import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.inzucorp.artistly.R;

import Classes.artistlyDB;
import Classes.userDB;
import Models.usersAdapter;

public class SearchMessagesFragment extends Fragment {

// declare layout ids
    FrameLayout flMessagingFragment;
    EditText etSearchMessagesSearch;
    RecyclerView rvSearchMessagesUsers;

// local variables
    userDB theUserDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_messages, container, false);
    // initialize local variables
        theUserDB = new userDB();
    // initialize layout ids
        flMessagingFragment = getActivity().findViewById(R.id.flMessaging_Fragment);
        etSearchMessagesSearch = v.findViewById(R.id.etSearchMessages_Search);
        rvSearchMessagesUsers = v.findViewById(R.id.rvSearchMessages_Users);

    // set recycler adapter layout
        rvSearchMessagesUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

    // add listener to search text changes so that the recycler adapter view is updated to whatever the app user is typing
        etSearchMessagesSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    searchFollowingAdapter(s.toString().toLowerCase());
                }
                else {
                    searchFollowingAdapter("");
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
        searchFollowingAdapter("");
    }

// query firebase for what is being searched and inflate the recycler view
    private void searchFollowingAdapter(String usernameSearch) {
        // query Users by their username matching usernameSearch
        Query usernameQuery = artistlyDB.startingWithQuery("Users", "followers" + "/" + theUserDB.getUserID(), usernameSearch);
        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<usersAdapter> options = new FirebaseRecyclerOptions.Builder<usersAdapter>().setQuery(usernameQuery, usersAdapter.class).build();
        final FirebaseRecyclerAdapter<usersAdapter, SearchExploreFragment.usersAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<usersAdapter, SearchExploreFragment.usersAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchExploreFragment.usersAdapterViewHolder holder, final int position, @NonNull final usersAdapter model) {
                holder.setInfo(getActivity(), model.getUsername(), model.getName(), model.getProfilePhoto());
                final userDB theUserDB = new userDB();
                final String messagingUserID = getRef(position).getKey();
                // if not user model then add an onClickListener to the view
                if (!theUserDB.getUserID().equals(messagingUserID)) {
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // pass the user that was clicked as an argument through the intent
                            final Fragment frChat = new ChatFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("messagingUserID", messagingUserID);
                            frChat.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(flMessagingFragment.getId(), frChat).addToBackStack(null).commit();
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
        rvSearchMessagesUsers.setAdapter(adapter);
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