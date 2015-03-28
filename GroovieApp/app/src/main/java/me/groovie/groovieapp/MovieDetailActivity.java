package me.groovie.groovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieDetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String movieName = intent.getStringExtra("movie_name");

        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movie_name", movieName);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public static class MovieDetailFragment extends Fragment {
        public MovieDetailFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            TextView nameTextView = (TextView) rootView.findViewById(R.id.movie_name_textview);

            String movieName = getArguments().getString("movie_name");
            nameTextView.setText(movieName);

            ListView listView = (ListView) rootView.findViewById(R.id.tweets_list_view);
//            listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.id.));

            return rootView;
        }
    }
}
