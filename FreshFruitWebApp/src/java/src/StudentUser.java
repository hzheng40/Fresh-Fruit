package src;

/**
 * A normal user with the ability to search and rate movies.
 */
public class StudentUser extends User {
    /**
     * StudentUser Constructor.
     * @param username the username of the student user
     * @param password the password of the student user
     */
    public StudentUser(String username, String password) {
        super(username, password);
        major = Major.Un;
    }
    /** The user's major.*/
    private Major major;
    /** The user's preferences.*/
    private String preferences;
    /** The user's interest.*/
    private String interest;
    /**
     * Returns the user's major.
     * @return the user's major
     */
    public Major getMajor() {
        return major;
    }
    /**
     * Returns the user's interests.
     * @return the user's interests
     */
    public String getInterest() {
        return interest;
    }
    /**
     * Returns the user's preferences.
     * @return the user's preferences
     */
    public String getPreferences() {
        return preferences;
    }
    /**
     * Sets the user's major.
     * @param major the new major
     */
    public void setMajor(Major major) {
        this.major = major;
    }
    /**
     * Sets the user's interests.
     * @param interest the new interests
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }
    /**
     * Sets the user's preferences.
     * @param preferences the new preferences
     */
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    /**
     * Edits the profile of a user.
     * @param user the user's new username
     * @param pass the user's new password
     * @param email the user's new email
     * @param major the user's new major
     * @param preferences the user's new preferences
     * @param interest the user's new interests
     */
    public void editProfile(String user, String pass, String email, Major major,
            String preferences, String interest) {
        String oldUsername = username;
        if (!username.equals(user)) {
            userManager.changeUsername(username, user);
        }
        username = user;
        password = pass;
        this.email = email;
        this.major = major;
        this.preferences = preferences;
        this.interest = interest;
        UserManager.updateSQL("UPDATE USERS"
                    + " SET USERNAME=\'" + user + "\', PASSWORD=\'" + pass
                    + "\', EMAIL=\'" + email + "\', MAJOR=\'"
                    + major + "\', PREFERENCES=\'"
                    + preferences + "\', INTEREST=\'" + interest
                    + "\' WHERE USERNAME=\'" + oldUsername + "\'");
    }
}