package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExploreActivity extends AppCompatActivity {

// declare layout ids
    TextView tvExploreSearch;
    ImageButton ibExploreHome, ibExploreMessages, ibExploreExplore, ibExploreNewPost, ibExploreProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

    // initialize layout ids
        tvExploreSearch = findViewById(R.id.tvExplore_Search);
        ibExploreHome = findViewById(R.id.ibExplore_Home);
        ibExploreMessages = findViewById(R.id.ibExplore_Messages);
        ibExploreExplore = findViewById(R.id.ibExplore_Explore);
        ibExploreNewPost = findViewById(R.id.ibExplore_NewPost);
        ibExploreProfile = findViewById(R.id.ibExplore_Profile);

    // set onClickListeners
        tvExploreSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, SearchActivity.class));
            }
        });
        ibExploreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, HomeActivity.class));
            }
        });

        ibExploreMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, MessagesActivity.class));
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
