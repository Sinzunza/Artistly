package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.inzucorp.artistly.R;

public class MessagesFragment extends Fragment {

// declare layout ids
    FrameLayout flMessagingFragment;
    ImageButton ibMessagesNewMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragmentS
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

    // initialize layout ids
        flMessagingFragment = getActivity().findViewById(R.id.flMessaging_Fragment);
        ibMessagesNewMessage = v.findViewById(R.id.ibMessages_NewMessage);

    // set onClickListeners
        ibMessagesNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment frSearchMessages = new SearchMessagesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(flMessagingFragment.getId(), frSearchMessages).addToBackStack(null).commit();
            }
        });

        return v;
    }
}