package me.groovie.groovieapp.models;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class Tweet {
    public String username, text;
    public String image_url, rating;
    public Tweet(String username, String message){
        this.username = username;
        text = message;
    }
    public Tweet(String username, String message,String rating, String image_url){
        this.username = username;
        text = message;
        this.rating = rating;
        this.image_url = image_url;
    }
}
