package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.inzucorp.artistly.PostActivity;

import com.inzucorp.artistly.R;

import Classes.mediaDB;
import Models.postsAdapter;
import Classes.artistlyDB;

public class ExploreFragment extends Fragment {

// declare layout ids
    FrameLayout flExploreFragment;
    TextView tvExploreSearch;
    RecyclerView rvExplorePosts;

// local variables
    boolean isMedia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        flExploreFragment = getActivity().findViewById(R.id.flExplore_Fragment);
        tvExploreSearch = v.findViewById(R.id.tvExplore_Search);
        rvExplorePosts = v.findViewById(R.id.rvExplore_Posts);

    // initialize local variables
        isMedia = true;

    // set recycler adapter layout
        rvExplorePosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        profilePostsAdapter();

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


// move bar and change post type based on typeAdapter
    private void profilePostsAdapter() {
        // set which post tab to underline and set the query

       Query postQuery = artistlyDB.orderedByQuery("Media", "user");

        // implement FirebaseRecycler on recycler adapter
        FirebaseRecyclerOptions<postsAdapter> options = new FirebaseRecyclerOptions.Builder<postsAdapter>().setQuery(postQuery, postsAdapter.class).build();
        final FirebaseRecyclerAdapter<postsAdapter, ExploreFragment.postsAdapterViewHolder> adapter = new FirebaseRecyclerAdapter<postsAdapter, ExploreFragment.postsAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ExploreFragment.postsAdapterViewHolder holder, final int position, @NonNull final postsAdapter model) {
                holder.setInfo(getActivity(), model.getPhoto());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String postID = getRef(position).getKey();
                        Intent intent = new Intent(getActivity(), PostActivity.class);
                        intent.putExtra("postID", postID);
                        intent.putExtra("postType", isMedia);
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public ExploreFragment.postsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_posts, parent, false);
                return new ExploreFragment.postsAdapterViewHolder(v);
            }
        };
        adapter.startListening();
        rvExplorePosts.setAdapter(adapter);
    }

// view Holder Class need for recycler adapter
    public static class postsAdapterViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public postsAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setInfo(Context ctx, String photo){
            final Context ctxF = ctx;
            final ImageView ivPostsAdImage = mView.findViewById(R.id.ivPostsAd_Image);
            Glide.with(ctxF).load(photo).into(ivPostsAdImage);
        }
    }
}