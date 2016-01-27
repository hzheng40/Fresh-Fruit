/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.ProfileUI;
import src.UserManager;

/**
 *
 * @author Greg
 */
public class TestEditProfile {
    /** The username that will be tested. */
    private String username;
    /** The password that will be tested. */
    private String password;


    public TestEditProfile() {
    }
    /**
     *Begin Setup
     */
    @Before
    public void setUp() {
    }
    /**
     * Test of editProfile method, Branch: Blank Username.
     */
    @Test
    public void testEditBlankUsername() {
        ProfileUI editprof = new ProfileUI() {
            private String m;
            public String getMessage() {
                return m;
            }
            @Override
            public void displayMessage(String msg) {
                m = msg;
            }
        };
        editprof.setUsername("");
        String result = editprof.register();

        assertEquals(result, null);
        assertEquals(editprof.getMessage(), "No username entered.");
    }
    /**
     * Test of editProfile method, Branch: Duplicate username.
     */
    @Test
    public void testDuplicateUsername() {
        ProfileUI editprof = new ProfileUI() {
            private String m;
            public String getMessage() {
                return m;
            }
            @Override
            public void displayMessage(String msg) {
                m = msg;
            }
        };
        editprof.setUsername("user");
        editprof.userManager = new UserManager();
        editprof.userManager.makeUser("user", "pass");
        String result = editprof.register();

        assertEquals(result, null);
        assertEquals(editprof.getMessage(), "Username already exists.");
    }
    /**
     * Test of editProfile method, Branch: Test success.
     */
    @Test
    public void testSucess() {
        ProfileUI editprof = new ProfileUI() {
            private String m;
            @Override
            public String getMessage() {
                return m;
            }
            @Override
            public void displayMessage(String msg) {
                m = msg;
            }
        };
        editprof.userManager = new UserManager();
        editprof.userManager.makeUser("user", "pass");
        String result = editprof.register();
        assertEquals(result, "home");
        assertEquals(editprof.getMessage(), "Registration successful.");
    }
}