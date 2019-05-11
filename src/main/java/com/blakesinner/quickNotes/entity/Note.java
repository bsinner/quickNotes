package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

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
    private String contents;

    @Column(name = "creation_date", insertable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private User user;

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
     * @param title    note title
     * @param contents contents of note
     * @param user     note creator id
     */
    public Note(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
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
     * Gets user.
     *
     * @return user that created note
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user user that created note
     */
    public void setUser(User user) {
        this.user = user;
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

    /**
     * Test if the current note and a passed in note are equal.
     *
     * @param o note to compare to
     * @return true if the notes are equal, false if they aren't equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (this == null || getClass() != o.getClass()) {
            return false;
        }

        Note note = (Note) o;

        return title.equals(note.getTitle())
                && user.equals(note.getUser())
                && id == note.getId()
                && contents.equals(note.getContents());

    }

}
