package com.blakesinner.quickNotes.entity;

import java.time.LocalDateTime;

/**
 * This class represents a note.
 *
 * @author bsinner
 */
public class Note {


    private String title;
    private String contents;
    private LocalDateTime creationDate;
    private int userId;
    private int id;

    /**
     * No argument constructor for note.
     */
    public Note() { }

    /**
     * Instantiates a new Note.
     */
    public Note(String title, String contents, int userId) {
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
    public String getContents() {
        return contents;
    }

    /**
     * Sets note contents.
     *
     * @param contents the contents
     */
    public void setContents(String contents) {
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
