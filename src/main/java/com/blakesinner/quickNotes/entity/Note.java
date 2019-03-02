package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.json.JsonObject;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class represents a note.
 *
 * @author bsinner
 */
@Entity(name = "Note")
@Table(name = "notes")
public class Note {


    private String title;
    private JsonObject contents;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "user_id")
    private int userId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    /**
     * No argument constructor for note.
     */
    public Note() { }

    /**
     * Instantiates a new Note.
     *
     * @param title note title
     * @param contents contents of note
     * @param userId note creator id
     */
    public Note(String title, JsonObject contents, int userId) {
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }

    /**
     * Gets note title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets note title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets note contents.
     *
     * @return the contents
     */
    public JsonObject getContents() {
        return contents;
    }

    /**
     * Sets note contents.
     *
     * @param contents the contents
     */
    public void setContents(JsonObject contents) {
        this.contents = contents;
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
     * Gets user who created note id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user who created note id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

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

}
