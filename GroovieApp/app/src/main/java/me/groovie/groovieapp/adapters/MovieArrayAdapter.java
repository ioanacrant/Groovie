package me.groovie.groovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.groovie.groovieapp.R;
import me.groovie.groovieapp.models.MovieReview;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieArrayAdapter extends ArrayAdapter<MovieReview> {
    ArrayList<MovieReview> movieReviews;
    public MovieArrayAdapter(Context context, int resource, ArrayList<MovieReview> objects){
        super(context,resource, objects);
        movieReviews = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieReview review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }
        TextView nameTextView = (TextView) convertView.findViewById(R.id.movie_name_textview);
        nameTextView.setText(review.name);

        TextView ratingTextView= (TextView)convertView.findViewById(R.id.movie_rating_textview);
        ratingTextView.setText("Tweet Rating: "+review.rating);

        return convertView;
    }
}
