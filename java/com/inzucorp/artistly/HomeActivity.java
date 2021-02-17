package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

// declare layout ids
    ImageButton ibHomeHome, ibHomeMessages, ibHomeExplore, ibHomeNewPost, ibHomeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    // initialize layout ids
        ibHomeHome= findViewById(R.id.ibHome_Home);
        ibHomeMessages = findViewById(R.id.ibHome_Messages);
        ibHomeExplore = findViewById(R.id.ibHome_Explore);
        ibHomeNewPost = findViewById(R.id.ibHome_NewPost);
        ibHomeProfile = findViewById(R.id.ibHome_Profile);

    // set onClickListeners
        ibHomeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            }
        });

        ibHomeMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MessagesActivity.class));
            }
        });

        ibHomeExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
            }
        });

        ibHomeNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
            }
        });

        ibHomeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
    }
}
