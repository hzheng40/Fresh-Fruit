/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import src.Review;
import src.StudentUser;
import src.UserManager;
import src.MovieUI;
import src.Movie;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chenghanngan
 */
public class MovieUITest {
    
    public MovieUITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of rate method, of class MovieUI.
     */
    @Test
    public void testRate() {
        MovieUI ui = new MovieUI();
        UserManager um = new UserManager();
        ui.movieDetails(new Movie("Some Movie", "Path", "1-1-1970", "IDK", 100));
        um.setUser(new StudentUser("a", "b"));
        ui.setUserManager(um);
        
        //Main success scenario
        int rating = 4;
        String reviewText = "Blah";
        String nav = "home";
        int defaultRating = 5;
        String defaultReviewText = "";
        ui.setRating(rating);
        ui.setReviewText(reviewText);
        
        String result = ui.rate();
        ResultSet rs = UserManager.querySQL("SELECT * FROM REVIEWS WHERE MOVIEID = \'100\'");
        Review review = null;
        try {
            while (rs.next()) {
                review = new Review(Integer.parseInt(rs.getString("STARRATING")), rs.getString("TEXTREVIEW"), (StudentUser)ui.userManager.getUser());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserManager.updateSQL("DELETE FROM REVIEWS WHERE MOVIEID = \'100\'");
        assertNotNull(review);
        assertEquals(rating, review.getStarRating());
        assertEquals(reviewText, review.getTextReview());
        assertEquals(nav, result);
        assertEquals(defaultRating, ui.getRating());
        assertEquals(defaultReviewText, ui.getReviewText());
        
        //Branch if user did not enter rating
        result = ui.rate();
        assertNull(result);
    }
}
