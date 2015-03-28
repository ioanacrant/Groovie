package me.groovie.groovieapp.models;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieReview {
    public String image_url,name,rating;

    public MovieReview(String name, String rating){
        this.name = name;
        this.rating = rating;
    }
}
