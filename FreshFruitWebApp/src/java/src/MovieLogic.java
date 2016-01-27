package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains static business logic functions for movie searching.
 */
public class MovieLogic {
    
    /** The limit of listings. */
    public static final int LIMIT = 50;

    /** The host URL of the database. */
    public static final String HOST = "jdbc:derby://localhost:1527/fruit";

    /** The username for the database. */
    public static final String UNAME = "team11";

    /** The password for the database. */
    public static final String UPASS= "fruit";

    /** The error status of program. */
    public static final int HTTPOK = 200;

    /**
     * Finds movies based on a search query.
     * @param search the search query
     * @return an array of movies matching the search query
     */
    public static Movie[] searchMovies(String search) {
        String link = "http://api.rottentomatoes.com/api/public/v1.0/movies."
                + "json?apikey=yedukp76ffytfuy24zsqk7f5&q="
                    + search.replaceAll("\\s", "+") + "&page_limit=" + LIMIT;
        return findMovies(link);
    }
    /**
     * Finds the latest DVD movie releases.
     * @return an array of new DVD releases
     */
    public static Movie[] getNewDvd() {
        String link = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds"
                + "/new_releases.json?page_limit=" + LIMIT + "&page=1&country"
                + "=us&apikey=yedukp76ffytfuy24zsqk7f5";
        return findMovies(link);
    }
    /**
     * Finds recommended movies based on major.
     * @param major the major to search for recommended movies from
     * @return an array of recommended movies
     */
    public static Movie[] recommendMovies(Major major) {
        ArrayList ids = new ArrayList();
        ArrayList totals = new ArrayList();
        ArrayList reviewCount = new ArrayList();
        try {
            ResultSet rs = UserManager.querySQL("SELECT MOVIEID,STARRATING "
                    + "FROM REVIEWS WHERE REVIEWMAJOR=\'" + major + "\'");
            while (rs.next()) {
                int testID = Integer.parseInt(rs.getString("MOVIEID"));
                if (ids.contains(testID)) {
                    totals.set(ids.indexOf(testID),
                            Integer.parseInt(rs.getString("STARRATING"))
                                    + (int) totals.get(ids.indexOf(testID)));
                    reviewCount.set(ids.indexOf(testID), 1 + (int)
                            reviewCount.get(ids.indexOf(testID)));
                } else {
                    ids.add(testID);
                    totals.add(Integer.parseInt(rs.getString("STARRATING")));
                    reviewCount.add(1);
                }
            }
            int[][] averages = new int[ids.size()][2];
            for (int i = 0; i < ids.size(); i++) {
                averages[i][0] = (int) totals.get(i) / (int) reviewCount.get(i);
                averages[i][1] = (int) ids.get(i);
            }
            java.util.Arrays.sort(averages, new java.util.Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                return Double.compare(b[0], a[0]);
                }
            });
            Movie[] movies = new Movie[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                Movie movie = getMovieById(averages[i][1]);
                if (movie != null) {
                    movies[i] = movie;
                }
            }
            return movies;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }
    /**
     * Takes an id number and returns the movie object.
     * @param id the movie's id
     * @return the movie corresponding to the number
     */
    public static Movie getMovieById(int id) {
        String link = "http://api.rottentomatoes.com/api/public/v1.0/movie"
                + "s/" + id + ".json?apikey=yedukp76ffytfuy24zsqk7f5";
        String callResult = getJsonData(link);
        Gson googleJson = new Gson();
        JsonObject movieJson = googleJson.fromJson(callResult,
                    JsonObject.class);
        String title = movieJson.get("title").getAsString();
        String thumbnail = ((JsonObject) movieJson.get("posters")).get("thu"
                + "mbnail").getAsString();
        String synopsis = "None", release = "Unknown";
        if (movieJson.get("synopsis") != null) {
            synopsis = movieJson.get("synopsis").getAsString();
        }
        JsonElement releaseJson = ((JsonObject) movieJson.get("release_"
                + "dates")).get("theater");
        if (releaseJson != null) {
            release = releaseJson.getAsString();
        }
        return new Movie(title, thumbnail, release, synopsis, id);
    }
    /**
     * Finds the newest movie releases.
     * @return an array of new movies
     */
    public static Movie[] getNewMovies() {
        String link = "http://api.rottentomatoes.com/api/public/v1.0/lists/"
                + "movies/in_theaters.json?page_limit=" + LIMIT + "&page=1&"
                + "country=us&apikey=yedukp76ffytfuy24zsqk7f5";
        return findMovies(link);
    }
    /**
     * Finds the top rated movies.
     * @return an array of top movies
     */
    public static Movie[] getTopMovies() {
        String link = "http://api.rottentomatoes.com/api/public/v1.0/lists/"
                + "dvds/top_rentals.json?limit=" + LIMIT + "&page=1&country="
                + "us&apikey=yedukp76ffytfuy24zsqk7f5";
        return findMovies(link);
    }
    /**
     * Find movies similar to the specified movie.
     * @param id the id of the movie to search for similar movies with
     * @return an array of similar movies
     */
    public static Movie[] getSimilarMovies(int id) {
        String link;
        link = "http://api.rottentomatoes.com/api/public/v1.0/movies/"
                + id + "/similar.json?limit=5&apikey=yedukp76ffytfuy24zsqk7f5";
        return findMovies(link);
    }
    /**
     * Gets movies from Rotten Tomatoes based on the query.
     * @param query the URL to get the JSON from
     * @return the movies matching the query
     */
    public static Movie[] findMovies(String query){
        String callResult = getJsonData(query);
        Gson googleJson = new Gson();
        JsonObject jo = googleJson.fromJson(callResult, JsonObject.class);
        JsonArray movieArray = (JsonArray) jo.get("movies");
        int numMovies = Math.min(LIMIT, movieArray.size());
        Movie[] movies = new Movie[numMovies];
        for (int i = 0; i < numMovies; i++) {
            JsonObject movieJson = (JsonObject) movieArray.get(i);
            String title = movieJson.get("title").getAsString();
            String thumbnail = ((JsonObject) movieJson.get("posters")).get("th"
                    + "umbnail").getAsString();
            int id = movieJson.get("id").getAsInt();
            String synopsis = "None", release = "Unknown";
            if (movieJson.get("synopsis") != null) {
                synopsis = movieJson.get("synopsis").getAsString();
            }
            JsonElement releaseJson = ((JsonObject) movieJson.get("release_"
                    + "dates")).get("theater");
            if (releaseJson != null) {
                release = releaseJson.getAsString();
            }
            movies[i] = new Movie(title, thumbnail, release, synopsis, id);
        }
        return movies;
    }
    /**
     * Gets JSON data from the API.
     * @param link the URL to get the JSON from
     * @return the JSON data
     */
    public static String getJsonData(String link) {
        URL url;
        String jsonData = "";
        try {
            url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HTTPOK) {
                throw new RuntimeException("Failed : "
                        + "HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                jsonData += output;
            }
            conn.disconnect();
            } catch (MalformedURLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (IOException ex) {
                System.out.println("Cannot open url");
                ex.printStackTrace();
            }
        return jsonData;
    }
}