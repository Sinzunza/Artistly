package com.inzucorp.artistly;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import Models.postsAdapter;
import Classes.artistlyDB;
import Classes.visitingUserDB;
import Fragments.ProfileUserFragment;
import Fragments.ProfileVisitingFragment;

public class ProfileActivity extends AppCompatActivity {

// declare layout ids
    ImageView ivProfileServices, ivProfileMedia;
    ImageButton ibProfileHome, ibProfileMessages, ibProfileExplore, ibProfileNewPost, ibProfileProfile;
    FrameLayout flProfileFragment, flProfileServicesBar, flProfileMediaBar;
    RecyclerView rvProfilePosts;

// local variables
    visitingUserDB someUserDB;
    ArtistlyPost postType;
    float x1, x2, y1; // for swiping functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

    // initialize layout ids
        ivProfileMedia = findViewById(R.id.ivProfile_Media);
        ivProfileServices = findViewById(R.id.ivProfile_Services);
        ibProfileHome = findViewById(R.id.ibProfile_Home);
        ibProfileMessages = findViewById(R.id.ibProfile_Messages);
        ibProfileExplore = findViewById(R.id.ibProfile_Explore);
        ibProfileNewPost = findViewById(R.id.ibProfile_NewPost);
        ibProfileProfile = findViewById(R.id.ibProfile_Profile);
        flProfileFragment = findViewById(R.id.flProfile_Fragment);
        flProfileMediaBar = findViewById(R.id.flProfile_MediaBar);
        flProfileServicesBar = findViewById(R.id.flProfile_ServicesBar);
        rvProfilePosts = findViewById(R.id.rvProfile_Posts);

    // initiate local variables
        String someUserID = getIntent().getStringExtra("someUserID");
        if (someUserID == null){
            someUserDB =  new visitingUserDB(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final Fragment frProfileUser = new ProfileUserFragment();
            getSupportFragmentManager().beginTransaction().add(flProfileFragment.getId(), frProfileUser).commit();
        }
        else {
            someUserDB =  new visitingUserDB(someUserID);
            final Fragment frProfileVisitingUser= new ProfileVisitingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("visitingUserID", getIntent().getStringExtra("someUserID"));
            frProfileVisitingUser.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(flProfileFragment.getId(), frProfileVisitingUser).commit();
        }
        postType = ArtistlyPost.Media;

    // initialize adapter to media
        rvProfilePosts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

    // set onClickListeners
        ivProfileMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postType != ArtistlyPost.Media) {
                    profilePostsAdapter(ArtistlyPost.Media);
                }
            }
        });

        ivProfileServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postType != ArtistlyPost.Service) {
                    profilePostsAdapter(ArtistlyPost.Service);
                }
            }
        });

        ibProfileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        ibProfileMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MessagingActivity.class));
            }
        });

        ibProfileExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ExploreActivity.class));
            }
        });

        ibProfileNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, NewPostActivity.class));
            }
        });

        ibProfileProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            }
        });

    }

// retrieve user data every time activity is opened
    @Override
    protected void onResume() {
        super.onResume();
        profilePostsAdapter(ArtistlyPost.Media);
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
                if(x1 > (x2 + 170) && y1 > 670) {
                    if (postType != ArtistlyPost.Service) {
                        profilePostsAdapter(ArtistlyPost.Service);
                    }
                }
            // swipe right, ...
                else if((x1 + 170) < x2 && y1 > 670) {
                    if (postType != ArtistlyPost.Media) {
                        profilePostsAdapter(ArtistlyPost.Media);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

// move bar and change post type based on typeAdapter
    private void profilePostsAdapter(ArtistlyPost typeAdapter) {
    // set which post tab to underline and set the query
        Query postQuery;
        if (typeAdapter == ArtistlyPost.Media){
            postType = ArtistlyPost.Media;
            flProfileMediaBar.setPadding(0,0,0,6);
            flProfileServicesBar.setPadding(0,0,0,0);
            postQuery = artistlyDB.equalToQuery("Media", "user", someUserDB.getUserID());
        }
        else {
            postType = ArtistlyPost.Service;
            flProfileServicesBar.setPadding(0,0,0,6);
            flProfileMediaBar.setPadding(0,0,0,0);
            postQuery = artistlyDB.equalToQuery("Services", "user", someUserDB.getUserID());
        }

    // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<postsAdapter> options = new FirebaseRecyclerOptions.Builder<postsAdapter>().setQuery(postQuery, postsAdapter.class).build();
        final FirebaseRecyclerAdapter<postsAdapter, postsAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<postsAdapter, postsAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileActivity.postsAdapterViewHolder holder, final int position, @NonNull final postsAdapter model) {
                holder.setInfo(getApplicationContext(), model.getPhoto());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String postID = getRef(position).getKey();
                        Intent intent = new Intent(ProfileActivity.this, PostActivity.class);
                        intent.putExtra("postID", postID);
                        intent.putExtra("postType", postType);
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public ProfileActivity.postsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_profile_posts, parent, false);
                return new ProfileActivity.postsAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvProfilePosts.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter
    public static class postsAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public postsAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String photo){
            final Context ctxF = ctx;
            final ImageView ivProfilePostsMedia = mView.findViewById(R.id.ivProfilePosts_Image);
            Glide.with(ctxF).load(photo).into(ivProfilePostsMedia);
        }
    }

}