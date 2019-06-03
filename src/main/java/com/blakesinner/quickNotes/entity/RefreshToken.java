package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity for refresh tokens, a refresh token is used to get new
 * access tokens without the user logging in again.
 *
 * @author bsinner
 */
@Entity(name = "RefreshToken")
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "expire_date", insertable = false)
    private LocalDateTime expireDate;

    /**
     * Instantiates a new Refresh token.
     */
    public RefreshToken() {
    }

    /**
     * Instantiates a new Refresh token with a user.
     *
     * @param user the user
     */
    public RefreshToken(User user) {
        this.user = user;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(UUID id) {
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
     * Gets expire date.
     *
     * @return the expire date
     */
    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    /**
     * Sets expire date.
     *
     * @param expireDate the expire date
     */
    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * Get RefreshToken string.
     *
     * @return the token string
     */
    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", user=" + user +
                ", expireDate=" + expireDate +
                '}';
    }

    /**
     * Get if two tokens are the same token.
     *
     * @param o the object to compare
     * @return  true if tokens are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken token = (RefreshToken) o;
        return Objects.equals(id, token.id) &&
                Objects.equals(user, token.user) &&
                Objects.equals(expireDate, token.expireDate);
    }

}
