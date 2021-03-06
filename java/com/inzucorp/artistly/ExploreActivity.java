package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import Fragments.ExploreFragment;

public class ExploreActivity extends AppCompatActivity {

// declare layout ids
    FrameLayout flExploreFragment;
    ImageButton ibExploreHome, ibExploreMessages, ibExploreExplore, ibExploreNewPost, ibExploreProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

    // initialize layout ids
        flExploreFragment = findViewById(R.id.flExplore_Fragment);
        ibExploreHome = findViewById(R.id.ibExplore_Home);
        ibExploreMessages = findViewById(R.id.ibExplore_Messages);
        ibExploreExplore = findViewById(R.id.ibExplore_Explore);
        ibExploreNewPost = findViewById(R.id.ibExplore_NewPost);
        ibExploreProfile = findViewById(R.id.ibExplore_Profile);

    // initiate explore fragment
        final Fragment frExplore = new ExploreFragment();
        getSupportFragmentManager().beginTransaction().add(flExploreFragment.getId(), frExplore).commit();

    // set onClickListeners
        ibExploreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, HomeActivity.class));
            }
        });

        ibExploreMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, MessagingActivity.class));
            }
        });

        ibExploreExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, ExploreActivity.class));
            }
        });

        ibExploreNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, NewPostActivity.class));
            }
        });

        ibExploreProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, ProfileActivity.class));
            }
        });
    }
}
