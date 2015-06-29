package me.groovie.groovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.groovie.groovieapp.R;
import me.groovie.groovieapp.models.MovieReview;
import me.groovie.groovieapp.models.Tweet;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetArrayAdapter(Context context, int resource, ArrayList<Tweet> objects){
        super(context,resource, objects);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
        }

        String username = tweet.username;
        String text = tweet.text;
        String rating = tweet.rating;
        String imageUrl = tweet.image_url;

        ImageView imageView = (ImageView) convertView.findViewById(R.id.tweet_icon_imageview);
        Picasso.with(getContext()).load(imageUrl).into(imageView);
//
        TextView nameTextView = (TextView) convertView.findViewById(R.id.tweet_name_textview);
        nameTextView.setText("@"+username);

        TextView tweetTextView = (TextView) convertView.findViewById(R.id.tweet_text_textview);
        tweetTextView.setText(text);
        TextView ratingTextView = (TextView) convertView.findViewById(R.id.tweet_rating_textview);
        ratingTextView.setText(rating+"%");



//
//        TextView ratingTextView= (TextView)convertView.findViewById(R.id.movie_rating_textview);
//        ratingTextView.setText("Tweet Rating: "+review.rating);

        return convertView;
    }
}