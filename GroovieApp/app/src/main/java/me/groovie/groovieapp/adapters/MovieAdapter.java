//package me.groovie.groovieapp.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import me.groovie.groovieapp.R;
//import me.groovie.groovieapp.models.MovieReview;
//
///**
// * Created by yuweixu on 2015-03-28.
// */
//public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
//    private ArrayList<MovieReview> movieReviews;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public TextView txtHeader;
//        public TextView txtFooter;
//
//
//        public ViewHolder(View v) {
//            super(v);
//            txtHeader = (TextView) v.findViewById(R.id.firstLine);
//            txtFooter = (TextView) v.findViewById(R.id.secondLine);
//        }
//    }
//
//    public void add(int position, MovieReview item) {
//        movieReviews.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(MovieReview item){
//        int position = movieReviews.indexOf(item);
//        movieReviews.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public MovieAdapter(ArrayList<MovieReview> myDataset) {
//        movieReviews = myDataset;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                   int viewType) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        final MovieReview review = movieReviews.get(position);
//        holder.txtHeader.setText(review.name);
//        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                remove(review);
//            }
//        });
//
//        //holder.txtFooter.setText("Footer: " + movieReviews.get(position));
//
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return movieReviews.size();
//    }
//
//}