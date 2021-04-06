package com.inzucorp.artistly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

// if there is no user, then open create account page, else delete any previous visitngUserID and visitedUsers values and open explore page
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(SplashScreenActivity.this, ExploreActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(SplashScreenActivity.this, CreateAccountActivity.class));
            finish();
        }
    }
}