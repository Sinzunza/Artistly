package com.inzucorp.artistly;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Vector;

import Classes.userDB;

public class TypeArtistActivity extends AppCompatActivity {

// declare layout ids
    TextView tvTypeArtistActing, tvTypeArtistArt, tvTypeArtistDancer, tvTypeArtistMusician, tvTypeArtistPhotographer,
             tvTypeArtistVideographer, tvTypeArtistNone, tvTypeArtistDone;

// local variables
    int currentSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_artist);

    // initialize layout ids
        tvTypeArtistActing = findViewById(R.id.tvTypeArtist_Acting);
        tvTypeArtistArt = findViewById(R.id.tvTypeArtist_Art);
        tvTypeArtistDancer = findViewById(R.id.tvTypeArtist_Dancer);
        tvTypeArtistMusician = findViewById(R.id.tvTypeArtist_Musician);
        tvTypeArtistPhotographer = findViewById(R.id.tvTypeArtist_Photographer);
        tvTypeArtistVideographer = findViewById(R.id.tvTypeArtist_Videographer);
        tvTypeArtistNone = findViewById(R.id.tvTypeArtist_None);
        tvTypeArtistDone = findViewById(R.id.tvTypeArtist_Done);

    // initialize to vectors to help with user's selection of type
        final Vector<TextView> typeSelections = new Vector<TextView>(Arrays.asList(tvTypeArtistActing, tvTypeArtistArt, tvTypeArtistDancer,
                                        tvTypeArtistMusician, tvTypeArtistPhotographer, tvTypeArtistVideographer, tvTypeArtistNone));

        final Vector<String> typeStrings = new Vector<String>(Arrays.asList("Acting", "Art", "Dancer", "Musician", "Photographer", "Videographer", "None"));

        currentSelection = typeSelections.size()-1; // initiate currentSelection to none

    // for loop that sets onClickListeners to all Type text views
        for(int i = 0; i < typeSelections.size(); i++) {
            final int tempI = i;
            typeSelections.elementAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // if clicked type that is currently selected, unselect it and set currentSelection to none
                    if (tempI == currentSelection && currentSelection != typeSelections.size()-1) {
                        typeSelections.elementAt(currentSelection).setTypeface(null, Typeface.NORMAL);
                        currentSelection = typeSelections.size()-1;
                        typeSelections.elementAt(currentSelection).setTypeface(null, Typeface.BOLD_ITALIC);

                    }
                //if clicked type that isn't currently selected, set currentSelection to that type
                    else {
                        typeSelections.elementAt(currentSelection).setTypeface(null, Typeface.NORMAL);
                        currentSelection = tempI;
                        typeSelections.elementAt(currentSelection).setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                }
            });
        }

    // set onClickListeners
        tvTypeArtistDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final userDB theUserDB = new userDB();
                theUserDB.addVal("typeArtist", typeStrings.elementAt(currentSelection));
                startActivity(new Intent(TypeArtistActivity.this, ExploreActivity.class));
            }
        });
    }
}
