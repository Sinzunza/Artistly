package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MessagesActivity extends AppCompatActivity {

// declare layout ids
    ImageButton ibMessagesHome, ibMessagesMessages, ibMessagesExplore, ibMessagesNewPost, ibMessagesProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

    // initialize layout ids
        ibMessagesHome = findViewById(R.id.ibMessages_Home);
        ibMessagesMessages = findViewById(R.id.ibMessages_Messages);
        ibMessagesExplore = findViewById(R.id.ibMessages_Explore);
        ibMessagesNewPost = findViewById(R.id.ibMessages_NewPost);
        ibMessagesProfile = findViewById(R.id.ibMessages_Profile);

    // set onClickListeners
        ibMessagesHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagesActivity.this, HomeActivity.class));
            }
        });

        ibMessagesMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagesActivity.this, MessagesActivity.class));
            }
        });

        ibMessagesExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagesActivity.this, ExploreActivity.class));
            }
        });

        ibMessagesNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagesActivity.this, NewPostActivity.class));
            }
        });

        ibMessagesProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagesActivity.this, ProfileActivity.class));
            }
        });
    }
}
