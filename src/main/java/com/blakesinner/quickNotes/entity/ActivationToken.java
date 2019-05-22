package com.blakesinner.quickNotes.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The account activation token entity.
 *
 * @author bsinner
 */
@Entity(name = "ActivationToken")
@Table(name = "activation_tokens")
public class ActivationToken {

    @Id
    private String id;

    @ManyToOne
    private User user;

    @Column(name = "creation_date", insertable = false)
    private LocalDateTime creationDate;

    /**
     * Zero argument constructor.
     */
    public ActivationToken() { }

    /**
     * Construct with User, the only non auto generated field.
     *
     * @param user the user
     */
    public ActivationToken(User user) {
        this.user = user;
    }

    /**
     * All argument constructor.
     *
     * @param id           the id
     * @param user         the user
     * @param creationDate the creation date
     */
    public ActivationToken(String id, User user, LocalDateTime creationDate) {
        this.id = id;
        this.user = user;
        this.creationDate = creationDate;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creation date.
     *
     * @param creationDate the creation date
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get toString of token properties.
     *
     * @return the token string
     */
    @Override
    public String toString() {
        return "ActivationToken{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", creationDate=" + creationDate +
                '}';
    }
}
