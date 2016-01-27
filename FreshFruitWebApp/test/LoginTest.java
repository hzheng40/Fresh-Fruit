import src.StudentUser;
import src.ProfileUI;
import src.User;
import src.UserManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for login()
 * @author Hongrui Zheng
 */
public class LoginTest {
    private ProfileUI ui;
    private UserManager um;
    private StudentUser user;
    public LoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() { 
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ui = new ProfileUI();
        um = new UserManager();
        user = (StudentUser) um.makeUser("user", "pass");
        ui.setUserManager(um);
        um.setUser(user);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of login method
     */
    @Test 
    public void testLoginEmpty() {
        ui.setUsername("");
        ui.setPassword("");
        if (ui.getContext() == null) {
            
            try {
                assertNull(ui.login());
            } catch (NullPointerException npe) {
            }
            assertEquals("No username entered.", ui.getMessage());
        }
    }
    
    @Test
    public void testLoginLocked() {
        user.setStatus(User.Status.Locked);
        um.setUser(user);
        ui.setUsername("user");
        ui.setPassword("pass");
        ui.setUserManager(um);
        if (ui.getContext() == null) {
            try {
                assertNull(ui.login());
            } catch (NullPointerException npe) {
            }
            assertEquals("You have exceeded your number of attempts to log in.", ui.getMessage());
        }
    }
    
    @Test
    public void testLoginBanned() {
        user.setStatus(User.Status.Banned);
        um.setUser(user);
        ui.setUsername("user");
        ui.setPassword("pass");
        ui.setUserManager(um);
        if (ui.getContext() == null) {
            try {
                assertNull(ui.login());
            } catch (NullPointerException npe) {
            }
            assertEquals("You have been banned from this application.", ui.getMessage());
        }
    }
    
    @Test
    public void testLoginSuccess() {
        ui.setUsername("user");
        ui.setUsername("pass");
        if (ui.getContext() == null) {
            try {
                assertEquals("home", ui.login());
            } catch (NullPointerException npe) {
            }
            assertEquals(true, user.checkLogin("pass"));
        }
    }
    
    @Test
    public void testLoginFail() {
        ui.setUsername("user");
        ui.setPassword("wrongpass");
        if (ui.getContext() == null) {
            try {
                assertNull(ui.login());
            } catch (NullPointerException npe) {
            }
            assertEquals(false, user.checkLogin(ui.getPassword()));
        }
    }
}