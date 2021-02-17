package com.inzucorp.artistly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Classes.userDB;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

// declare layout ids
    CircleImageView civEditProfilePhoto;
    EditText etEditProfileName, etEditProfileBio;
    TextView tvEditProfileChangePhoto, tvEditProfileDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

    // initialize layout ids
        civEditProfilePhoto = findViewById(R.id.civEditProfile_Photo);
        tvEditProfileChangePhoto = findViewById(R.id.tvEditProfile_ChangePhoto);
        etEditProfileName = findViewById(R.id.etEditProfile_Name);
        etEditProfileBio = findViewById(R.id.etEditProfile_Bio);
        tvEditProfileDone = findViewById(R.id.tvEditProfile_Done);

    // retrieve values from firebase and set them in the activity
        final userDB theUserDB = new userDB();
        theUserDB.getDBRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etEditProfileName.setText(theUserDB.getName(snapshot));
                etEditProfileBio.setText(theUserDB.getBio(snapshot));
                Glide.with(getApplicationContext()).load(theUserDB.getProfilePhoto(snapshot)).into(civEditProfilePhoto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    // open up the gallery upon user tapping change photo. Basically change to CropPhotoActivity
        tvEditProfileChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, CropPhotoActivity.class));
            }
        });

    // update values after user clicks done
        tvEditProfileDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etEditProfileName.getText().toString();
                String bio = etEditProfileBio.getText().toString();
                final userDB theUserDB = new userDB();
                Map newPost = new HashMap();
                newPost.put("name",  name);
                newPost.put("bio",  bio);
                theUserDB.addVals(newPost);
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}
