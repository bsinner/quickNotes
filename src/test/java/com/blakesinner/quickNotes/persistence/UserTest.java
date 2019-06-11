package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.test.util.DatabaseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the User DAO.
 *
 * @author bsinner
 */
class UserTest {

    private GenericDAO<User> dao;

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
        assertEquals(1, users.size());
    }

    /**
     * Tests if get user by id returns the correct user.
     */
    @Test
    void testGetUserById() {
        User foundUser = dao.getById(1);
        assertNotNull(foundUser);
    }

    /**
     * Verify successful insert of a user
     */
    @Test
    void testInsert() {
        User newUser = new User("rsmith", "password4", "rsmith@gmail.555");
        int id = dao.insert(newUser);

        User insertedUser = dao.getById(id);

        assertNotEquals(0, id);
        assertEquals(newUser, insertedUser);
    }

    /**
     * Verify successful insert of user with note.
     */
    @Test
    void testInsertWithNote() {
        User newUser = new User("ksmith", "password8", "ksmith@gmail.555");
        Note newNote = new Note("Untitled", "{}", newUser);
        newUser.addNote(newNote);

        int id = dao.insert(newUser);

        User insertedUser = dao.getById(id);

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
        assertNull(dao.getById(userId));

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
        assertEquals(1, users.size());
    }

    /**
     * Verify successful get by properties equal
     */
    @Test
    void testGetByPropertiesEqual() {
        Map<String, String> properties = new HashMap<>();
        properties.put("username", "bsmith");
        properties.put("id", "1");

        List<User> users = dao.getByPropertiesEqual(properties);

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
        assertEquals("bsmith@gmail.555", users.get(0).getEmail());
    }

    /**
     * Prepare the note table to be used in the delete user test.
     */
    void setUpDeleteTest() {
        DatabaseUtility dbUtil = new DatabaseUtility();
        dbUtil.runSQL("target/test-classes/setupNoteTests.sql");

    }

}