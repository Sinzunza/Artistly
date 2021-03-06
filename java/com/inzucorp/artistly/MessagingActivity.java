package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import Fragments.ExploreFragment;
import Fragments.MessagesFragment;
import Fragments.PostMediaFragment;

public class MessagingActivity extends AppCompatActivity {

// declare layout ids
    FrameLayout flMessagingFragment;
    ImageButton ibMessagingHome, ibMessagingMessaging, ibMessagingExplore, ibMessagingNewPost, ibMessagingProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

    // initialize layout ids
        flMessagingFragment = findViewById(R.id.flMessaging_Fragment);
        ibMessagingHome = findViewById(R.id.ibMessaging_Home);
        ibMessagingMessaging = findViewById(R.id.ibMessaging_Messages);
        ibMessagingExplore = findViewById(R.id.ibMessaging_Explore);
        ibMessagingNewPost = findViewById(R.id.ibMessaging_NewPost);
        ibMessagingProfile = findViewById(R.id.ibMessaging_Profile);

    // initiate explore fragment
        final Fragment frMessages = new MessagesFragment();
        getSupportFragmentManager().beginTransaction().add(flMessagingFragment.getId(), frMessages).commit();

    // set onClickListeners
        ibMessagingHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, HomeActivity.class));
            }
        });

        ibMessagingMessaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, MessagingActivity.class));
            }
        });

        ibMessagingExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, ExploreActivity.class));
            }
        });

        ibMessagingNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, NewPostActivity.class));
            }
        });

        ibMessagingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, ProfileActivity.class));
            }
        });
    }
}
