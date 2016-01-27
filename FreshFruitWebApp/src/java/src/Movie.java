package src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A movie and its details.
 */
public class Movie {
    private String title;
    private List<Review> reviews;
    private String releaseDate;
    private String synopsis;
    private String imagePath;
    private int id;

    /**
     * Constructor for a movie object.
     * @param title title of the movie
     * @param imagePath url to img of movie
     * @param id Rotten Tomatoes movie ID
     */
    public Movie(String title, String imagePath, int id) {
        this.title = title;
        this.imagePath = imagePath;
        reviews = new ArrayList<>();
        releaseDate = "2000-01-01";
        synopsis = "Something else";
        this.id = id;
    }

    /**
     * Constructor for movie object which include "releaseDate" and "synopsis".
     * @param title title of movie
     * @param imagePath url to img of movie
     * @param releaseDate theater release date of movie
     * @param synopsis synopsis of move
     * @param id RottenTomatoes id of movie
     */
    public Movie(String title, String imagePath, String releaseDate,
                 String synopsis, int id) {
        this.title = title;
        this.imagePath = imagePath;
        reviews = new ArrayList<>();
        this.releaseDate = releaseDate;
        this.synopsis = synopsis;
        this.id = id;

        try {
            ResultSet rs = UserManager.querySQL("SELECT STARRATING, TEXTREVIEW"
                    + ", USERID FROM REVIEWS WHERE MOVIEID='" + id + "'");
            while (rs.next()) {
                User tempUser = UserManager.find(rs.getInt("USERID"));
                if (tempUser instanceof StudentUser) {
                    reviews.add(new Review(Integer.parseInt(
                            rs.getString("STARRATING"))
                            , rs.getString("TEXTREVIEW")
                            , (StudentUser) tempUser));
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * Returns the movie's id.
     * @return the movie's id
     */
    public final int getId() {
        return id;
    }

    /**
     * Returns the movie's title.
     * @return the movie's title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Returns the URL of the image representing the movie.
     * @return the image path of the movie's image
     */
    public final String getImagePath() {
        return imagePath;
    }

    /**
     * Returns the movie's release date.
     * @return the movie's release date
     */
    public final String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the movie's reviews, limited to a certain number.
     * @return a List of movie reviews
     */
    public final List<Review> getReviews() {
        return reviews.size() < MovieLogic.LIMIT ? reviews
                : reviews.subList(0, MovieLogic.LIMIT);
    }

    /**
     * Returns the movie's synopsis.
     * @return the movie's synopsis
     */
    public final String getSynopsis() {
        return synopsis;
    }

    /**
     * Returns the movie's average rating.
     * @return the movie's average rating
     */
    public int avgScore() {
        int total = 0;
        for (Review review : reviews) {
            total += review.getStarRating();
        }
        total /= reviews.size();
        return total;
    }

    /**
     * Adds a review to the movie.
     * @param review the review to add
     */
    public void addReview(Review review) {
        reviews.add(review);
    }
}
