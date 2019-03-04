package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.test.util.DatabaseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Note test class.
 *
 * @author bsinner
 */
public class NoteTest {

    GenericDAO<Note> dao;
    Note testNote;
    User testUser;

    /**
     * Set up for note tests.
     */
    @BeforeEach
    void setUp() {
        dao = new GenericDAO<>(Note.class);

        testUser = new GenericDAO<>(User.class).getByPropertyEqual("id", "1").get(0);
        testNote = new Note("test_note", "{}", testUser);

        DatabaseUtility dbUtil = new DatabaseUtility();

        dbUtil.runSQL("target/test-classes/cleanNotes.sql");
        dbUtil.runSQL("target/test-classes/populateNotes.sql");
    }

    /**
     * Test getAll method.
     */
    @Test
    void testGetAll() {
        List<Note> notes = dao.getAll();
        assertEquals(3, notes.size());
    }

    /**
     * Test saveOrUpdate method.
     */
    @Test
    void testUpdate() {

        int newId = dao.insert(testNote);

        testNote.setId(newId);
        testNote.setTitle("testNote");
        dao.saveOrUpdate(testNote);

        Note updatedNote = dao.getByPropertyEqual("id", String.valueOf(newId)).get(0);

        assertEquals(testNote, updatedNote);
    }

    /**
     * Test insert method.
     */
    @Test
    void testInsert() {
        testUser.addNote(testNote);

        int newId = dao.insert(testNote);

        Note newNote = dao.getByPropertyEqual("id", String.valueOf(newId)).get(0);

        assertNotEquals(0, newId);
        assertEquals(testNote, newNote);
        assertEquals("bsmith", newNote.getUser().getUsername());
    }

    /**
     * Test delete method.
     */
    @Test
    void testDelete() {
        dao.delete(dao.getByPropertyEqual("title", "Lorem Ipsum").get(0));
        assertEquals(0, dao.getByPropertyEqual("title", "Lorem Ipsum").size());
    }

    /**
     * Test getByPropertyEqual method.
     */
    @Test
    void testGetByPropertyEqual() {
        List<Note> notes = dao.getByPropertyEqual("id", "1");
        assertEquals(1, notes.size());
        assertEquals(1, notes.get(0).getId());
    }

    /**
     * Test getByPropertyLike method.
     */
    @Test
    void testGetByPropertyLike() {
        List<Note> notes = dao.getByPropertyLike("title", "World");
        assertEquals(2, notes.size());
    }
}
