package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inzucorp.artistly.R;
import com.inzucorp.artistly.SettingsActivity;

import Classes.mediaDB;
import Classes.visitingUserDB;

public class ChatFragment extends Fragment {

// declare layout ids
    TextView tvChatUsername;
    EditText etChatTextMessage;
    ImageButton ibChatSend;

// local variables
    visitingUserDB messagingUserDB;
    String textMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messagingUserDB = new visitingUserDB(getArguments().getString("messagingUserID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

    // initialize layout ids
        tvChatUsername = v.findViewById(R.id.tvChat_Username);
        etChatTextMessage = v.findViewById(R.id.etChat_TextMessage);
        ibChatSend = v.findViewById(R.id.ibChat_Send);

    // set onClickListeners
        ibChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessage = etChatTextMessage.getText().toString();
                // if no chat exists then createChat

                // if chat exists the newMessage
            }
        });

        return v;
    }

// load user data only when activity is opened
    @Override
    public void onResume() {
        super.onResume();
        messagingUserDB.getDBRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            // setting visitingUser's info in the activity
                tvChatUsername.setText(messagingUserDB.getUsername(snapshot));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}