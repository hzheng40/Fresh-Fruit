package src;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/** ManagedBean notation.*/
@ManagedBean
/** SessionScoped notation.*/
@SessionScoped
/**
 * A generic user. Can be either a StudentUser or an AdminUser.
 */
public class User implements Serializable {
    /** MangedProperty notation. */
    @ManagedProperty("#{userManager}")
    /** The serialVersionUID of the User class. */
    protected static final long serialVersionUID = 666L;
    /** The user manager of the user. */
    protected UserManager userManager;
    /** The username of the user. */
    protected String username;
    /** The password of the user. */
    protected String password;
    /** The email of the user. */
    protected String email;
    /** The status of the user account. */
    protected Status status;
    /** The number of trial of login. */
    protected int numTries;
    /** The name of host of the database. */
    protected static final String HOST = "jdbc:derby://localhost:1527/fruit";
    /** The username of the database. */
    protected static final String UNAME = "team11";
    /** The pass of the database. */
    protected static final String UPASS = "fruit";

    /**
    * Status of the user.
    */
    public enum Status {
        /** the statuses of user. */
        Normal, Locked, Banned, Admin
    }

    /**
     * Creates a new instance of User.
     * @param usern the user's username
     * @param passw the user's password
     */
    public User(String usern, String passw) {
        this.username = usern;
        this.password = passw;
        numTries = 0;
    }

    /**
     * Returns the user's username.
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's password.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's email address.
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the status of the user.
     * @return the status of the user
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the status of the user as a string.
     * @return a string representing the user's status
     */
    public String getStatusString() {
        if (status == null) {
            return "";
        } else {
            return status.toString();
        }
    }

    /**
     * Gets the number of trial.
     * @return numTries
     */
    public int getNumTries() {
        return numTries;
    }

    /**
     * Sets the user's username.
     * @param usernm the new username
     */
    public void setUsername(String usernm) {
        username = usernm;
    }

    /**
     * Sets the user's password.
     * @param pass the new password
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Sets the user's email address.
     * @param eml the new email address
     */
    public void setEmail(String eml) {
        email = eml;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sets the user's status and updates it in the database.
     * @param status The new status
     */
    public void setStatusString(String status) {
        this.status = Status.valueOf(status);
        UserManager.updateSQL("UPDATE USERS SET STATUS = \'"
                + status + "\' WHERE USERNAME = \'" + username + "\'");
    }

    /**
     * Sets the number of trials.
     * @param num the number
     */
    public void setNumTries(int num) {
        numTries = num;
    }
    /**
     * Checks the login information.
     * @param pass the attempted password
     * @return true if the password is correct
     */
    public boolean checkLogin(String pass) {
        if (pass.equals(password)) {
            return true;
        } else {
            numTries++;
            if (numTries >= UserManager.LIMITTRIES
                    && this instanceof StudentUser) {
                setStatus(Status.Locked);
            }
            return false;
        }
    }

    /**
     * Checks if the account is locked.
     * @return true if the account is locked
     */
    public boolean isLocked() {
        return getStatus() == Status.Locked;
    }

    /**
     * Checks if the account is banned.
     * @return true if the account is banned
     */
    public boolean isBanned() {
        return getStatus() == Status.Banned;
    }

    /**
     * Sets the user manager.
     * @param umanager the user manager
     */
    public void setUserManager(UserManager umanager) {
        userManager = umanager;
    }

    /**
     * Gets the user manager.
     * @return returns the UserManager instance
     */
    public UserManager getUserManager() {
        return userManager;
    }
}