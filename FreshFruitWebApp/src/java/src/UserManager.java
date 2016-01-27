package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/** ManagedBean notation. */
@ManagedBean (name = "userManager")
/** ApplicationScoped notation. */
@ApplicationScoped
/**
 * Manages all users in the system.
 */
public class UserManager {
    /** The list of the users. */
    private Map<String, User> userList = new HashMap<>();
    /** The limit of trials. */
    public static final int LIMITTRIES = 3;
    /** The current user. */
    private User currentUser;
    /** The host of the database. */
    private static final String HOST = "jdbc:derby://localhost:1527/fruit";
    /** The username of the database. */
    private static final String UNAME = "team11";
    /** The pass of the database. */
    private static final String UPASS = "fruit";

    /**
     * Creates a new instance of UserManager.
     */
    public UserManager() {
        ResultSet rs = querySQL("SELECT USERNAME,PASSWORD,EMAIL,"
                + "MAJOR,PREFERENCES,INTEREST,STATUS FROM USERS");
        try {
            while (rs.next()) {
                userList.put(rs.getString("USERNAME"), recreateUser(rs));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * Returns the current user of the application.
     * @return the user of the application.
     */
    public User getUser() {
        return currentUser;
    }

    /**
     * Returns all users in the system.
     * @return a map containing all users in the system
     */
    public Map<String, User> getUsers() {
        return userList;
    }

    /**
     * Sets the current application user.
     * @param user the new user
     */
    public void setUser(User user) {
        currentUser = user;
    }

    /**
     * Makes a new user and puts it in the map.
     * @param user the new user's username
     * @param pass the new user's password
     * @return the new user
     */
    public User makeUser(String user, String pass) {
        User newUser = new StudentUser(user, pass);
        newUser.setUserManager(this);
        userList.put(user, newUser);
        updateSQL("INSERT INTO USERS (USERNAME, PASSWORD)"
                + "VALUES (\'" + user + "\',\'" + pass + "\')");
        return newUser;
    }

    /**
     * Changes a user's username in the hash map.
     * @param oldName the user's old name
     * @param newName the user's new name
     */
    public void changeUsername(String oldName, String newName) {
        User user = find(oldName);
        userList.put(newName, user);
        userList.remove(oldName);
        updateSQL("UPDATE USERS" + " SET USERNAME=\'"
                + newName + "\' WHERE USERNAME=\'" + oldName + "\'");
    }

    /**
     * Finds a user in the map.
     * @param username the user to find
     * @return the User object, or null if the user does not exist
     */
    public User find(String username) {
        return userList.get(username);
    }

    /**
     * Finds a user by his/her unique id in the database.
     * @param id the id to find a user for
     * @return the user with the id, or null if no user is found
     */
    public static User find(int id) {
        ResultSet rs = querySQL("SELECT USERNAME,PASSWORD,EMAIL,MAJOR,"
                + "PREFERENCES,INTEREST,STATUS FROM USERS WHERE USERID=" + id);
        try {
            if (rs.next()) {
                return recreateUser(rs);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }

    /**
     * Makes a user object based on a database query result.
     * @param rs the database result
     * @return the created user object
     */
    public static User recreateUser(ResultSet rs) {
        try {
            User.Status status = User.Status.valueOf(rs.getString("STATUS"));
            if (status == User.Status.Admin) {
                AdminUser newUser = new AdminUser(rs.getString("USERNAME"),
                        rs.getString("PASSWORD"));
                newUser.setEmail(rs.getString("EMAIL"));
                newUser.setStatus(status);
                return newUser;
            } else {
                StudentUser newUser = new StudentUser(rs.getString("USERNAME"),
                        rs.getString("PASSWORD"));
                newUser.setEmail(rs.getString("EMAIL"));
                newUser.setStatus(status);
                newUser.setMajor(Major.valueOf(rs.getString("MAJOR")));
                newUser.setPreferences(rs.getString("PREFERENCES"));
                newUser.setInterest(rs.getString("INTEREST"));
                return newUser;
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }

    /**
     * Executes an SQL query.
     * @param query the SQL query
     * @return the result of the SQL query
     */
    public static ResultSet querySQL(String query) {
        try {
            Connection con = DriverManager.getConnection(HOST, UNAME, UPASS);
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException err) {
            err.printStackTrace();
            return null;
        }
    }

        /**
     * Executes an SQL update.
     * @param query the SQL statement
     */
    public static void updateSQL(String query) {
        try {
            Connection con = DriverManager.getConnection(HOST, UNAME, UPASS);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }
}