package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inzucorp.artistly.EditProfileActivity;
import com.inzucorp.artistly.FollowActivity;
import com.inzucorp.artistly.R;
import com.inzucorp.artistly.SettingsActivity;

import Classes.userDB;

public class ProfileUserFragment extends Fragment {

// declare layout ids
    TextView tvProfileUserUsername, tvProfileUserName, tvProfileUserTypeArtist, tvProfileUserFollowersNum, tvProfileUserFollowingNum,
             tvProfileUserFollowers, tvProfileUserFollowing, tvProfileUserBio, tvProfileUserEditProfile;
    ImageView civProfileUserPhoto;
    ImageButton ibProfileUserSettings;

// local variables
    userDB theUserDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // initialize local variables
        theUserDB = new userDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_user, container, false);
    // initialize layout ids
        tvProfileUserUsername = v.findViewById(R.id.tvProfileUser_Username);
        tvProfileUserName = v.findViewById(R.id.tvProfileUser_Name);
        tvProfileUserTypeArtist = v.findViewById(R.id.tvProfileUser_TypeArtist);
        tvProfileUserFollowersNum = v.findViewById(R.id.tvProfileUser_FollowersNum);
        tvProfileUserFollowingNum = v.findViewById(R.id.tvProfileUser_FollowingNum);
        tvProfileUserFollowers = v.findViewById(R.id.tvProfileUser_Followers);
        tvProfileUserFollowing = v.findViewById(R.id.tvProfileUser_Following);
        tvProfileUserBio = v.findViewById(R.id.tvProfileUser_Bio);
        tvProfileUserEditProfile = v.findViewById(R.id.tvProfileUser_EditProfile);
        civProfileUserPhoto = v.findViewById(R.id.civProfileUser_Photo);
        ibProfileUserSettings = v.findViewById(R.id.ibProfileUser_Settings);

    // set onClickListeners
        ibProfileUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        tvProfileUserFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // pass userID and what follow was clicked as arguments through the intent
                Intent intentStartFollow = new Intent(getActivity(), FollowActivity.class);
                intentStartFollow.putExtra("someUserID", theUserDB.getUserID());
                intentStartFollow.putExtra("followType", FollowActivity.ArtistlyFollow.Followers);
                startActivity(intentStartFollow);
            }
        });

        tvProfileUserFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // pass userID and what follow was clicked as arguments through the intent
                Intent intentStartFollow = new Intent(getActivity(), FollowActivity.class);
                intentStartFollow.putExtra("someUserID", theUserDB.getUserID());
                intentStartFollow.putExtra("followType", FollowActivity.ArtistlyFollow.Following);
                startActivity(intentStartFollow);
            }
        });

        tvProfileUserEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        return v;
    }

// load user data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        theUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvProfileUserUsername.setText(theUserDB.getUsername(snapshot));
                tvProfileUserName.setText(theUserDB.getName(snapshot));
                tvProfileUserTypeArtist.setText(theUserDB.getTypeArtist(snapshot));
                tvProfileUserBio.setText(theUserDB.getBio(snapshot));
                tvProfileUserFollowersNum.setText(String.valueOf(theUserDB.getFollowersCount(snapshot)));
                tvProfileUserFollowingNum.setText(String.valueOf(theUserDB.getFollowingCount(snapshot)));
                Glide.with(getActivity()).load(theUserDB.getProfilePhoto(snapshot)).into(civProfileUserPhoto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
