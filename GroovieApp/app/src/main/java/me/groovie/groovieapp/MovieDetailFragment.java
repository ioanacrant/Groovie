package me.groovie.groovieapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.groovie.groovieapp.adapters.TweetArrayAdapter;
import me.groovie.groovieapp.models.Tweet;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieDetailFragment extends Fragment {
    public MovieDetailFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        //TextView nameTextView = (TextView) rootView.findViewById(R.id.movie_name_textview);

        String movieName = getArguments().getString("movie_name");
        String bannerUrl = getArguments().getString("banner_url");
        String rating = getArguments().getString("rating");

        ListView listView = (ListView) rootView.findViewById(R.id.tweets_list_view);
        View bannerView = inflater.inflate(R.layout.banner, listView, false);

        ImageView imageView = (ImageView) bannerView.findViewById(R.id.movie_banner_imageview);
        Picasso.with(getActivity()).load(bannerUrl).fit().centerCrop().into(imageView);

        TextView nameTextView = (TextView) bannerView.findViewById(R.id.movie_name_textview);
        nameTextView.setText(movieName);

        //android.support.v7.widget.CardView cardView = (android.support.v7.widget.CardView) bannerView.findViewById(R.id.banner_cardview);

        TextView ratingTextView = (TextView) bannerView.findViewById(R.id.movie_rating_textview);
        ratingTextView.setText(rating);

        listView.addHeaderView(bannerView);
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(new Tweet("abc","def"));
        tweets.add(new Tweet("abc","def"));
        tweets.add(new Tweet("abc","def"));
        tweets.add(new Tweet("abc","def"));
        tweets.add(new Tweet("abc","def"));
        TweetArrayAdapter mAdapter = new TweetArrayAdapter(getActivity(), R.layout.tweet_item, tweets);
        listView.setAdapter(mAdapter);
        //listView.addHeaderView(cardView);

//            listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.id.));

        return rootView;
    }
}