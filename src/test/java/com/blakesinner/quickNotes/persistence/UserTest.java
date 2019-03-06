package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.test.util.DatabaseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the User DAO.
 *
 * @author bsinner
 */
class UserTest {

    GenericDAO<User> dao;

    /**
     * Initialize user DAO.
     */
    @BeforeEach
    void setUp() {

        dao = new GenericDAO<>(User.class);

        DatabaseUtility dbUtil = new DatabaseUtility();

        dbUtil.runSQL("target/test-classes/setupUserTests.sql");
    }

    /**
     * Test that get all users gets the number of users in the
     * user table.
     */
    @Test
    void testGetAll() {
        List<User> users = dao.getAll();
        assertEquals(3, users.size());
    }

    /**
     * Tests getUsersByUsername method success.
     */
    @Test
    void testGetUsersByUsername() {
        List<User> users = dao.getByPropertyLike("username", "smith");
        assertEquals(2, users.size());
    }

    /**
     * Tests if get user by id returns the correct user.
     */
    @Test
    void testGetUserById() {
        List<User> foundUser = dao.getByPropertyEqual("id", "1");
        assertNotNull(foundUser);
        assertEquals(1, foundUser.size());
        assertEquals(1, foundUser.get(0).getId());
    }

    /**
     * Verify successful insert of a user
     */
    @Test
    void testInsert() {
        User newUser = new User("rsmith", "password4", "rsmith@gmail.com");
        int id = dao.insert(newUser);

        User insertedUser = dao.getByPropertyEqual("id", String.valueOf(id)).get(0);

        assertNotEquals(0, id);
        assertEquals(newUser, insertedUser);
    }

    /**
     * Verify successful insert of user with note.
     */
    @Test
    void testInsertWithNote() {
        User newUser = new User("ksmith", "password8", "ksmith@gmail.com");
        Note newNote = new Note("Untitled", "{}", newUser);
        newUser.addNote(newNote);

        int id = dao.insert(newUser);

        User insertedUser = dao.getByPropertyEqual("id", String.valueOf(id)).get(0);

        assertNotEquals(0, id);
        assertEquals(newUser, insertedUser);
        assertEquals(1, newUser.getNotes().size());

        newUser.removeNote(newNote);
        assertEquals(0, newUser.getNotes().size());
    }

    /**
     * Verify successful delete of user.
     */
    @Test
    void testDelete() {
        setUpDeleteTest();

        GenericDAO<Note> noteDao = new GenericDAO<>(Note.class);
        User user = dao.getByPropertyEqual("username", "ldavis").get(0);

        int userCount = dao.getAll().size();
        int noteCount = noteDao.getAll().size();
        int userNoteCount = user.getNotes().size();
        int userId = user.getId();

        dao.delete(user);

        assertEquals(userCount - 1, dao.getAll().size());
        assertEquals(noteCount - userNoteCount, noteDao.getAll().size());
        assertEquals(0, dao.getByPropertyEqual("id", String.valueOf(userId)).size());

    }

    /**
     * Verify successful get by property (equal match)
     */
    @Test
    void testGetByPropertyEqual() {
        List<User> users = dao.getByPropertyLike("username", "bsmith");
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    /**
     * Verify successful get by property (like match)
     */
    @Test
    void testGetByPropertyLike() {
        List<User> users = dao.getByPropertyLike("username", "smith");
        assertEquals(2, users.size());
    }

    /**
     * Prepare the note table to be used in the delete user test.
     */
    void setUpDeleteTest() {
        DatabaseUtility dbUtil = new DatabaseUtility();
        dbUtil.runSQL("target/test-classes/setupNoteTests.sql");

    }

}