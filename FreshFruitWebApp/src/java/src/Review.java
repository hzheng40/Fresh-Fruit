package src;

/**
 * A movie review and its details.
 */
public class Review {
    private int starRating;
    private String textReview;
    private StudentUser reviewer;
    
    public Review(int starRating, String textReview, StudentUser reviewer) {
        this.starRating = starRating;
        this.textReview = textReview;
        this.reviewer = reviewer;
    }
    
    /**
     * Returns the star rating of the review.
     * @return the star rating
     */
    public int getStarRating() {
        return starRating;
    }
    
    /**
     * Returns the text in the review.
     * @return the review's text
     */
    public String getTextReview() {
        return textReview;
    }
    
    /**
     * Returns the reviewer.
     * @return the reviewer
     */
    public StudentUser getReviewer() {
        return reviewer;
    }
    
    /**
     * Decides whether the star to display is empty or filled.
     * @param position the star's position on the UI
     * @return the path of the star image
     */
    public String starImage(int position) {
        return starRating < position ? "StarEmpty.png" : "StarFilled.png";
    }
}
