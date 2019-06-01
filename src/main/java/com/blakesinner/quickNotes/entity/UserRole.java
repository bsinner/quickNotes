package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Represents one user role.
 *
 * @author bsinner 
 */
@Entity(name = "UserRole")
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String role;

    @ManyToOne
    private User user;

    /**
     * Constructs UserRole.
     *
     * @param role the role
     */
    public UserRole(String role) { this.role = role; }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
