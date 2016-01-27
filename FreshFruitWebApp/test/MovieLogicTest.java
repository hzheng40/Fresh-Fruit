/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import src.Movie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Benjamin
 */
public class MovieLogicTest {
    
    Movie movie;
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testMovieWithSynopsisReleaseDate() {
           int id = 771324839;
           String title = "Jurassic World";
           String imgPath = "http://resizing.flixster.com/zJnnEYLx2T9TbVL9UspqrKV7X2g=/51x81/dkpu1ddg7pbsk.cloudfront.net/movie/11/19/12/11191223_ori.jpg";
           String release = "2015-06-12";
           String synop = "Steven Spielberg returns to executive produce the long-awaited next installment of his groundbreaking Jurassic Park series, Jurassic World. Colin Trevorrow directs the epic action-adventure based on characters created by Michael Crichton. The screenplay is by Rick Jaffa & Amanda Silver and Derek Connolly & Trevorrow, and the story is by Rick Jaffa & Amanda Silver. Frank Marshall and Patrick Crowley join the team as producers. (C) Universal";
           
           movie = new Movie(title, imgPath, release, synop,id);
           Assert.assertEquals("Error, incorrect ID", id, movie.getId());
           Assert.assertEquals("Error, incorrect image path", imgPath, movie.getImagePath());
           Assert.assertEquals("Error, incorrect Release Date", release, movie.getReleaseDate());
           Assert.assertEquals("Error, incorrect Synopsis", synop, movie.getSynopsis());
    }
    
    @Test 
    public void testMovieWithoutSynopsis() {
        int id = 22494;
        
        String title = "Titanic";
        String imgPath = "http://resizing.flixster.com/sWle3WJBfZ4Afd0F7SK11vyNJmo=/54x81/dkpu1ddg7pbsk.cloudfront.net/movie/11/16/63/11166320_ori.jpg";
        String release = "1997-12-19";
        String synop = "";

        movie = new Movie(title, imgPath, release, synop,id);
        Assert.assertEquals("Error, incorrect ID", id, movie.getId());
        Assert.assertEquals("Error, incorrect image path", imgPath, movie.getImagePath());
        Assert.assertEquals("Error, incorrect Release Date", release, movie.getReleaseDate());
        Assert.assertEquals("Error, incorrect Synopsis", synop, movie.getSynopsis());
    }
    
    @Test
    public void testMovieWithoutRelease() {
        int id = 770724989;
        
        String title = "The Interview";
        String imgPath = "http://resizing.flixster.com/Q_OsHpd6JABA-acSWmyOiEEi01I=/54x74/dkpu1ddg7pbsk.cloudfront.net/movie/10/84/97/10849785_ori.jpg";
        String release = "Unknown";
        String synop = "";
                
        movie = new Movie(title, imgPath, release, synop,id);
        Assert.assertEquals("Error, incorrect ID", id, movie.getId());
        Assert.assertEquals("Error, incorrect image path", imgPath, movie.getImagePath());
        Assert.assertEquals("Error, incorrect Release Date", release, movie.getReleaseDate());
        Assert.assertEquals("Error, incorrect Synopsis", synop, movie.getSynopsis());
        
    }
 
    
      
}
