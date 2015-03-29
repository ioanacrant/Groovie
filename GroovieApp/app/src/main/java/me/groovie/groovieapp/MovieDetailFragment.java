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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
        ratingTextView.setText("Twitter Rating: "+ rating+"%");

        listView.addHeaderView(bannerView);
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
//        tweets.add(new Tweet("abc","def"));
//        tweets.add(new Tweet("abc","def"));
//        tweets.add(new Tweet("abc","def"));
//        tweets.add(new Tweet("abc","def"));
//        tweets.add(new Tweet("abc","def"));
        tweetArrayAdapter = new TweetArrayAdapter(getActivity(), R.layout.tweet_item, tweets);
        listView.setAdapter(tweetArrayAdapter);
        new AsyncTweetLoader().execute(movieName);
        //listView.addHeaderView(cardView);

//            listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.id.));

        return rootView;
    }
    public class AsyncTweetLoader extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            movieArrayAdapter.setMovieReviews(result);
            tweetArrayAdapter.notifyDataSetChanged();
        }
        public String convertStandardJSONString(String data_json){
            data_json = data_json.replace("\\", "");
            data_json = data_json.replace("\"{", "{");
            data_json = data_json.replace("}\",", "},");
            data_json = data_json.replace("}\"", "}");
            return data_json;
        }
        @Override
        protected Void doInBackground(String... params) {

            final ArrayList<Tweet> result = new ArrayList<Tweet>();

            try {
                String resp;
                String u = "http://young-fjord-8790.herokuapp.com/movies/"+params[0];
                u = u.replaceAll(" ","%20");

                URL url = new URL(u);
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
                //resp ="{\"tweets\": [{\"text\": \"Crazy stuff going on in the world. I turned the news on for 30 seconds and realized we gonna need Batman for real.\", \"rating\": \"30.7\", \"username\": \"King Bach\", \"imageurl\": \"https://pbs.twimg.com/profile_images/555161397068189696/yCajAqdj_normal.jpeg\"}, {\"text\": \"Batman's reading corner. http://t.co/mJP7i63epA\", \"rating\": \"77.2\", \"username\": \"A.J.\", \"imageurl\": \"https://pbs.twimg.com/profile_images/532179269581959169/OYuukW8r_normal.jpeg\"}, {\"text\": \"Hey art collectors--check out this auction of a Batman &amp; Robin illustration I did #charity https://t.co/dBS9uiVgYX http://t.co/4jICbj7sim\", \"rating\": \"84.3\", \"username\": \"Jim Lee\", \"imageurl\": \"https://pbs.twimg.com/profile_images/551195471658500096/Qh6MAunH_normal.jpeg\"}, {\"text\": \"Holy 1980's batman. #SNLVintage\", \"rating\": \"68.8\", \"username\": \"Erin\", \"imageurl\": \"https://pbs.twimg.com/profile_images/530914330733060096/M45B9rl4_normal.jpeg\"}, {\"text\": \"RT @YouTube: How to be Batman. http://t.co/hwKJNpiAz8 http://t.co/jem2l2Vsw0\", \"rating\": \"70.0\", \"username\": \"MyNameIsTom\", \"imageurl\": \"https://pbs.twimg.com/profile_images/579403177394638849/3B2YHPQ7_normal.jpg\"}, {\"text\": \"NEW 52: FUTURES END 37,38,39,40,41,42 (2015) KEITH GIFFEN, JEFF LEMIRE http://t.co/PuafAuIKHz #batman\", \"rating\": \"64.8\", \"username\": \"Batman Memorabilia\", \"imageurl\": \"https://pbs.twimg.com/profile_images/378800000653846982/13023bb54129b3411d3bab234639c86b_normal.jpeg\"}]}";
//                resp = "{\"movies\": [{\"name\": \"Get Hard\", \"rating\": \"66.1\", \"image_url\": \"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"The Imitation Game\", \"rating\": \"39.5\", \"image_url\": \"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"Cinderella\", \"rating\": \"52.2\", \"image_url\": \"http://www.impawards.com/2015/posters/cinderella_ver4.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}]}";
                Log.v("TWEET JSON: ", convertStandardJSONString(resp.trim().substring(1,resp.length()-2)));
                resp =  convertStandardJSONString(resp.trim().substring(1,resp.length()-2));
                JSONObject obj = new JSONObject(resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1));
                JSONArray arr = obj.getJSONArray("tweets");
                //Log.v("I MADE IT","HI");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject movieobj = arr.getJSONObject(i);
                    String text = movieobj.getString("text");
                    String rating = movieobj.getString("rating");
                    String username = movieobj.getString("username");
                    String image_url = movieobj.getString("image_url");
                    //Log.v("ADDING", name + " "+rating);
                    result.add(new Tweet(username,text,rating,image_url));
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