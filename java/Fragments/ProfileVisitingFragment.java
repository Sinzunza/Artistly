package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.inzucorp.artistly.FollowActivity;
import com.inzucorp.artistly.FollowActivity.ArtistlyFollow;
import com.inzucorp.artistly.R;

import Classes.userDB;
import Classes.visitingUserDB;

public class ProfileVisitingFragment extends Fragment {

// declare layout ids
    TextView tvProfileVisitingUsername, tvProfileVisitingName, tvProfileVisitingTypeArtist, tvProfileVisitingFollowingNum,
            tvProfileVisitingFollowersNum, tvProfileVisitingFollowing, tvProfileVisitingFollowers, tvProfileVisitingBio, tvProfileVisitingFollow,
            tvProfileVisitingMessage;
    ImageView civProfileVisitingPhoto, ivProfileVisitingFollow;
    ImageButton ibProfileVisitingSettings;
    ConstraintLayout clProfileVisitingFollow;

// local variables
    visitingUserDB theVisitingUserDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        // initialize local variables
            theVisitingUserDB = new visitingUserDB(getArguments().getString("visitingUserID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_visiting, container, false);
    // initialize layout ids
        tvProfileVisitingUsername = v.findViewById(R.id.tvProfileVisiting_Username);
        tvProfileVisitingName = v.findViewById(R.id.tvProfileVisiting_Name);
        tvProfileVisitingTypeArtist = v.findViewById(R.id.tvProfileVisiting_TypeArtist);
        tvProfileVisitingFollowingNum = v.findViewById(R.id.tvProfileVisiting_FollowingNum);
        tvProfileVisitingFollowersNum = v.findViewById(R.id.tvProfileVisiting_FollowersNum);
        tvProfileVisitingFollowing = v.findViewById(R.id.tvProfileVisiting_Following);
        tvProfileVisitingFollowers = v.findViewById(R.id.tvProfileVisiting_Followers);
        tvProfileVisitingBio = v.findViewById(R.id.tvProfileVisiting_Bio);
        tvProfileVisitingFollow = v.findViewById(R.id.tvProfileVisiting_follow);
        tvProfileVisitingMessage = v.findViewById(R.id.tvProfileVisiting_Message);
        civProfileVisitingPhoto = v.findViewById(R.id.civProfileVisiting_Photo);
        ivProfileVisitingFollow = v.findViewById(R.id.ivProfileVisiting_Follow);
        ibProfileVisitingSettings = v.findViewById(R.id.ibProfileVisiting_Settings);
        clProfileVisitingFollow = v.findViewById(R.id.clProfileVisiting_Follow);

    //set onClickListeners
        ibProfileVisitingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        tvProfileVisitingFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // pass visitingUserID and what follow was clicked as arguments through the intent
                Intent intentStartFollow = new Intent(getActivity(), FollowActivity.class);
                intentStartFollow.putExtra("someUserID", theVisitingUserDB.getUserID());
                intentStartFollow.putExtra("followType", ArtistlyFollow.Followers);
                startActivity(intentStartFollow);
            }
        });

        tvProfileVisitingFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // pass visitingUserID and what follow was clicked as arguments through the intent
                Intent intentStartFollow = new Intent(getActivity(), FollowActivity.class);
                intentStartFollow.putExtra("someUserID", theVisitingUserDB.getUserID());
                intentStartFollow.putExtra("followType", ArtistlyFollow.Following);
                startActivity(intentStartFollow);
            }
        });

        clProfileVisitingFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // update database when user follows / unfollows
                final userDB theUserDB = new userDB();
                theUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshotUser) {
                        theVisitingUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotVisiting) {
                                // if following then remove from following and change following to follow and increment followers number
                                if (snapshotUser.child("following").hasChild(theVisitingUserDB.getUserID())) {
                                    theUserDB.removeFollowing(theVisitingUserDB);
                                    tvProfileVisitingFollow.setText("Follow");
                                    ivProfileVisitingFollow.setImageDrawable(getActivity().getDrawable(R.drawable.ic_addfriend));
                                    tvProfileVisitingFollowersNum.setText(String.valueOf(Integer.parseInt(tvProfileVisitingFollowersNum.getText().toString())-1));
                                }
                                // if not following then add to following and change follow to following and decrement followers number
                                else {
                                    theUserDB.addFollowing(snapshotUser, theVisitingUserDB, theVisitingUserDB.getUsernameLowerCase(snapshotVisiting));
                                    tvProfileVisitingFollow.setText("Following");
                                    ivProfileVisitingFollow.setImageDrawable(getActivity().getDrawable(R.drawable.ic_checkmark));
                                    tvProfileVisitingFollowersNum.setText(String.valueOf(Integer.parseInt(tvProfileVisitingFollowersNum.getText().toString())+1));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        return v;
    }

// load user data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        theVisitingUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            // setting visitingUser's info in the activity
                tvProfileVisitingUsername.setText(theVisitingUserDB.getUsername(snapshot));
                tvProfileVisitingName.setText(theVisitingUserDB.getName(snapshot));
                tvProfileVisitingTypeArtist.setText(theVisitingUserDB.getTypeArtist(snapshot));
                tvProfileVisitingFollowersNum.setText(String.valueOf(theVisitingUserDB.getFollowersCount(snapshot)));
                tvProfileVisitingFollowingNum.setText(String.valueOf(theVisitingUserDB.getFollowingCount(snapshot)));
                tvProfileVisitingBio.setText(theVisitingUserDB.getBio(snapshot));
                Glide.with(getActivity()).load(theVisitingUserDB.getProfilePhoto(snapshot)).into(civProfileVisitingPhoto);
                final userDB userDB = new userDB();
                if (snapshot.child("followers").hasChild(userDB.getUserID())) {
                    tvProfileVisitingFollow.setText("Following");
                    ivProfileVisitingFollow.setImageDrawable(getActivity().getDrawable(R.drawable.ic_checkmark));
                } else {
                    tvProfileVisitingFollow.setText("Follow");
                    ivProfileVisitingFollow.setImageDrawable(getActivity().getDrawable(R.drawable.ic_addfriend));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}