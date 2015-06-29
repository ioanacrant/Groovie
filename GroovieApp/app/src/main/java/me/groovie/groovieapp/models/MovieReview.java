package me.groovie.groovieapp.models;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class MovieReview {
    public String image_url,banner_url,name,rating;

    public MovieReview(String name, String rating, String image_url, String banner_url){
        this.name = name;
        this.rating = rating;
        this.image_url = image_url;
        this.banner_url = banner_url;
    }
}
