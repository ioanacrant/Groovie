package me.groovie.groovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import me.groovie.groovieapp.adapters.MovieArrayAdapter;
import me.groovie.groovieapp.models.MovieReview;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MoviesFragment extends Fragment {
    RecyclerView mRecyclerView;
    GridView mGridView;
    LinearLayoutManager mLayoutManager;
    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_gridview);

        //mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
       // mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<MovieReview> myDataset = new ArrayList<MovieReview>();
        myDataset.add(new MovieReview("Cinderella","93","http://www.impawards.com/2015/posters/cinderella_ver4.jpg"));
        myDataset.add(new MovieReview("Get Hard","55","https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg"));
        myDataset.add(new MovieReview("The Imitation Game","80","http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg"));
        myDataset.add(new MovieReview("Focus","74","http://www.impawards.com/2015/posters/focus_ver2.jpg"));
        myDataset.add(new MovieReview("American Sniper", "80", "http://www.impawards.com/2014/posters/american_sniper.jpg"));


        // specify an adapter (see also next example)
//        mAdapter = new MovieAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);
        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.movie_item, myDataset);
        mGridView.setAdapter(movieArrayAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = ((TextView) view.findViewById(R.id.movie_name_textview)).getText().toString();
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie_name", movie);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
