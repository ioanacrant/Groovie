package me.groovie.groovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Movie;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    MovieArrayAdapter movieArrayAdapter;
    ProgressBar progressBar;
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
//        myDataset.add(new MovieReview("Cinderella","93","http://www.impawards.com/2015/posters/cinderella_ver4.jpg",
//                "http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"));
//        myDataset.add(new MovieReview("Get Hard","55","https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg",
//                "http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"));
//        myDataset.add(new MovieReview("The Imitation Game","80","http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg",
//                "http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"));
//        myDataset.add(new MovieReview("Focus","74","http://www.impawards.com/2015/posters/focus_ver2.jpg",
//                "http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"));
//        myDataset.add(new MovieReview("American Sniper", "80", "http://www.impawards.com/2014/posters/american_sniper.jpg",
//                "http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"));


        // specify an adapter (see also next example)
//        mAdapter = new MovieAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.movie_item, myDataset);
        mGridView.setAdapter(movieArrayAdapter);
        new AsyncMovieLoader().execute(myDataset);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = ((TextView) view.findViewById(R.id.movie_name_textview)).getText().toString();
                MovieReview review = (MovieReview) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie_name", movie);
                intent.putExtra("banner_url", review.banner_url);
                intent.putExtra("rating", review.rating);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    public class AsyncMovieLoader extends AsyncTask<ArrayList<MovieReview>, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            movieArrayAdapter.setMovieReviews(result);
            movieArrayAdapter.notifyDataSetChanged();

        }
        public String convertStandardJSONString(String data_json){
            data_json = data_json.replace("\\", "");
            data_json = data_json.replace("\"{", "{");
            data_json = data_json.replace("}\",", "},");
            data_json = data_json.replace("}\"", "}");
            return data_json;
        }
        @Override
        protected Void doInBackground(ArrayList<MovieReview>... params) {

            final ArrayList<MovieReview> result = new ArrayList<MovieReview>();

            try {
                // String resp;
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

                String resp = buffer.toString();

                //System.out.println(resp);
//                resp = "{\"movies\": [{\"name\": \"Get Hard\", \"rating\": \"66.1\", \"image_url\": \"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"The Imitation Game\", \"rating\": \"39.5\", \"image_url\": \"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"},";
//                resp +="{\"name\": \"Cinderella\", \"rating\": \"52.2\", \"image_url\": \"http://www.impawards.com/2015/posters/cinderella_ver4.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}]}";
//                Log.v("JSON: ",resp);
              //  String resp ="{\"success\": \"true\", \"movies\": [{\"image_url\": \"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg\", \"rating\": \"61.7\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\", \"name\": \"Get Hard\"}, {\"image_url\": \"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg\", \"rating\": \"45.9\", \"banner_url\": \"http://blog.bettercrypto.com/wp-content/uploads/the-imitation-game-banner.jpg\", \"name\": \"The Imitation Game\"}, {\"image_url\": \"http://www.impawards.com/2015/posters/cinderella_ver4.jpg\", \"rating\": \"50.4\", \"banner_url\": \"http://www.flickeringmyth.com/wp-content/uploads/2015/01/Cinderella-2015.jpg\", \"name\": \"Cinderella\"}, {\"image_url\": \"http://www.impawards.com/2014/posters/american_sniper.jpg\", \"rating\": \"65.5\", \"banner_url\": \"http://www.davestrailerpage.co.uk/images/americansniper800.jpg\", \"name\": \"American Sniper\"}, {\"image_url\": \"http://assets.nydailynews.com/polopoly_fs/1.1591196!/img/httpImage/image.jpg_gen/derivatives/article_970/grey26f-1-web.jpg\", \"rating\": \"65.9\", \"banner_url\": \"http://www.flickeringmyth.com/wp-content/uploads/2015/02/fifty-shades-of-grey-banner.jpg\", \"name\": \"Fifty Shades of Grey\"}, {\"image_url\": \"http://www.hollywoodreporter.com/sites/default/files/custom/Blog_Images/interstellar2.jpg\", \"rating\": \"59.7\", \"banner_url\": \"http://www.sasapost.com/wp-content/uploads/98caac85-f5ed-419a-8a2e-672a10473ea3.jpeg\", \"name\": \"Interstellar\"}, {\"image_url\": \"http://fwooshflix.thefwoosh.com/files/2015/02/Kingsman-The-Secret-Service-poster.jpg\", \"rating\": \"100.0\", \"banner_url\": \"https://vincentloy.files.wordpress.com/2015/03/kingsman-the-secret-service-banner.jpg\", \"name\": \"Kingsman: The Secret Service\"}, {\"image_url\": \"http://upload.wikimedia.org/wikipedia/en/a/af/Insurgent_poster.jpg\", \"rating\": \"100.0\", \"banner_url\": \"http://redcarpetrefs.com/wp-content/uploads/2015/03/insurgent-banner.png\", \"name\": \"The Divergent Series: Insurgent\"}]}";
                resp = convertStandardJSONString(resp.trim());
                //resp =  convertStandardJSONString(resp.trim().substring(1,resp.length()-2));
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
                    result.add(new MovieReview(name,rating,image_url,banner_url));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieArrayAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(ProgressBar.GONE);
                        movieArrayAdapter = new MovieArrayAdapter(getActivity(),R.layout.movie_item,result);
                        movieArrayAdapter.notifyDataSetChanged();
                        mGridView.setAdapter(movieArrayAdapter);
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
