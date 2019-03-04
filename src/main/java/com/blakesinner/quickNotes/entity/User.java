package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class to represents a user.
 *
 * @author bsinner
 */
@Entity(name = "User")
@Table(name = "users")
public class User {

    private String username;
    private String password;
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Note> notes = new HashSet<>();

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
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Get the user's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     *
     * @param password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     *
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the user's id.
     *
     * @return the id
     */
    public int getId() { return id; }

    /**
     * Set the users's id.
     *
     * @param id the id to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * Get user notes.
     *
     * @return user notes
     */
    public Set<Note> getNotes() {
        return notes;
    }

    /**
     * Set user notes.
     *
     * @param notes user notes
     */
    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    /**
     * Add note to notes.
     * @param note note to add
     */
    public void addNote(Note note) {
        notes.add(note);
        note.setUser(this);
    }

    /**
     * Remove note from notes
     * @param note note to remove
     */
    public void removeNote(Note note) {
        notes.remove(note);
        note.setUser(null);
    }

    /**
     * Compares a passed in object to the current user object.
     * @param o object to compare the current user to
     * @return true if the passed in object represents the current user, false if it
     * isn't identical
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (this == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return username.equals(user.username)
                && password.equals(user.password)
                && email.equals(user.email);
    }

}