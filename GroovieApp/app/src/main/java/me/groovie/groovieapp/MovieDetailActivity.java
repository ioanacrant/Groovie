package me.groovie.groovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        String bannerUrl = intent.getStringExtra("banner_url");
        String rating= intent.getStringExtra("rating");
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movie_name", movieName);
            bundle.putString("banner_url", bannerUrl);
            bundle.putString("rating",rating);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


}
