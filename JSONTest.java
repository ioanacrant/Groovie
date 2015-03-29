import org.json.JSONArray;
import org.json.JSONObject;

class JSONTest{
    public static void main (String [] args){
        String s = "{\"movies\": [{\"name\": \"Get Hard\", \"rating\": \"66.1\", \"image_url\": \"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}, {\"name\": \"The Imitation Game\", \"rating\": \"39.5\", \"image_url\": \"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}, {\"name\": \"Cinderella\", \"rating\": \"52.2\", \"image_url\": \"http://www.impawards.com/2015/posters/cinderella_ver4.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}, {\"name\": \"American Sniper\", \"rating\": \"40.1\", \"image_url\": \"http://www.impawards.com/2014/posters/american_sniper.jpg\", \"banner_url\": \"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg\"}], \"succes\": \"true\"}";
        JSONObject obj = new JSONObject(s);

    }
}