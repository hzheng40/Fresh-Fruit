package src;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean (name = "ui")
@SessionScoped
/**
 * A generic UI class. Can be overridden (ProfileUI/MovieUI).
 */
public class UI {

    /** Manages all users. */
    @ManagedProperty("#{userManager}")
    public UserManager userManager;

    /**
     * Sets the user manager.
     * @param um the user manager
     */
    public void setUserManager(UserManager um) {
        userManager = um;
    }
}