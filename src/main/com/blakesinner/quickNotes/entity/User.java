/**
 * This class to represents a user.
 *
 * @author bsinner
 */
public class User {
    private String userName;
    private String password;
    private String email;

    /**
     * No arguement constructor.
     */
    public User() { }

    /**
     * Get the user's email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email.
     * @param the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     * @param password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the username.
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the username.
     * @param the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}