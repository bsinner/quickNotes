package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.entity.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.net.www.content.text.Generic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DAO for setting up activation tokens, and using them to activate users.
 *
 * @author bsinner
 */
public class ActivationDAO extends GenericDAO<ActivationToken> {

    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * No argument constructor for ActivationDAO.
     */
    public ActivationDAO() {
        setType(ActivationToken.class);
    }

    /**
     * Assign id to token, save to database, return id of created token.
     *
     * @param token the token
     * @return      the id
     */
    public String insertToken(ActivationToken token) {
        Session session = sessionFactory.openSession();
        String id = null;
        token.setId(UUID.randomUUID().toString());

        Transaction transaction = session.beginTransaction();
        id = (String)session.save(token);
        transaction.commit();

        session.close();
        return id;
    }

    /**
     * Activate a user's account and delete the activation token.
     *
     * @param token the account activation token
     * @return      the activated user
     */
    public User activateUser(ActivationToken token) {
        User user = token.getUser();

        User activatedUser = updateUser(user);
        GenericDAO<User> userDao = new GenericDAO<>(User.class);

        userDao.saveOrUpdate(activatedUser);
        List<User> results = userDao
                .getByPropertyEqual("id", String.valueOf(activatedUser.getId()));

        super.delete(token);

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Delete a users UNACTIVATED role and add the USER role to show
     * that they are activated.
     *
     * @param user the user to activate
     * @return     the activated user with updated roles
     */
    private User updateUser(User user) {

        user.addRole(new UserRole("USER"));

        Set<UserRole> toDelete = user.getUserRoles().stream()
                .filter(r -> r.getRole().equals("UNACTIVATED"))
                .collect(Collectors.toCollection(HashSet::new));

        toDelete.forEach(user::removeRole);

        return user;
    }


}
