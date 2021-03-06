package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.inzucorp.artistly.ExploreActivity;
import com.inzucorp.artistly.R;

public class ExploreFragment extends Fragment {

// declare layout ids
    FrameLayout flExploreFragment;
    TextView tvExploreSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        View ex =

    // initialize layout ids
        flExploreFragment = getActivity().findViewById(R.id.flExplore_Fragment);
        tvExploreSearch = v.findViewById(R.id.tvExplore_Search);

    // set onClickListeners
        tvExploreSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment frSearch = new SearchExploreFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(flExploreFragment.getId(), frSearch).addToBackStack(null).commit();
            }
        });

        return v;
    }
}