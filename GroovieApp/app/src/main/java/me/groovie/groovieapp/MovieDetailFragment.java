package me.groovie.groovieapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import me.groovie.groovieapp.adapters.MovieArrayAdapter;
import me.groovie.groovieapp.adapters.TweetArrayAdapter;
import me.groovie.groovieapp.models.MovieReview;
import me.groovie.groovieapp.models.Tweet;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieDetailFragment extends Fragment {
    TweetArrayAdapter tweetArrayAdapter;
    ListView listView;
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

        listView = (ListView) rootView.findViewById(R.id.tweets_list_view);
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
        tweetArrayAdapter = new TweetArrayAdapter(getActivity(), R.layout.tweet_item, tweets);
        listView.setAdapter(tweetArrayAdapter);
        //listView.addHeaderView(cardView);

//            listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.id.));

        return rootView;
    }
    public class AsyncMovieLoader extends AsyncTask<ArrayList<MovieReview>, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            movieArrayAdapter.setMovieReviews(result);
            tweetArrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(ArrayList<MovieReview>... params) {

            final ArrayList<Tweet> result = new ArrayList<Tweet>();

            try {
                String resp;
                URL url = new URL("http://young-fjord-8790.herokuapp.com/movies");

                // Create the request to OpenWeatherMap, and open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }



                resp = buffer.toString();
//                resp = "{\"movies\": [{\"name\": \"Get Hard\", \"rating\": \"66.1\", \"image_url\": \"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"The Imitation Game\", \"rating\": \"39.5\", \"image_url\": \"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"Cinderella\", \"rating\": \"52.2\", \"image_url\": \"http://www.impawards.com/2015/posters/cinderella_ver4.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}]}";
                Log.v("JSON: ", resp);
                JSONObject obj = new JSONObject(resp);
                JSONArray arr = obj.getJSONArray("movies");
                //Log.v("I MADE IT","HI");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject movieobj = arr.getJSONObject(i);
                    String name = movieobj.getString("name");
                    String rating = movieobj.getString("rating");
                    String image_url = movieobj.getString("image_url");
                    String banner_url = movieobj.getString("banner_url");
                    Log.v("ADDING", name + " "+rating);
                    result.add(new Tweet(name,rating,image_url,banner_url));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tweetArrayAdapter.notifyDataSetChanged();
                        tweetArrayAdapter = new TweetArrayAdapter(getActivity(),R.layout.tweet_item,result);
                        tweetArrayAdapter.notifyDataSetChanged();
                        listView.setAdapter(tweetArrayAdapter);
                    }
                });

                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
            //- See more at: http://www.survivingwithandroid.com/2013/01/android-async-listview-jee-and-restful.html#sthash.LqWgqCI8.dpuf
        }
    }
}