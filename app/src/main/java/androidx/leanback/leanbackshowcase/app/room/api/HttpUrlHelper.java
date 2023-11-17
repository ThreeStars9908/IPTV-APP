package androidx.leanback.leanbackshowcase.app.room.api;

public class HttpUrlHelper {
    public static String site_url = "192.168.2.26";
    public static String site_port = "5000";

    public static String api_movies = "/api/category/movies";
    public static String getImageUrl(String image_url){
        String str_url = "";
        str_url += "http://" + site_url + ":" + site_port + image_url;
        return str_url;
    }
    public static String getApiUrl(String api_url){
        String str_url = "";
        str_url += "http://" + site_url + ":" + site_port + api_url;
        return str_url;
    }
}
