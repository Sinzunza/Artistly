package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inzucorp.artistly.R;

import Classes.mediaDB;
import Classes.visitingUserDB;

public class PostMediaFragment extends Fragment {

// declare layout ids
    TextView tvPostMediaUsername, tvPostMediaTypeArtist, tvPostMediaCaption;
    ImageView civPostMediaProfilePhoto, ivPostMediaPostPhoto;

// local variables
    mediaDB theMediaDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // initialize local variables
        if (getArguments() != null) {
            theMediaDB = new mediaDB(getArguments().getString("postID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_media, container, false);
    // initialize layout ids
        tvPostMediaUsername = v.findViewById(R.id.tvPostMedia_Username);
        tvPostMediaTypeArtist = v.findViewById(R.id.tvPostMedia_TypeArtist);
        tvPostMediaCaption = v.findViewById(R.id.tvPostMedia_Caption);
        civPostMediaProfilePhoto = v.findViewById(R.id.civPostMedia_ProfilePhoto);
        ivPostMediaPostPhoto = v.findViewById(R.id.ivPostMedia_Photo);

        return v;
    }

// load post data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        theMediaDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvPostMediaCaption.setText(theMediaDB.getCaption(snapshot));
                Glide.with(getActivity()).load(theMediaDB.getPhoto(snapshot)).into(ivPostMediaPostPhoto);
                final visitingUserDB someUserDB = new visitingUserDB(theMediaDB.getUser(snapshot));
                someUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tvPostMediaUsername.setText(someUserDB.getUsername(snapshot));
                        tvPostMediaTypeArtist.setText(someUserDB.getTypeArtist(snapshot));
                        Glide.with(getActivity()).load(someUserDB.getProfilePhoto(snapshot)).into(civPostMediaProfilePhoto);
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
}