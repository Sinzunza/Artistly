package com.inzucorp.artistly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import Fragments.PostMediaFragment;
import Fragments.PostServiceFragment;

public class PostActivity extends AppCompatActivity {

// declare layout ids
    FrameLayout flPostFragment;
    ImageButton ibPostHome, ibPostMessages, ibPostExplore, ibPostNewPost, ibPostProfile;

// local variables
    boolean isMedia;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        flPostFragment = findViewById(R.id.flPost_Fragment);
        ibPostHome = findViewById(R.id.ibPost_Home);
        ibPostMessages = findViewById(R.id.ibPost_Messages);
        ibPostExplore = findViewById(R.id.ibPost_Explore);
        ibPostNewPost = findViewById(R.id.ibPost_NewPost);
        ibPostProfile = findViewById(R.id.ibPost_Profile);

    // initialize local vairables
        isMedia = true;
        arguments = getIntent().getExtras();

    // open fragment depending on postType
        if (arguments.getBoolean("postType") == isMedia) {
            final Fragment frPostMedia = new PostMediaFragment();
            Bundle bundle = new Bundle();
            bundle.putString("postID", getIntent().getStringExtra("postID"));
            frPostMedia.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(flPostFragment.getId(), frPostMedia).commit();
        }
        else {
            final Fragment frPostServices = new PostServiceFragment();
            Bundle bundle = new Bundle();
            bundle.putString("postID", getIntent().getStringExtra("postID"));
            frPostServices.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(flPostFragment.getId(), frPostServices).commit();
        }

    // set onClickListeners
        ibPostHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, HomeActivity.class));
            }
        });

        ibPostMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MessagingActivity.class));
            }
        });

        ibPostExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, ExploreActivity.class));
            }
        });

        ibPostNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, NewPostActivity.class));
            }
        });

        ibPostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, ProfileActivity.class));
            }
        });
    }
}
