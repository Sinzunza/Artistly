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
;
import Classes.serviceDB;
import Classes.otherUserDB;

public class PostServiceFragment extends Fragment {

// declare layout ids
    TextView tvPostServiceUsername, tvPostServiceTypeArtist, tvPostServiceTitle, tvPostServiceDescription, tvPostServiceFee;
    ImageView civPostServiceProfilePhoto, ivPostServicePostPhoto;

// local variables
    serviceDB theServiceDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // initialize local variables
        if (getArguments() != null) {
            theServiceDB = new serviceDB(getArguments().getString("postID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_service, container, false);
    // initialize layout ids
        tvPostServiceUsername = v.findViewById(R.id.tvPostService_Username);
        tvPostServiceTypeArtist = v.findViewById(R.id.tvPostService_TypeArtist);
        tvPostServiceTitle = v.findViewById(R.id.tvPostService_Title);
        tvPostServiceDescription = v.findViewById(R.id.tvPostService_Description);
        tvPostServiceFee = v.findViewById(R.id.tvPostService_Fee);
        civPostServiceProfilePhoto = v.findViewById(R.id.civPostService_ProfilePhoto);
        ivPostServicePostPhoto = v.findViewById(R.id.ivPostService_Photo);

        return v;
    }

// load post data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        theServiceDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvPostServiceTitle.setText(theServiceDB.getTitle(snapshot));
                tvPostServiceDescription.setText(theServiceDB.getDescription(snapshot));
                tvPostServiceFee.setText(theServiceDB.getFee(snapshot));
                Glide.with(getActivity()).load(theServiceDB.getPhoto(snapshot)).into(ivPostServicePostPhoto);
                final otherUserDB someUserDB = new otherUserDB(theServiceDB.getUser(snapshot));
                someUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tvPostServiceUsername.setText(someUserDB.getUsername(snapshot));
                        tvPostServiceTypeArtist.setText(someUserDB.getTypeArtist(snapshot));
                        Glide.with(getActivity()).load(someUserDB.getProfilePhoto(snapshot)).into(civPostServiceProfilePhoto);
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