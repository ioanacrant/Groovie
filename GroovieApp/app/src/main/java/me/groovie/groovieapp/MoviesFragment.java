package me.groovie.groovieapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
        myDataset.add(new MovieReview("Batman","3"));
        myDataset.add(new MovieReview("IronMan","5"));
        myDataset.add(new MovieReview("Interstellar","8"));

        // specify an adapter (see also next example)
//        mAdapter = new MovieAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);
        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.movie_item, myDataset);
        mGridView.setAdapter(movieArrayAdapter);
        return rootView;
    }
}
