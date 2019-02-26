package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quicknotes.test.util.DatabaseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the User DAO.
 *
 * @author bsinner
 */
class UserDAOTest {

    UserDAO dao;

    /**
     * Initialize user DAO.
     */
    @BeforeEach
    void setUp() {
        dao = new UserDAO();

        DatabaseUtility dbUtil = new DatabaseUtility();
        dbUtil.runSQL("/home/student/IdeaProjects/quickNotes/src/test/resources/cleandb.sql");
        dbUtil.runSQL("/home/student/IdeaProjects/quickNotes/src/test/resources/populatedb.sql");
    }

    /**
     * Test that get all users gets the number of users in the
     * user table.
     */
    @Test
    void getAllUsersSuccess() {
        List<User> users = dao.getAllUsers();
        assertEquals(3, users.size());
    }

    /**
     * Tests getUsersByUsername method success.
     */
    @Test
    void getUsersByUsernameSuccess() {
        List<User> users = dao.getByPropertyLike("username", "smith");
        assertEquals(2, users.size());
    }

    /**
     * Tests if get user by id returns the correct user.
     */
    @Test
    void getUserByIdSuccess() {
        List<User> foundUser = dao.getByPropertyEqual("id", "1");
        assertNotNull(foundUser);
        assertEquals(1, foundUser.size());
        assertEquals(1, foundUser.get(0).getId());
    }

    /**
     * Verify successful insert of a user
     */
    @Test
    void insertSuccess() {
        User newUser = new User("rsmith", "password4", "rsmith@gmail.com");

        int id = dao.insert(newUser);
        assertNotEquals(0, id);
        User insertedUser = dao.getByPropertyEqual("id", String.valueOf(id)).get(0);

        assertEquals("rsmith", insertedUser.getUsername());
        // Could continue comparing all values, but
        // it may make sense to use .equals()
        // TODO review .equals recommendations http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#mapping-model-pojo-equalshashcode
    }

    /**
     * Verify successful delete of user
     */
    @Test
    void deleteSuccess() {
        dao.delete(dao.getByPropertyEqual("username", "ldavis").get(0));
        assertEquals(dao.getByPropertyEqual( "username", "ldavis").size(), 0);
    }

    /**
     * Verify successful get by property (equal match)
     */
    @Test
    void getByPropertyEqualSuccess() {
        List<User> users = dao.getByPropertyLike("username", "bsmith");
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    /**
     * Verify successful get by property (like match)
     */
    @Test
    void getByPropertyLikeSuccess() {
        List<User> users = dao.getByPropertyLike("username", "smith");
        assertEquals(2, users.size());
    }

}