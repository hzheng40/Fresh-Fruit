package src;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean (name = "movieui")
@SessionScoped
/**
 * UI logic dealing with movie navigation and searching.
 */
public class MovieUI extends UI {
    /** The movies returned by the search query. */
    private Movie[] movies;

    /** The search query to find movies. */
    private String query;

    /** The movie currently being viewed. */
    private Movie movie;

    /** The user's star rating for the movie. */
    private int rating;

    /** The text of the review. */
    private String reviewText;

    /** The search screen. */
    private static final String SEARCHSCREEN = "search";

    /** The maximum possible star rating. */
    private static final int MAXRATING = 5;

    /**
     * Returns the user's search query.
     * @return the user's search query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets movies currently stored in the UI.
     * @return an array of movies in the UI
     */
    public Movie[] getMovies() {
        return movies;
    }

    /**
     * Gets the current movie being looked at.
     * @return the current movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Returns the text in the review box.
     * @return the text in the review box
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Gets the star rating in the UI.
     * @return rating the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the user's search query.
     * @param query the new search query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Sets the review text in the UI text box.
     * @param reviewText the new review text
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Sets the star rating in the UI.
     * @param rating the new rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the movies that match the user's search query.
     * @return the search screen if succeeded, home screen if not.
     */
    public String search() {
        if (query == null) {
            query = "";
            return "home";
        } else {
            if (query.equalsIgnoreCase("New Releases")) {
                releases();
            } else if (query.equalsIgnoreCase("New DVDs")) {
                dvds();
            } else if (query.equalsIgnoreCase("Top Rated")) {
                top();
            } else if (query.equalsIgnoreCase("Recommended Movies")) {
                recommended();
            } else {
                movies = MovieLogic.searchMovies(query.trim());
            }
            return SEARCHSCREEN;
        }
    }

    /**
     * Gets the newest movie releases.
     * @return the search screen
     */
    public String releases() {
        movies =  MovieLogic.getNewMovies();
        query = "New Releases";
        return SEARCHSCREEN;
    }

    /**
     * Gets the newest DVD releases.
     * @return the search screen
     */
    public String dvds() {
        movies = MovieLogic.getNewDvd();
        query = "New DVDs";
        return SEARCHSCREEN;
    }

    /**
     * Gets the top rated movies.
     * @return an array of the top rated movies
     */
    public String top() {
        movies = MovieLogic.getTopMovies();
        query = "Top Rated";
        return SEARCHSCREEN;
    }

    /**
     * Finds movies similar to the movie being looked at.
     * @param movie the movie to find similar movies for
     * @return the search screen
     */
    public String similar(Movie movie) {
        int id = movie.getId();
        movies = MovieLogic.getSimilarMovies(id);
        return "search";
    }
    /**
     * Gets the recommended movies for the user's major.
     * @return an array of recommended movies
     */
    public String recommended() {
        StudentUser user = (StudentUser)userManager.getUser();
        if (user.getMajor() == Major.Un) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("You need to "
                    + "have decided a major to be recommended movies."));
        } else {
            movies = MovieLogic.recommendMovies(user.getMajor());
            query = "Recommended Movies";
            return SEARCHSCREEN;
        }
        return null;
    }

    /**
     * Navigates to movie details page.
     * @param movie the movie to display details for
     * @return the movie screen
     */
    public String movieDetails(Movie movie) {
        this.movie = movie;
        rating = MAXRATING;
        reviewText = "";
        return "movie";
    }

    /**
     * Adds the user's review to the movie.
     * @return null
     */
    public String rate() {
        FacesContext context = FacesContext.getCurrentInstance();
        if ("".equals(reviewText)) {
            if (context != null) {
                context.addMessage(null, new FacesMessage("You need to enter a"
                        + " review before it can be submitted."));
            }
            return null;
        }
        StudentUser user = (StudentUser)userManager.getUser();
        if (movie != null) {
            movie.addReview(new Review(rating, reviewText, user));
            try {
                ResultSet rs = UserManager.querySQL("SELECT USERID FROM USERS WHERE USERNAME = '" + user.getUsername() + "'");
                int userID = -1;
                if (rs.next()) {
                    userID = rs.getInt("USERID");
                }

                UserManager.updateSQL("INSERT INTO REVIEWS (MOVIEID, STARRATING"
                        + ", TEXTREVIEW, REVIEWMAJOR, USERID) VALUES ('"
                        + movie.getId() + "', " + rating + ",'" + reviewText
                        + "','" + user.getMajor() + "'," + userID + ")");
                if (context != null) {
                    context.addMessage(null, new FacesMessage(
                            "Your review has been submitted."));
                }
            } catch (SQLException err) {
                err.printStackTrace();
            }
            rating = MAXRATING;
            reviewText = "";
        }
        return "home";
    }

    /**
     * Decides whether the star to display is empty or filled.
     * @param position the star's position on the UI
     * @return the path of the star image
     */
    public String starImage(int position) {
        return rating < position ? "StarEmpty.png" : "StarFilled.png";
    }
}