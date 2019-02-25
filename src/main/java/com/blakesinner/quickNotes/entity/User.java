package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * This class to represents a user.
 *
 * @author bsinner
 */
@Entity(name = "User")
@Table(name = "users")
public class User {
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "isAdmin")
    private int isAdmin;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    /**
     * No argument constructor.
     */
    public User() { }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param isAdmin  the is admin
     */
    public User(String username, String password, String email, int isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    /**
     * Get the user's email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email.
     * @param email the email to set
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
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     * @param userName username to set
     */
    public void setUsername(String userName) {
        this.username = username;
    }

    /**
     * Get the user's id.
     * @return the id
     */
    public int getId() { return id; }

    /**
     * Set the users's id.
     * @param id the id to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * Get value of isAdmin
     * @return 0 if the user doesn't have administrative privileges, 1 if they do
     */
    public int getIsAdmin() { return isAdmin; }

    /**
     * Set isAdmin field.
     * @param isAdmin 0 to represent a regular user, 1 to represent an admin user
     */
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }
}