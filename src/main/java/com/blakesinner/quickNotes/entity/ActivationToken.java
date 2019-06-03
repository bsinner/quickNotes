package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The account activation token entity.
 *
 * @author bsinner
 */
@Entity(name = "ActivationToken")
@Table(name = "activation_tokens")
public class ActivationToken {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "creation_date", insertable = false)
    private LocalDateTime creationDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

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
